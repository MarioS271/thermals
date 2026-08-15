// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details A shim between PawnIO and the main java app which interacts with PawnIO and hands data to java via stdout
 * @authors MarioS271
 */

#include <windows.h>
#include <d3dkmthk.h>
#include <intrin.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <filesystem>
#include <format>
#include "PawnIOLib.h"
#include "nvapi.h"

#pragma comment(lib, "gdi32.lib")

namespace fs = std::filesystem;

using std::string, std::wstring, std::vector;

constexpr ULONG64 MSR_IA32_PACKAGE_THERM_STATUS = 0x1b1;
constexpr ULONG64 MSR_IA32_PERF_STATUS          = 0x198;
constexpr ULONG64 MSR_IA32_TEMPERATURE_TARGET   = 0x1a2;
constexpr ULONG64 MSR_AMD_PSTATE0               = 0xC0010064;
constexpr ULONG64 SMN_THM_TCON_CUR_TMP          = 0x00059800;

static vector<uint8_t> load_file(const fs::path& path) {
    std::ifstream f(path, std::ios::binary);
    return {std::istreambuf_iterator(f), {}};
}

static fs::path exe_dir() {
    wchar_t buf[MAX_PATH];
    GetModuleFileNameW(nullptr, buf, MAX_PATH);
    return fs::path(buf).parent_path();
}

static bool cpu_is_intel() {
    int info[4];
    __cpuid(info, 0);
    return info[1] == 0x756e6547 && info[3] == 0x49656e69 && info[2] == 0x6c65746e;
}

static bool cpu_is_amd() {
    int info[4];
    __cpuid(info, 0);
    return info[1] == 0x68747541 && info[3] == 0x69746e65 && info[2] == 0x444d4163;
}

static bool read_msr(HANDLE h, ULONG64 msr, ULONG64& out) {
    const ULONG64 in_buf[1] = { msr };
    ULONG64 out_buf[1] = {};
    SIZE_T returned = 0;

    if (FAILED(pawnio_execute(h, "ioctl_read_msr", in_buf, 1, out_buf, 1, &returned)))
        return false;

    out = out_buf[0];
    return true;
}

static bool read_smn(HANDLE h, ULONG64 offset, ULONG64& out) {
    const ULONG64 in_buf[1] = { offset };
    ULONG64 out_buf[1] = {};
    SIZE_T returned = 0;

    if (FAILED(pawnio_execute(h, "ioctl_read_smn", in_buf, 1, out_buf, 1, &returned)))
        return false;

    out = out_buf[0];
    return true;
}

struct GpuInfo {
    wstring name;
    double temp_c;
    ULONG fan_rpm;
    double gpu_usage_pct;
    ULONG64 mem_freq_hz;
};

static vector<GpuInfo> query_gpus() {
    vector<GpuInfo> gpus;

    D3DKMT_ENUMADAPTERS2 ea = {};
    vector<D3DKMT_ADAPTERINFO> adapters(16);
    ea.NumAdapters = 16;
    ea.pAdapters = adapters.data();
    if (D3DKMTEnumAdapters2(&ea) != 0)
        return gpus;

    bool nvapi_ok = NvAPI_Initialize() == NVAPI_OK;
    NvPhysicalGpuHandle nv_handles[NVAPI_MAX_PHYSICAL_GPUS] = {};
    NvU32 nv_count = 0;
    if (nvapi_ok)
        nvapi_ok = NvAPI_EnumPhysicalGPUs(nv_handles, &nv_count) == NVAPI_OK;

    for (UINT i = 0; i < ea.NumAdapters; i++) {
        const auto& a = adapters[i];

        D3DKMT_ADAPTERREGISTRYINFO reg = {};
        D3DKMT_QUERYADAPTERINFO qi = {};
        qi.hAdapter = a.hAdapter;
        qi.Type = KMTQAITYPE_ADAPTERREGISTRYINFO;
        qi.pPrivateDriverData = &reg;
        qi.PrivateDriverDataSize = sizeof(reg);
        D3DKMTQueryAdapterInfo(&qi);

        D3DKMT_ADAPTER_PERFDATA pd = {};
        pd.PhysicalAdapterIndex = 0;
        D3DKMT_QUERYADAPTERINFO qi2 = {};
        qi2.hAdapter = a.hAdapter;
        qi2.Type = KMTQAITYPE_ADAPTERPERFDATA;
        qi2.pPrivateDriverData = &pd;
        qi2.PrivateDriverDataSize = sizeof(pd);
        D3DKMTQueryAdapterInfo(&qi2);

        GpuInfo g = {};
        g.name = reg.AdapterString;
        g.temp_c = pd.Temperature / 10.0;
        g.mem_freq_hz = pd.MemoryFrequency;

        if (g.name.empty() || g.temp_c == 0.0)
            continue;

        if (nvapi_ok && i < nv_count) {
            NvU32 fan_rpm = 0;

            if (NvAPI_GPU_GetTachReading(nv_handles[i], &fan_rpm) == NVAPI_OK)
                g.fan_rpm = fan_rpm;

            NV_GPU_DYNAMIC_PSTATES_INFO_EX ps = {};
            ps.version = NV_GPU_DYNAMIC_PSTATES_INFO_EX_VER;
            if (NvAPI_GPU_GetDynamicPstatesInfoEx(nv_handles[i], &ps) == NVAPI_OK)
                g.gpu_usage_pct = ps.utilization[0].percentage;
        }

        gpus.push_back(g);

        D3DKMT_CLOSEADAPTER ca = { a.hAdapter };
        D3DKMTCloseAdapter(&ca);
    }

    if (nvapi_ok)
        NvAPI_Unload();

    return gpus;
}

