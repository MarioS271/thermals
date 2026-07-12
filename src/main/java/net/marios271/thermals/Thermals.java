package net.marios271.thermals;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.tray.TrayManager;

public class Thermals {
    public static int DATA_UPDATE_INTERVAL_MS = 500;

    public static void main(String[] args) {
        HwManager hwManager = new HwManager();
        hwManager.init();

        TrayManager trayMan = new TrayManager();
        trayMan.start(hwManager);
    }
}
