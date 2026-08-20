package net.marios271.thermals.hardware;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.windows_reader.SensorData;
import oshi.hardware.CentralProcessor;

import java.io.File;
import java.nio.file.Files;

public class Cpu {
    private HwManager manager;
    private CentralProcessor cpu;

    private String processorName;
    private int logicalCores;

    private volatile int usagePct;
    private volatile double coreUsage;
    private volatile double clockSpeedGhz;

    private static final double TEMP_SMOOTHING = 0.4;
    private volatile double tempC;
    private double rawTempC;

    private long[] prevUsageTicks;

    public Cpu init() {
        cpu = HwManager.hal().getProcessor();

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
            rawTempC = readerData.cpuTempC();
        }
        else if (Platform.isLinux()) {
            try {
                File hwmonDir = new File("/sys/class/hwmon");
                for (File hwmon : hwmonDir.listFiles()) {
                    File nameFile = new File(hwmon, "name");
                    if (!nameFile.exists())
                        continue;

                    String name = Files.readString(nameFile.toPath()).trim();
                    if (!name.equals("coretemp") && !name.equals("k10temp"))
                        continue;

                    boolean foundTdie = false;
                    for (File f : hwmon.listFiles()) {
                        if (!f.getName().endsWith("_label"))
                            continue;

                        String label = Files.readString(f.toPath()).trim();
                        if (label.equals("Tdie") || label.startsWith("Package id")) {
                            String inputName = f.getName().replace("_label", "_input");
                            String raw = Files.readString(new File(hwmon, inputName).toPath()).trim();
                            rawTempC = Long.parseLong(raw) / 1000.0;
                            foundTdie = true;
                            break;
                        }
                    }

                    if (!foundTdie && name.equals("k10temp")) {
                        for (File f : hwmon.listFiles()) {
                            if (!f.getName().endsWith("_label"))
                                continue;
                            if (!Files.readString(f.toPath()).trim().equals("Tctl"))
                                continue;

                            String inputName = f.getName().replace("_label", "_input");
                            String raw = Files.readString(new File(hwmon, inputName).toPath()).trim();

                            rawTempC = Long.parseLong(raw) / 1000.0;

                            break;
                        }
                    }
                    break;
                }
            } catch (Exception _) {}
        }

        tempC = tempC + TEMP_SMOOTHING * (rawTempC - tempC);
        prevUsageTicks = cpu.getSystemCpuLoadTicks();
    }

    public String getCpuName() { return processorName; }
    public int getLogicalCores() { return logicalCores; }
    public int getCpuUsagePct() { return usagePct; }
    public double getCpuCoreUsage() { return coreUsage; }
    public double getClockSpeedGhz() { return clockSpeedGhz; }
    public double getTempC() { return tempC; }
    public String getTempCFormatted() { return tempC < 0 ? "N/A" : Helpers.doubleAsSinglePrecisionString(tempC); }
}
