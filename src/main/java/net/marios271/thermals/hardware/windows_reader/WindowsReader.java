package net.marios271.thermals.hardware.windows_reader;

import com.google.gson.*;
import net.marios271.thermals.ui.PopupMessage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WindowsReader {
    private static final java.util.Set<String> notifiedErrors = new java.util.HashSet<>();

    private static Path findReaderExe() {
        Path prod = Path.of("app/pawnio_reader.exe");
        if (Files.exists(prod)) return prod;

        Path dev = Path.of("resources/windows/pawnio_reader.exe");
        if (Files.exists(dev)) return dev;

        return null;
    }

    private static String readRawData(Path readerPath) {
        try {
            Process proc = new ProcessBuilder(readerPath.toAbsolutePath().toString()).start();
            String stdout = new String(proc.getInputStream().readAllBytes());
            proc.waitFor();
            return stdout;
        } catch (Exception e) {
            return null;
        }
    }

    private static SensorData parseReaderJson(String raw) {
        try {
            JsonObject root = JsonParser.parseString(raw).getAsJsonObject();
            if (root.has("error")) {
                String error = root.get("error").getAsString();
                if (notifiedErrors.add(error)) {
                    PopupMessage.createWarnPopup("PawnIO-Reader returned an error\n\n" + error);
                }
                System.err.println("PawnIO-Reader returned an error: " + error);
                return null;
            }

            double cpuTemp = root.get("cpu_temp_c").getAsDouble();

            List<GpuData> gpus = new ArrayList<>();
            for (JsonElement element : root.getAsJsonArray("gpus")) {
                JsonObject gpu = element.getAsJsonObject();

                gpus.add(new GpuData(
                    gpu.get("name").getAsString(),
                    gpu.get("temp_c").getAsDouble(),
                    gpu.get("gpu_usage_pct").getAsDouble(),
                    gpu.get("vram_used_mb").getAsLong(),
                    gpu.get("vram_total_mb").getAsLong()
                ));
            }
            return new SensorData(cpuTemp, gpus);
        } catch (Exception e) {
            return null;
        }
    }

    public static SensorData requestData() {
        Path exe = findReaderExe();
        if (exe == null) return null;

        String raw = readRawData(exe);
        if (raw == null) return null;

        return parseReaderJson(raw);
    }
}
