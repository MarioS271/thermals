// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details GPU Functions
 * @authors MarioS271
 */

#include "gpu.h"
#include <d3dkmthk.h>
#include <map>
#include <string>
#include "nvapi.h"
#include "ADLXHelper.h"
#include "ISystem.h"
#include "IPerformanceMonitoring.h"

struct AdlxGpuData {
    double usage_pct  = 0;
    int vram_used_mb  = 0;
    int vram_total_mb = 0;
};

static std::map<std::wstring, AdlxGpuData> query_adlx() {
    std::map<std::wstring, AdlxGpuData> result;

    ADLXHelper helper;
    if (ADLX_FAILED(helper.Initialize()))
        return result;

    adlx::IADLXSystem* sys = helper.GetSystemServices();
    if (!sys) return result;

    adlx::IADLXPerformanceMonitoringServicesPtr perf;
    if (ADLX_FAILED(sys->GetPerformanceMonitoringServices(&perf)))
        return result;

    adlx::IADLXGPUListPtr gpu_list;
    if (ADLX_FAILED(sys->GetGPUs(&gpu_list)))
        return result;

    for (adlx_uint i = gpu_list->Begin(); i != gpu_list->End(); i++) {
        adlx::IADLXGPUPtr gpu;
        if (ADLX_FAILED(gpu_list->At(i, &gpu))) continue;

        const char* name_c = nullptr;
        gpu->Name(&name_c);
        if (!name_c) continue;

        adlx::IADLXGPUMetricsPtr metrics;
        if (ADLX_FAILED(perf->GetCurrentGPUMetrics(gpu, &metrics))) continue;

        AdlxGpuData data;

        adlx_double usage = 0;
        if (ADLX_SUCCEEDED(metrics->GPUUsage(&usage)))
            data.usage_pct = usage;

        adlx_int vram_used = 0;
        if (ADLX_SUCCEEDED(metrics->GPUVRAM(&vram_used)))
            data.vram_used_mb = vram_used;

        adlx_uint vram_total = 0;
        gpu->TotalVRAM(&vram_total);
        data.vram_total_mb = static_cast<int>(vram_total);

        result[std::wstring(name_c, name_c + strlen(name_c))] = data;
    }

    return result;
}

std::vector<GpuInfo> query_gpus() {
    std::vector<GpuInfo> gpus;

    D3DKMT_ENUMADAPTERS2 ea = {};
    std::vector<D3DKMT_ADAPTERINFO> adapters(16);
    ea.NumAdapters = 16;
    ea.pAdapters = adapters.data();
    if (D3DKMTEnumAdapters2(&ea) != 0)
        return gpus;

    bool nvapi_ok = NvAPI_Initialize() == NVAPI_OK;
    NvPhysicalGpuHandle nv_handles[NVAPI_MAX_PHYSICAL_GPUS] = {};
    NvU32 nv_count = 0;
    if (nvapi_ok)
        nvapi_ok = NvAPI_EnumPhysicalGPUs(nv_handles, &nv_count) == NVAPI_OK;

    const auto adlx_data = query_adlx();

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

        if (g.name.empty() || g.temp_c == 0.0) {
            D3DKMT_CLOSEADAPTER ca = { a.hAdapter };
            D3DKMTCloseAdapter(&ca);
            continue;
        }

        if (nvapi_ok && i < nv_count) {
            NV_GPU_DYNAMIC_PSTATES_INFO_EX ps = {};
            ps.version = NV_GPU_DYNAMIC_PSTATES_INFO_EX_VER;
            if (NvAPI_GPU_GetDynamicPstatesInfoEx(nv_handles[i], &ps) == NVAPI_OK)
                g.gpu_usage_pct = ps.utilization[0].percentage;

            NV_DISPLAY_DRIVER_MEMORY_INFO mem = {};
            mem.version = NV_DISPLAY_DRIVER_MEMORY_INFO_VER;
            if (NvAPI_GPU_GetMemoryInfo(nv_handles[i], &mem) == NVAPI_OK) {
                g.vram_used_mb = (mem.dedicatedVideoMemory - mem.curAvailableDedicatedVideoMemory) / 1024;
                g.vram_total_mb = mem.dedicatedVideoMemory / 1024;
            }
        } else {
            const auto it = adlx_data.find(g.name);
            if (it != adlx_data.end()) {
                g.gpu_usage_pct = it->second.usage_pct;
                g.vram_used_mb = it->second.vram_used_mb;
                g.vram_total_mb = it->second.vram_total_mb;
            }
        }

        gpus.push_back(g);

        D3DKMT_CLOSEADAPTER ca = { a.hAdapter };
        D3DKMTCloseAdapter(&ca);
    }

    if (nvapi_ok) NvAPI_Unload();
    return gpus;
}
