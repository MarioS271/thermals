package net.marios271.thermals;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.tray.TrayManager;
import net.marios271.thermals.ui.PopupMessage;
import net.marios271.thermals.ui.Window;

import javax.swing.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Thermals {
    public static int DATA_UPDATE_INTERVAL_MS = 500;

    @SuppressWarnings("unused")
    private static FileChannel lockChannel;
    private static FileLock lockHandle;

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
            String exePath = System.getProperty("jpackage.app-path");
            if (exePath == null) {
                exePath = ProcessHandle.current().info().command().orElse(null);
            }
            try {
                if (exePath != null) {
                    new ProcessBuilder("powershell", "-Command",
                        "Start-Process '" + exePath + "' -Verb RunAs")
                        .start();
                }
            } catch (Exception ignored) {}
            System.exit(0);
        }

        if (!lockInstance()) {
            System.exit(0);
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        if (Platform.isWindows() && !isPawnIOInstalled()) {
            int result = confirmOnTop(
                "Thermals uses the PawnIO kernel driver for CPU temperature readings.\n\n" +
                    "Would you like to install it now? (requires administrator privileges)",
                "Install PawnIO Driver"
            );

            if (result == JOptionPane.YES_OPTION) {
                Path installer = findPawnIOInstaller();
                if (installer == null) {
                    messageOnTop("PawnIO installer not found. Please reinstall Thermals.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Runtime.getRuntime().exec(new String[]{
                            "powershell", "-Command",
                            "Start-Process", "'" + installer.toAbsolutePath() + "'",
                            "-ArgumentList", "'/S'",
                            "-Verb", "RunAs",
                            "-Wait"
                        }).waitFor();
                    } catch (Exception e) {
                        messageOnTop("Failed to launch PawnIO installer:\n" + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        HwManager hwManager = new HwManager();
        hwManager.init();

        Window.init(hwManager);
        TrayManager.start(hwManager);
    }

    private static boolean lockInstance() {
        try {
            Path lockPath = Path.of(System.getProperty("java.io.tmpdir"), "thermals.lock");
            lockChannel = FileChannel.open(lockPath,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            lockHandle = lockChannel.tryLock();
            if (lockHandle == null) {
                lockChannel.close();
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private static int confirmOnTop(String message, String title) {
        JOptionPane pane = new JOptionPane(message,
            JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        dialog.dispose();
        Object v = pane.getValue();
        return (v instanceof Integer) ? (Integer) v : JOptionPane.CLOSED_OPTION;
    }

    private static void messageOnTop(String message, String title, int type) {
        JOptionPane pane = new JOptionPane(message, type);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        dialog.dispose();
    }

    private static boolean isPawnIOInstalled() {
        try {
            Process check = Runtime.getRuntime().exec(
                new String[]{"sc", "query", "PawnIO"}
            );
            check.waitFor();
            return check.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static Path findPawnIOInstaller() {
        Path prod = Path.of("app/PawnIO_setup.exe");
        if (Files.exists(prod)) return prod;

        Path dev = Path.of("resources/windows/PawnIO_setup.exe");
        if (Files.exists(dev)) return dev;

        return null;
    }
}
