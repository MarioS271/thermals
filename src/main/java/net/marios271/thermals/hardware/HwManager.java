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
    private SystemInfo sysInfo;
    private HardwareAbstractionLayer hal;

    private Cpu cpu;
    private Ram ram;
    private ArrayList<Gpu> gpus = new ArrayList<>();
    private ArrayList<Disk> disks = new ArrayList<>();
    private ArrayList<Net> nets = new ArrayList<>();

    private Thread pollingThread;

    private static final Set<String> IGNORED_FS_TYPES = Set.of(
        "tmpfs", "devtmpfs", "sysfs", "proc", "cgroup",
        "cgroup2", "pstore", "none", "overlay", "squashfs"
    );

    private final List<HwUpdateListener> listeners = new ArrayList<>();

    public void init() {
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

        cpu = new Cpu().init(this);
        ram = new Ram().init(this);
        for (int i = 0; i < numGpus; ++i)
            gpus.add(new Gpu().init(this, readerData, i));
        for (OSFileStore fileStore : sysInfo.getOperatingSystem().getFileSystem().getFileStores()) {
            if (Platform.isLinux() && IGNORED_FS_TYPES.contains(fileStore.getType()))
                continue;
            disks.add(new Disk().init(this, fileStore));
        }
        for (NetworkIF nif : hal.getNetworkIFs()) {
            if (Net.isValid(nif)) {
                nets.add(new Net().init(this, nif));
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

    public void update() {
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

        listeners.forEach(HwUpdateListener::onHwUpdate);
    }

    public void addUpdateListener(HwUpdateListener listener) {
        listeners.add(listener);
        System.out.println("added update listener: " + listener);
    }

    public SystemInfo sysInfo() {
        return sysInfo;
    }
    public HardwareAbstractionLayer hal() {
        return hal;
    }

    public Cpu cpu() {
        return cpu;
    }
    public Ram ram() {
        return ram;
    }
    public ArrayList<Gpu> gpus() {
        return gpus;
    }
    public ArrayList<Disk> disks() {
        return disks;
    }
    public ArrayList<Net> nets() {
        return nets;
    }
}
