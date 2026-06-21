package net.marios271.thermals;

import net.marios271.thermals.tray.TrayManager;

public class Thermals {
    public static void main(String[] args) {
        TrayManager trayMan = new TrayManager();
        trayMan.start();
    }
}
