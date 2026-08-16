// SPDX-License-Identifier: GPL-3.0-only
/**
 * @details JSON helper function(s)
 * @authors MarioS271
 */

#include "json.h"

#include "util.h"
#include <iostream>
#include <format>

std::string json_str(const std::wstring& ws) {
    std::string out = "\"";
    for (const wchar_t wc : ws) {
        const char c = static_cast<char>(wc);
        if (c == '"')       out += "\\\"";
        else if (c == '\\') out += "\\\\";
        else                out += c;
    }
    return out + "\"";
}

void print_json(double cpu_temp_c, const std::vector<GpuInfo>& gpus) {
    std::cout << "{\n";
    std::cout << std::format("  \"cpu_temp_c\": {:.1f},\n", cpu_temp_c);
    std::cout << "  \"gpus\": [\n";
    for (size_t i = 0; i < gpus.size(); i++) {
        const auto& g = gpus[i];
        std::cout << "    {\n";
        std::cout << "      \"name\": " << json_str(g.name) << ",\n";
        std::cout << std::format("      \"temp_c\": {:.1f},\n", g.temp_c);
        std::cout << std::format("      \"gpu_usage_pct\": {:.1f},\n", g.gpu_usage_pct);
        std::cout << std::format("      \"vram_used_mb\": {},\n", g.vram_used_mb);
        std::cout << std::format("      \"vram_total_mb\": {}\n", g.vram_total_mb);
        std::cout << "    }" << (i + 1 < gpus.size() ? "," : "") << "\n";
    }
    std::cout << "  ]\n}\n";
}
