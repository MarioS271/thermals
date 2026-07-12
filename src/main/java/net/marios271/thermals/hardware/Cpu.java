package net.marios271.thermals.hardware;

import net.marios271.thermals.Helpers;
import oshi.hardware.CentralProcessor;

public class Cpu {
    HwManager manager;
    CentralProcessor cpu;

    String processorName;
    int logicalCores;

    volatile int usagePct;
    volatile double coreUsage;
    volatile double clockSpeedGhz;

    long[] prevUsageTicks;

    public Cpu init(HwManager _manager) {
        manager = _manager;
        cpu = manager.hal().getProcessor();

        processorName = cpu.getProcessorIdentifier().getName();
        logicalCores = cpu.getLogicalProcessorCount();

        prevUsageTicks = cpu.getSystemCpuLoadTicks();

        return this;
    }

    public void pollValues() {
        double loadFraction = cpu.getSystemCpuLoadBetweenTicks(prevUsageTicks);
        usagePct = (int)(loadFraction * 100);
        coreUsage = loadFraction * logicalCores;
        clockSpeedGhz = Helpers.getAvgOfLongArray(cpu.getCurrentFreq()) / 1_000_000_000.0;

        prevUsageTicks = cpu.getSystemCpuLoadTicks();
    }

    public String getCpuName() {
        return processorName;
    }
    public int getLogicalCores() { return logicalCores; }

    public int getCpuUsagePct() {
        return usagePct;
    }
    public double getCpuCoreUsage() {
        return coreUsage;
    }
    public double getClockSpeedGhz() { return clockSpeedGhz; }
}
