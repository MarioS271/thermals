// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details JSON helper function(s)
 * @authors MarioS271
 */

#pragma once

#include <vector>
#include "gpu.h"

void print_json(double cpu_temp_c, const std::vector<GpuInfo>& gpus);
