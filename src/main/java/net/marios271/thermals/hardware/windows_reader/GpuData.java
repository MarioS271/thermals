package net.marios271.thermals.hardware.windows_reader;

public record GpuData(
    String name,
    double tempC,
    double usagePct,
    long vramUsedMb,
    long vramTotalMb
) {}
