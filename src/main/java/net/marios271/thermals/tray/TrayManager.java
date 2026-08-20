package net.marios271.thermals.tray;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.hardware.Gpu;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class TrayManager {
    static HwManager hwManager;

    static SystemTray sysTray;
    static TrayIcon icon;

    static MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1)
                Window.init(hwManager);
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    };

    public static void start(HwManager _hwManager) {
        if (!SystemTray.isSupported()) {
            return;
        }

        hwManager = _hwManager;
        sysTray = SystemTray.getSystemTray();

        BufferedImage img = TrayIconDrawer.draw(-1, sysTray.getTrayIconSize());
        icon = new TrayIcon(img);
        icon.setImageAutoSize(false);
        icon.addMouseListener(mouseListener);
        icon.setPopupMenu(buildMenu());

        try {
            sysTray.add(icon);
        } catch (AWTException e) {
            System.err.println("Exception while adding icon to tray: " + e);
        }

        hwManager.addUpdateListener(TrayManager::onHwUpdate);
    }

    static PopupMenu buildMenu() {
        MenuItem open = new MenuItem("Open");
        open.addActionListener(e -> Window.init(hwManager));

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        PopupMenu menu = new PopupMenu();
        menu.add(open);
        menu.add(exit);

        return menu;
    }

    public static void onHwUpdate() {
        double cpuTemp = hwManager.cpu().getTempC();
        double total = 0; int count = 0;
        for (Gpu gpu : hwManager.gpus()) { total += gpu.getTempC(); count++; }
        double gpuTemp = count > 0 ? total / count : 0;

        icon.setImage(TrayIconDrawer.draw(cpuTemp, sysTray.getTrayIconSize()));
        icon.setToolTip(
            "Thermals\n\n" +
            "CPU: " + Helpers.doubleAsSinglePrecisionString(cpuTemp) + " °C\n" +
            "GPU: " + Helpers.doubleAsSinglePrecisionString(gpuTemp) + " °C"
        );
    }

    public static boolean isSupported() {
        return SystemTray.isSupported();
    }
}
