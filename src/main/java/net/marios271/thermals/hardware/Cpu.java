package net.marios271.thermals.hardware;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.windows_reader.SensorData;
import oshi.hardware.CentralProcessor;

public class Cpu {
    private HwManager manager;
    private CentralProcessor cpu;

    private String processorName;
    private int logicalCores;

    private volatile int usagePct;
    private volatile double coreUsage;
    private volatile double clockSpeedGhz;
    private volatile double tempC;

    private long[] prevUsageTicks;

    public Cpu init(HwManager _manager) {
        manager = _manager;
        cpu = manager.hal().getProcessor();

        processorName = cpu.getProcessorIdentifier().getName();
        logicalCores = cpu.getLogicalProcessorCount();

        prevUsageTicks = cpu.getSystemCpuLoadTicks();

        return this;
    }

    public void update(SensorData readerData) {
        double loadFraction = cpu.getSystemCpuLoadBetweenTicks(prevUsageTicks);
        usagePct = (int)(loadFraction * 100);
        coreUsage = loadFraction * logicalCores;
        clockSpeedGhz = Helpers.getAvgOfLongArray(cpu.getCurrentFreq()) / 1_000_000_000.0;
        if (Platform.isWindows() && readerData != null) {
            tempC = readerData.cpuTempC();
        }

        prevUsageTicks = cpu.getSystemCpuLoadTicks();
    }

    public String getCpuName() {
        return processorName;
    }
    public int getLogicalCores() {
        return logicalCores;
    }

    public int getCpuUsagePct() {
        return usagePct;
    }
    public double getCpuCoreUsage() {
        return coreUsage;
    }
    public double getClockSpeedGhz() {
        return clockSpeedGhz;
    }
    public double getTempC() {
        return tempC;
    }
}
