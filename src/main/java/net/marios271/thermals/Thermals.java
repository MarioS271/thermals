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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        if (Platform.isWindows() && !Platform.isAdminWindows())
            restartAsAdmin();

        if (!lockInstance())
            System.exit(0);

        if (Platform.isWindows() && !isPawnIOInstalled()) {
            int result = PopupMessage.createConfirmPopup(
                "Thermals uses the PawnIO kernel driver for CPU temperature readings.\n" +
                "Would you like to install it now? (requires administrator privileges)"
            );

            if (result == JOptionPane.YES_OPTION) {
                Path installer = findPawnIOInstaller();
                if (installer == null) {
                    PopupMessage.createWarnPopup(
                        "PawnIO installer not found. Please reinstall Thermals " +
                        "or download the PawnIO driver manually at https://pawnio.eu"
                    );
                } else {
                    try {
                        Runtime.getRuntime().exec(new String[]{
                            "powershell", "-Command",
                            "Start-Process", "'" + installer.toAbsolutePath() + "'",
                            "-ArgumentList", "'-install -silent'",
                            "-Verb", "RunAs",
                            "-Wait"
                        }).waitFor();
                    } catch (Exception e) {
                        PopupMessage.createErrPopup("Failed to launch PawnIO installer:\n" + e.getMessage());
                    }
                }
            }
        }

        HwManager.init();
        Window.init();
        TrayManager.init();
    }

    private static void restartAsAdmin() {
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

    private static boolean lockInstance() {
        try {
            Path lockPath = Path.of(System.getProperty("java.io.tmpdir"), "thermals.lock");

            lockChannel = FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
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
