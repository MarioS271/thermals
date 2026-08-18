// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details CPU Functions
 * @authors MarioS271
 */

#include "cpu.h"

#include <intrin.h>
#include <windows.h>
#include "PawnIOLib.h"
#include "util.h"

constexpr ULONG64 MSR_IA32_PACKAGE_THERM_STATUS = 0x1b1;
constexpr ULONG64 MSR_IA32_TEMPERATURE_TARGET   = 0x1a2;
constexpr ULONG64 SMN_THM_TCON_CUR_TMP          = 0x00059800;

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

static bool pawnio_read_msr(HANDLE h, ULONG64 msr, ULONG64& out) {
    const ULONG64 in_buf[1] = { msr };
    ULONG64 out_buf[1] = {};
    SIZE_T returned = 0;

    if (FAILED(pawnio_execute(h, "ioctl_read_msr", in_buf, 1, out_buf, 1, &returned)))
        return false;

    out = out_buf[0];
    return true;
}

static bool pawnio_read_smn(HANDLE h, ULONG64 offset, ULONG64& out) {
    const ULONG64 in_buf[1] = { offset };
    ULONG64 out_buf[1] = {};
    SIZE_T returned = 0;

    if (FAILED(pawnio_execute(h, "ioctl_read_smn", in_buf, 1, out_buf, 1, &returned)))
        return false;

    out = out_buf[0];
    return true;
}

static double read_intel_temp(HANDLE h, const fs::path& modules_dir) {
    const auto bin = load_file(modules_dir / "IntelMSR.bin");
    if (bin.empty() || FAILED(pawnio_load(h, bin.data(), static_cast<ULONG>(bin.size()))))
        return -1;

    ULONG64 tjmax_raw = 0, pkg_raw = 0;
    if (pawnio_read_msr(h, MSR_IA32_TEMPERATURE_TARGET, tjmax_raw) &&
        pawnio_read_msr(h, MSR_IA32_PACKAGE_THERM_STATUS, pkg_raw))
        return ((tjmax_raw >> 16) & 0xFF) - ((pkg_raw >> 16) & 0x7F);

    return -1;
}

static double read_amd_temp(HANDLE h, const fs::path& modules_dir) {
    const auto bin = load_file(modules_dir / "AMDFamily17.bin");
    if (bin.empty() || FAILED(pawnio_load(h, bin.data(), static_cast<ULONG>(bin.size()))))
        return -1;

    HANDLE pci_mutex = OpenMutexW(SYNCHRONIZE, FALSE, L"Global\\Access_PCI");
    if (pci_mutex) WaitForSingleObject(pci_mutex, INFINITE);

    double temp = -1;
    ULONG64 tctl_raw = 0;
    if (pawnio_read_smn(h, SMN_THM_TCON_CUR_TMP, tctl_raw)) {
        temp = ((tctl_raw >> 21) & 0x7FF) / 8.0;
        if (tctl_raw & (1u << 19))
            temp -= 49.0;
    }

    if (pci_mutex) { ReleaseMutex(pci_mutex); CloseHandle(pci_mutex); }
    return temp;
}

double read_cpu_temp(const fs::path& modules_dir) {
    HANDLE h = nullptr;
    if (FAILED(pawnio_open(&h))) return -1;

    double temp = -1;
    if (cpu_is_intel()) temp = read_intel_temp(h, modules_dir);
    else if (cpu_is_amd()) temp = read_amd_temp(h, modules_dir);

    pawnio_close(h);
    return temp;
}
