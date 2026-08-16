package net.marios271.thermals.tray;

import net.marios271.thermals.hardware.Gpu;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.hardware.HwUpdateListener;
import net.marios271.thermals.ui.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class TrayManager implements HwUpdateListener {
    HwManager hwManager;

    SystemTray sysTray;
    TrayIcon icon;

    MouseListener mouseListener = new MouseListener() {
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

    public void start(HwManager _hwManager) {
        if (!SystemTray.isSupported()) {
            System.err.println("System does not support system tray, exiting");
            System.exit(1);
        }

        hwManager = _hwManager;
        sysTray = SystemTray.getSystemTray();

        BufferedImage img = new TrayIconDrawer().draw(-1, sysTray.getTrayIconSize());
        icon = new TrayIcon(img);
        icon.setImageAutoSize(false);
        icon.addMouseListener(mouseListener);
        icon.setPopupMenu(buildMenu());

        try {
            sysTray.add(icon);
        } catch (AWTException e) {
            System.err.println("Exception while adding icon to tray: " + e);
        }

        hwManager.addUpdateListener(this);
    }

    PopupMenu buildMenu() {
        MenuItem openWindow = new MenuItem("Open Popup Window");
        openWindow.addActionListener(e -> Window.init(hwManager));

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        PopupMenu menu = new PopupMenu();
        menu.add(openWindow);
        menu.add(exit);

        return menu;
    }

    @Override
    public void onHwUpdate() {
        double cpuTemp = hwManager.cpu().getTempC();
        double total = 0; int count = 0;
        for (Gpu gpu : hwManager.gpus()) { total += gpu.getTempC(); count++; }
        double gpuTemp = count > 0 ? total / count : 0;

        icon.setImage(new TrayIconDrawer().draw(cpuTemp, sysTray.getTrayIconSize()));
        icon.setToolTip("Thermals\n\nCPU: " + cpuTemp + " °C\nGPU: " + gpuTemp + " °C");
    }
}