static string json_str(const wstring& ws) {
    string out = "\"";

    for (const wchar_t wc : ws) {
        const char c = static_cast<char>(wc);

        if (c == '"')
            out += "\\\"";
        else if (c == '\\')
            out += "\\\\";
        else
            out += c;
    }

    return out + "\"";
}

int main() {
    const auto modules_dir = exe_dir() / "modules";

    if (!fs::exists(modules_dir)) {
        std::cout << "{\"error\":\"modules_dir_not_found\"}\n";
        return 1;
    }

    HANDLE h = nullptr;
    if (FAILED(pawnio_open(&h))) {
        std::cout << "{\"error\":\"pawnio_not_installed\"}\n";
        return 1;
    }

    double cpu_temp = -1;
    double cpu_freq_mhz = -1;

    if (cpu_is_intel()) {
        const auto msr_bin = load_file(modules_dir / "IntelMSR.bin");
        const bool cpu_ok  = !msr_bin.empty() && SUCCEEDED(pawnio_load(h, msr_bin.data(), static_cast<ULONG>(msr_bin.size())));

        if (cpu_ok) {
            ULONG64 tjmax_raw = 0, pkg_raw = 0, perf_raw = 0;

            if (read_msr(h, MSR_IA32_TEMPERATURE_TARGET, tjmax_raw) && read_msr(h, MSR_IA32_PACKAGE_THERM_STATUS, pkg_raw))
                cpu_temp = ((tjmax_raw >> 16) & 0xFF) - ((pkg_raw >> 16) & 0x7F);

            if (read_msr(h, MSR_IA32_PERF_STATUS, perf_raw))
                cpu_freq_mhz = ((perf_raw >> 8) & 0xFF) * 100.0;
        }
    }
    else if (cpu_is_amd()) {
        const auto f17_bin = load_file(modules_dir / "AMDFamily17.bin");
        const bool cpu_ok  = !f17_bin.empty() && SUCCEEDED(pawnio_load(h, f17_bin.data(), static_cast<ULONG>(f17_bin.size())));

        if (cpu_ok) {
            HANDLE pci_mutex = OpenMutexW(SYNCHRONIZE, FALSE, L"Global\\Access_PCI");
            if (pci_mutex) WaitForSingleObject(pci_mutex, INFINITE);

            ULONG64 tctl_raw = 0;
            if (read_smn(h, SMN_THM_TCON_CUR_TMP, tctl_raw))
                cpu_temp = ((tctl_raw >> 21) & 0x7FF) / 8.0;

            if (pci_mutex) { ReleaseMutex(pci_mutex); CloseHandle(pci_mutex); }

            ULONG64 pstate0 = 0;
            if (read_msr(h, MSR_AMD_PSTATE0, pstate0)) {
                const double fid = pstate0 & 0xFF;
                const double did = (pstate0 >> 8) & 0x3F;
                if (did > 0) cpu_freq_mhz = (fid * 200.0) / did;
            }
        }
    }

    pawnio_close(h);

    const auto gpus = query_gpus();

    std::cout << "{\n";
    std::cout << std::format("  \"cpu_temp_c\": {:.1f},\n", cpu_temp);
    std::cout << std::format("  \"cpu_freq_mhz\": {:.0f},\n", cpu_freq_mhz);
    std::cout << "  \"gpus\": [\n";
    for (size_t i = 0; i < gpus.size(); i++) {
        const auto& g = gpus[i];
        std::cout << "    {\n";
        std::cout << "      \"name\": " << json_str(g.name) << ",\n";
        std::cout << std::format("      \"temp_c\": {:.1f},\n", g.temp_c);
        std::cout << std::format("      \"fan_rpm\": {},\n", g.fan_rpm);
        std::cout << std::format("      \"gpu_usage_pct\": {:.1f},\n", g.gpu_usage_pct);
        std::cout << std::format("      \"mem_freq_mhz\": {:.0f}\n", g.mem_freq_hz / 1e6);
        std::cout << "    }" << (i + 1 < gpus.size() ? "," : "") << "\n";
    }
    std::cout << "  ]\n}\n";

    return 0;
}
