package net.marios271.thermals.hardware;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

public class HwManager {
    SystemInfo sysinfo;
    HardwareAbstractionLayer hal;
    Sensors sensors;

    Cpu cpu;

    Thread pollingThread;

    public void init() {
        sysinfo = new SystemInfo();
        hal = sysinfo.getHardware();
        sensors = hal.getSensors();

        cpu = new Cpu().init(this);

        pollingThread = new Thread(() -> {
            while (true) {
                pollValues();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException _) {}
            }
        });
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    public void pollValues() {
        cpu.pollValues();
    }

    public SystemInfo sysinfo() {
        return sysinfo;
    }
    public HardwareAbstractionLayer hal() {
        return hal;
    }

    public Cpu cpu() {
        return cpu;
    }
}
