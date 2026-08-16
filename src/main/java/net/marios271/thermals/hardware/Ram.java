package net.marios271.thermals.hardware;

import oshi.SystemInfo;

public class Ram {
    private SystemInfo sysInfo;

    private int capacityGb;
    private int speedMhz;
    private String type;

    private volatile double usedGb;
    private volatile double cachedGb;
    private volatile double freeGb;

    public Ram init(HwManager _hwManager) {
        sysInfo = _hwManager.sysInfo();

        var mem = sysInfo.getHardware().getMemory();
        var sticks = mem.getPhysicalMemory();

        capacityGb = (int)Math.round(mem.getTotal() / 1_073_741_824.0);

        if (!sticks.isEmpty()) {
            type = sticks.getFirst().getMemoryType();
            speedMhz = (int)(sticks.getFirst().getClockSpeed() / 1_000_000L);
        }

        update();

        return this;
    }

    public void update() {
        var mem = sysInfo.getHardware().getMemory();

        long total = mem.getTotal();
        long available = mem.getAvailable();
        long used = total - available;

        usedGb = used / 1_073_741_824.0;
        freeGb = available / 1_073_741_824.0;
        cachedGb = 0;
    }

    public int getCapacityGb() {
        return capacityGb;
    }
    public int getSpeedMhz() {
        return speedMhz;
    }
    public String getType() {
        return type;
    }

    public double getUsedGb() {
        return usedGb;
    }
    public double getCachedGb() {
        return cachedGb;
    }
    public double getFreeGb() {
        return freeGb;
    }
}
