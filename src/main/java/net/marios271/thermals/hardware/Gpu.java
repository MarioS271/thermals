package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.windows_reader.GpuData;
import net.marios271.thermals.hardware.windows_reader.SensorData;

public class Gpu {
    private String gpuName;
    private int gpuIndex;

    private volatile double tempC;
    private volatile double usagePct;
    private volatile long vramUsedMb;
    private volatile long vramTotalMb;

    private Process nvidiaSmiProcess;
    private boolean isNvidia = false;

    private static String readNvidiaSmi(String query, int index) {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{
                "nvidia-smi",
                query,
                "--format=csv,noheader,nounits",
                "-i", String.valueOf(index)
            });

            String result = new String(proc.getInputStream().readAllBytes()).trim();
            return result.isEmpty() ? null : result;
        } catch (Exception e) {
            return null;
        }
    }

    public Gpu init(HwManager _hwManager, SensorData readerData, int _gpuIndex) {
        gpuIndex = _gpuIndex;

        if (Platform.isWindows()) {
            gpuName = readerData.gpus().get(gpuIndex).name();
        }
        else if (Platform.isLinux()) {
            gpuName = readNvidiaSmi("--query-gpu=name", _gpuIndex);
            if (gpuName != null) {
                isNvidia = true;
                String vram = readNvidiaSmi("--query-gpu=memory.total", _gpuIndex);
                vramTotalMb = vram != null ? Long.parseLong(vram.replace(" MiB", "").trim()) : 0;
            }
        }

        return this;
    }

    public void update(SensorData readerData) {
        if (Platform.isWindows()) {
            GpuData gpuData = readerData.gpus().get(gpuIndex);

            tempC = gpuData.tempC();
            usagePct = gpuData.usagePct();
            vramUsedMb = gpuData.vramUsedMb();
            vramTotalMb = gpuData.vramTotalMb();
        }
        else if (Platform.isLinux()) {
            String csv = readNvidiaSmi(
                "--query-gpu=temperature.gpu,utilization.gpu,memory.used",
                gpuIndex
            );

            if (csv != null) {
                String[] parts = csv.split(",");
                if (parts.length == 3) {
                    tempC = Double.parseDouble(parts[0].trim());
                    usagePct = Double.parseDouble(parts[1].replace(" %", "").trim());
                    vramUsedMb = Long.parseLong(parts[2].replace(" MiB", "").trim());
                }
            }
        }
    }

    public String getGpuName() {
        return gpuName;
    }
    public int getGpuIndex() {
        return gpuIndex;
    }
    public double getTempC() {
        return tempC;
    }
    public double getUsagePct() {
        return usagePct;
    }
    public double getVramUsedGbOneTenth() {
        return Math.round(vramUsedMb / 102.4) / 10.0;
    }
    public int getVramTotalGbRounded() {
        return Math.toIntExact(Math.round(vramTotalMb / 1024.0));
    }
}
