package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.windows_reader.GpuData;
import net.marios271.thermals.hardware.windows_reader.SensorData;

public class Gpu {
    HwManager hwManager;

    String gpuName;
    int gpuIndex;

    volatile double tempC;
    volatile double usagePct;
    volatile long vramUsedMb;
    volatile long vramTotalMb;

    public Gpu init(HwManager _hwManager, SensorData readerData, int _gpuIndex) {
        hwManager = _hwManager;
        gpuIndex = _gpuIndex;

        if (Platform.isWindows()) {
            gpuName = readerData.gpus().get(gpuIndex).name();
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
