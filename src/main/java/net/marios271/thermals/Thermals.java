package net.marios271.thermals;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.tray.TrayManager;
import net.marios271.thermals.ui.PopupMessage;

public class Thermals {
    public static int DATA_UPDATE_INTERVAL_MS = 500;

    private static String getJarPath() {
        // thisll fail in dev; if ur testing this u need to start the ide as admin
        try {
            return Thermals.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath();
        } catch (Exception e) {
            PopupMessage.createErrPopup("Could not find .jar file to start as admin");
            System.err.println("Could not find .jar file to start as admin");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        if (Platform.isWindows() && !Platform.isAdminWindows()) {
            try {
                new ProcessBuilder("powershell", "-Command",
                    "Start-Process javaw -ArgumentList '-jar \"" + getJarPath() + "\"' -Verb RunAs")
                    .start();
            } catch (Exception e) {
                PopupMessage.createErrPopup("Failed to start as administrator");
                System.err.println("Failed to start as an administator");
                System.exit(1);
            }
            System.exit(0);
        }

        HwManager hwManager = new HwManager();
        hwManager.init();

        TrayManager trayMan = new TrayManager();
        trayMan.start(hwManager);
    }
}
