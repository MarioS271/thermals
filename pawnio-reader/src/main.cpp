// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details A shim between PawnIO and the main java app which interacts with PawnIO and hands data to java via stdout
 * @authors MarioS271
 */

#include <iostream>
#include <filesystem>
#include "util.h"
#include "cpu.h"
#include "gpu.h"
#include "json.h"

namespace fs = std::filesystem;

int main() {
    const auto modules_dir = exe_dir() / "modules";

    if (!fs::exists(modules_dir)) {
        std::cout << "{\"error\":\"modules_dir_not_found\"}\n";
        return 1;
    }

    const double cpu_temp = read_cpu_temp(modules_dir);
    const auto gpus = query_gpus();

    print_json(cpu_temp, gpus);
    return 0;
}
