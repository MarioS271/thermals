package net.marios271.thermals.hardware;

import net.marios271.thermals.Platform;
import net.marios271.thermals.Thermals;
import net.marios271.thermals.hardware.windows_reader.SensorData;
import net.marios271.thermals.hardware.windows_reader.WindowsReader;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

import java.util.ArrayList;
import java.util.List;

public class HwManager {
    private SystemInfo sysInfo;
    private HardwareAbstractionLayer hal;

    private Cpu cpu;
    private Ram ram;
    private ArrayList<Gpu> gpus = new ArrayList<>();

    private Thread pollingThread;

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

        cpu = new Cpu().init(this);
        ram = new Ram().init(this);
        for (int i = 0; i < numGpus; ++i) {
            gpus.add(new Gpu().init(this, readerData, i));
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
}
