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

        // placeholder temp for now
        int temp = 50;
        BufferedImage img = new TrayIconDrawer().draw(temp, sysTrayDims);

        TrayIcon icon = new TrayIcon(img);
        icon.setImageAutoSize(false);

        try {
            sysTray.add(icon);
        } catch (AWTException e) {
            System.err.println("Exception while adding icon to tray: " + e);
        }
    }
}
