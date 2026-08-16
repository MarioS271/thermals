// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details Helper Functions
 * @authors MarioS271
 */

#pragma once

#include <vector>
#include <string>
#include <filesystem>

namespace fs = std::filesystem;

std::vector<uint8_t> load_file(const fs::path& path);
fs::path exe_dir();
std::string json_str(const std::wstring& ws);
