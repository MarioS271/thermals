package net.marios271.thermals.hardware;

import oshi.hardware.CentralProcessor;

public class Cpu {
    HwManager manager;
    CentralProcessor cpu;

    String processorName;
    volatile int usagePct;

    long[] prevUsageTicks;

    public Cpu init(HwManager _manager) {
        manager = _manager;
        cpu = manager.hal().getProcessor();

        processorName = cpu.getProcessorIdentifier().getName();

        prevUsageTicks = cpu.getSystemCpuLoadTicks();

        return this;
    }

    public void pollValues() {
        usagePct = (int)(cpu.getSystemCpuLoadBetweenTicks(prevUsageTicks) * 100);

        prevUsageTicks = cpu.getSystemCpuLoadTicks();
    }

    public String getCpuName() {
        return processorName;
    }

    public int getCpuUsagePct() {
        return usagePct;
    }
}
