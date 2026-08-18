package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Ram {
    private SystemInfo sysInfo;
    private GlobalMemory mem;

    private int capacityGb;
    private int speedMhz;
    private String type;

    private volatile double usedGb;
    private volatile double cachedGb;
    private volatile double freeGb;

    public Ram init(HwManager _hwManager) {
        sysInfo = _hwManager.sysInfo();

        mem = sysInfo.getHardware().getMemory();
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
        long total = mem.getTotal();
        long available = mem.getAvailable();
        long used = total - available;

        usedGb = used / 1_073_741_824.0;
        freeGb = available / 1_073_741_824.0;
        cachedGb = getCachedMem();
    }

    private double getCachedMem() {
        if (!Platform.isLinux()) return 0;

        try (var lines = Files.lines(Path.of("/proc/meminfo"))) {
            var memInfo = lines
                .filter(l -> l.startsWith("Cached:") || l.startsWith("Buffers:"))
                .mapToLong(l -> Long.parseLong(l.split("\\s+")[1]))
                .sum();
            return memInfo / 1_048_576.0;
        } catch (IOException e) {
            return 0;
        }
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
