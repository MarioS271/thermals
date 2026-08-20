package net.marios271.thermals.hardware;

import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OSFileStore;

import java.util.List;

public class Disk {
    private OSFileStore fileStore;
    private HWDiskStore diskStore;

    private String name;
    private String mountpoint;
    private long totalGb;

    private volatile double usedPct;
    private volatile long usedGb;
    private volatile double readMBs;
    private volatile double writeMBs;

    private long prevReadBytes;
    private long prevWriteBytes;
    private long prevTimestamp;

    public Disk init(OSFileStore _fileStore) {
        fileStore = _fileStore;

        name = _fileStore.getName();
        mountpoint = _fileStore.getMount();
        totalGb = _fileStore.getTotalSpace() / 1_073_741_824L;

        diskStore = findParentDisk(HwManager.hal().getDiskStores(), _fileStore);

        if (diskStore != null) {
            prevReadBytes = diskStore.getReadBytes();
            prevWriteBytes = diskStore.getWriteBytes();
        }
        prevTimestamp = System.currentTimeMillis();

        update();

        return this;
    }

    private HWDiskStore findParentDisk(List<HWDiskStore> disks, OSFileStore fileStore) {
        for (HWDiskStore disk : disks) {
            for (HWPartition partition : disk.getPartitions()) {
                if (partition.getMountPoint().equals(fileStore.getMount())) {
                    return disk;
                }
            }
        }
        return null;
    }

    public void update() {
        fileStore.updateAttributes();

        long used = fileStore.getTotalSpace() - fileStore.getUsableSpace();
        usedPct = (double) used / fileStore.getTotalSpace() * 100.0;
        usedGb = used / 1_073_741_824L;

        if (diskStore != null) {
            diskStore.updateAttributes();
            long currentRead = diskStore.getReadBytes();
            long currentWrite = diskStore.getWriteBytes();
            long now = System.currentTimeMillis();
            double elapsed = (now - prevTimestamp) / 1000.0;

            readMBs = (currentRead - prevReadBytes) / elapsed / 1_048_576.0;
            writeMBs = (currentWrite - prevWriteBytes) / elapsed / 1_048_576.0;

            prevReadBytes = currentRead;
            prevWriteBytes = currentWrite;
            prevTimestamp = now;
        }
    }

    public String getName() { return name; }
    public String getMountpoint() { return mountpoint; }
    public long getTotalGb() { return totalGb; }

    public double getUsedPct() { return usedPct; }
    public long getUsedGb() { return usedGb; }
    public double getReadMBs() { return readMBs; }
    public double getWriteMBs() { return writeMBs; }
}
