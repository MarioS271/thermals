// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details CPU Functions
 * @authors MarioS271
 */

#pragma once

#include <filesystem>

namespace fs = std::filesystem;

double read_cpu_temp(const fs::path& modules_dir);
