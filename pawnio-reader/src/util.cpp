// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details Helper Functions
 * @authors MarioS271
 */

#include "util.h"

#include <fstream>
#include <windows.h>

std::vector<uint8_t> load_file(const fs::path& path) {
    std::ifstream f(path, std::ios::binary);
    return {std::istreambuf_iterator(f), {}};
}

fs::path exe_dir() {
    wchar_t buf[MAX_PATH];
    GetModuleFileNameW(nullptr, buf, MAX_PATH);
    return fs::path(buf).parent_path();
}
