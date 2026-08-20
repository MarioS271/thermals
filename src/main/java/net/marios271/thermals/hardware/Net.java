package net.marios271.thermals.hardware;

import oshi.hardware.NetworkIF;

public class Net {
    private NetworkIF networkIF;

    private String name;
    private volatile double uploadMBs;
    private volatile double downloadMBs;

    private long prevBytesSent;
    private long prevBytesRecv;
    private long prevTimestamp;

    public Net init(NetworkIF _networkIF) {
        networkIF = _networkIF;

        name = networkIF.getDisplayName();

        prevBytesSent = networkIF.getBytesSent();
        prevBytesRecv = networkIF.getBytesRecv();
        prevTimestamp = System.currentTimeMillis();

        update();

        return this;
    }

    public void update() {
        networkIF.updateAttributes();

        long currentSent = networkIF.getBytesSent();
        long currentRecv = networkIF.getBytesRecv();
        long now = System.currentTimeMillis();
        double elapsed = (now - prevTimestamp) / 1000.0;

        uploadMBs = (currentSent - prevBytesSent) / elapsed / 1_048_576.0;
        downloadMBs = (currentRecv - prevBytesRecv) / elapsed / 1_048_576.0;

        prevBytesSent = currentSent;
        prevBytesRecv = currentRecv;
        prevTimestamp = now;
    }

    public static boolean isValid(NetworkIF nif) {
        nif.updateAttributes();
        return nif.getIPv4addr().length > 0 && nif.getIfOperStatus() == NetworkIF.IfOperStatus.UP;
    }

    public String getName() { return name; }
    public double getDownloadMBs() { return downloadMBs; }
    public double getUploadMBs() { return uploadMBs; }
}
