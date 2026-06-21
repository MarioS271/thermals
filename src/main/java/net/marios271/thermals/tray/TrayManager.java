package net.marios271.thermals.tray;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayManager {
    public void start() {
        if (!SystemTray.isSupported()) {
            System.err.println("System does not support system tray, exiting");
            System.exit(1);
        }

        SystemTray sysTray = SystemTray.getSystemTray();
        Dimension sysTrayDims = sysTray.getTrayIconSize();

        // placeholder temps for now
        int cpuTemp = 50;
        int gpuTemp = 50;

        BufferedImage img = new TrayIconDrawer().draw(cpuTemp, sysTrayDims);

        TrayIcon icon = new TrayIcon(img);
        icon.setImageAutoSize(false);
        icon.setToolTip("Thermals\n\nCPU: " + cpuTemp + " °C\nGPU: " + gpuTemp + " °C");

        try {
            sysTray.add(icon);
        } catch (AWTException e) {
            System.err.println("Exception while adding icon to tray: " + e);
        }
    }
}
