// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details GPU Functions
 * @authors MarioS271
 */

#include "gpu.h"

#include <d3dkmthk.h>
#include "nvapi.h"

std::vector<GpuInfo> query_gpus() {
    std::vector<GpuInfo> gpus;

    D3DKMT_ENUMADAPTERS2 ea = {};
    std::vector<D3DKMT_ADAPTERINFO> adapters(16);
    ea.NumAdapters = 16;
    ea.pAdapters   = adapters.data();
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
        qi.hAdapter              = a.hAdapter;
        qi.Type                  = KMTQAITYPE_ADAPTERREGISTRYINFO;
        qi.pPrivateDriverData    = &reg;
        qi.PrivateDriverDataSize = sizeof(reg);
        D3DKMTQueryAdapterInfo(&qi);

        D3DKMT_ADAPTER_PERFDATA pd = {};
        pd.PhysicalAdapterIndex = 0;
        D3DKMT_QUERYADAPTERINFO qi2 = {};
        qi2.hAdapter             = a.hAdapter;
        qi2.Type                 = KMTQAITYPE_ADAPTERPERFDATA;
        qi2.pPrivateDriverData   = &pd;
        qi2.PrivateDriverDataSize = sizeof(pd);
        D3DKMTQueryAdapterInfo(&qi2);

        GpuInfo g = {};
        g.name   = reg.AdapterString;
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
                g.vram_used_mb  = (mem.dedicatedVideoMemory - mem.curAvailableDedicatedVideoMemory) / 1024;
                g.vram_total_mb = mem.dedicatedVideoMemory / 1024;
            }
        }

        gpus.push_back(g);

        D3DKMT_CLOSEADAPTER ca = { a.hAdapter };
        D3DKMTCloseAdapter(&ca);
    }

    if (nvapi_ok) NvAPI_Unload();
    return gpus;
}
