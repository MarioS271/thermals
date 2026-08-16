// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details GPU Functions
 * @authors MarioS271
 */

#pragma once

#include <string>
#include <vector>
#include <windows.h>

struct GpuInfo {
    std::wstring name;
    double temp_c;
    double gpu_usage_pct;
    ULONG vram_used_mb;
    ULONG vram_total_mb;
};

std::vector<GpuInfo> query_gpus();
