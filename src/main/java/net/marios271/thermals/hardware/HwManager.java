package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import net.marios271.thermals.Thermals;
import net.marios271.thermals.hardware.windows_reader.SensorData;
import net.marios271.thermals.hardware.windows_reader.WindowsReader;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HwManager {
    private static SystemInfo sysInfo;
    private static HardwareAbstractionLayer hal;

    private static Cpu cpu;
    private static Ram ram;
    private static ArrayList<Gpu> gpus = new ArrayList<>();
    private static ArrayList<Disk> disks = new ArrayList<>();
    private static ArrayList<Net> nets = new ArrayList<>();

    private static Thread pollingThread;

    private static final Set<String> IGNORED_FS_TYPES = Set.of(
        "tmpfs", "devtmpfs", "sysfs", "proc", "cgroup",
        "cgroup2", "pstore", "none", "overlay", "squashfs",
        "fuse", "fuseblk", "fuse.squashfuse", "squashfuse"
    );

    private static final List<Runnable> listeners = new ArrayList<>();

    public static void init() {
        sysInfo = new SystemInfo();
        hal = sysInfo.getHardware();

        int numGpus = 0;
        SensorData readerData = null;
        if (Platform.isWindows()) {
            readerData = WindowsReader.requestData();
            if (readerData != null)
                numGpus = readerData.gpus().size();
        }
        else if (Platform.isLinux()) {
            try {
                Process proc = Runtime.getRuntime().exec(new String[]{
                    "nvidia-smi", "--query-gpu=name", "--format=csv,noheader"
                });

                String output = new String(proc.getInputStream().readAllBytes()).trim();
                if (!output.isEmpty())
                    numGpus = output.split("\n").length;
            } catch (Exception _) {}
        }

        cpu = new Cpu().init();
        ram = new Ram().init();
        for (int i = 0; i < numGpus; ++i)
            gpus.add(new Gpu().init(readerData, i));
        for (OSFileStore fileStore : sysInfo.getOperatingSystem().getFileSystem().getFileStores()) {
            if (Platform.isLinux() && IGNORED_FS_TYPES.contains(fileStore.getType()))
                continue;
            disks.add(new Disk().init(fileStore));
        }
        for (NetworkIF nif : hal.getNetworkIFs()) {
            if (Net.isValid(nif)) {
                nets.add(new Net().init(nif));
            }
        }

        pollingThread = new Thread(() -> {
            while (true) {
                update();
                try {
                    Thread.sleep(Thermals.DATA_UPDATE_INTERVAL_MS);
                } catch (InterruptedException _) {}
            }
        });
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    public static void update() {
        SensorData readerData = null;
        if (Platform.isWindows())
            readerData = WindowsReader.requestData();

        cpu.update(readerData);
        ram.update();
        for (Gpu gpu : gpus)
            gpu.update(readerData);
        for (Disk disk : disks)
            disk.update();
        for (Net net : nets)
            net.update();

        listeners.forEach(Runnable::run);
    }

    public static void addUpdateListener(Runnable listener) {
        listeners.add(listener);
        System.out.println("added update listener: " + listener);
    }

    public static SystemInfo sysInfo() { return sysInfo; }
    public static HardwareAbstractionLayer hal() { return hal; }

    public static Cpu cpu() { return cpu; }
    public static Ram ram() { return ram; }
    public static ArrayList<Gpu> gpus() { return gpus; }
    public static ArrayList<Disk> disks() { return disks; }
    public static ArrayList<Net> nets() { return nets; }
}
