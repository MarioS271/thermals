package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.windows_reader.GpuData;
import net.marios271.thermals.hardware.windows_reader.SensorData;

import java.io.File;
import java.nio.file.Files;

public class Gpu {
    private String gpuName;
    private int gpuIndex;

    private volatile double tempC;
    private volatile double usagePct;
    private volatile long vramUsedMb;
    private volatile long vramTotalMb;

    private boolean isNvidia = false;
    private String amdHwmonPath = null;

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

    public Gpu init(SensorData readerData, int _gpuIndex) {
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
            else {
                try {
                    File hwmonDir = new File("/sys/class/hwmon");
                    for (File hwmon : hwmonDir.listFiles()) {
                        File nameFile = new File(hwmon, "name");
                        if (!nameFile.exists()) continue;
                        if (!Files.readString(nameFile.toPath()).trim().equals("amdgpu")) continue;

                        isNvidia = false;
                        amdHwmonPath = hwmon.getPath();

                        File drmLink = new File(hwmon, "device/drm");
                        if (drmLink.exists()) {
                            gpuName = "AMD GPU";

                            for (File card : drmLink.listFiles()) {
                                if (card.getName().startsWith("card")) {
                                    File deviceName = new File(card, "device/product_name");

                                    if (deviceName.exists())
                                        gpuName = Files.readString(deviceName.toPath()).trim();

                                    break;
                                }
                            }
                        }

                        File vramFile = new File(hwmon, "device/mem_info_vram_total");
                        if (vramFile.exists())
                            vramTotalMb = Long.parseLong(Files.readString(vramFile.toPath()).trim()) / 1_048_576L;

                        break;
                    }
                } catch (Exception _) {}
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
            if (isNvidia) {
                String csv = readNvidiaSmi(
                    "--query-gpu=temperature.gpu,utilization.gpu,memory.used",
                    gpuIndex
                );
                if (csv != null) {
                    String[] parts = csv.split(",");
                    if (parts.length == 3) {
                        tempC    = Double.parseDouble(parts[0].trim());
                        usagePct = Double.parseDouble(parts[1].trim());
                        vramUsedMb = Long.parseLong(parts[2].trim());
                    }
                }
            } else if (amdHwmonPath != null) {
                try {
                    File tempFile = new File(amdHwmonPath + "/temp1_input");
                    if (tempFile.exists())
                        tempC = Long.parseLong(Files.readString(tempFile.toPath()).trim()) / 1000.0;

                    File usageFile = new File(amdHwmonPath + "/device/gpu_busy_percent");
                    if (usageFile.exists())
                        usagePct = Long.parseLong(Files.readString(usageFile.toPath()).trim());

                    File vramUsedFile = new File(amdHwmonPath + "/device/mem_info_vram_used");
                    if (vramUsedFile.exists())
                        vramUsedMb = Long.parseLong(Files.readString(vramUsedFile.toPath()).trim()) / 1_048_576L;
                } catch (Exception _) {}
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
