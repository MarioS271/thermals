package net.marios271.thermals.hardware.windows_reader;

import java.util.List;

public record SensorData(
    double cpuTempC,
    List<GpuData> gpus
) {}
