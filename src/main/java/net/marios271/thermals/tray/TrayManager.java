package net.marios271.thermals.tray;

import net.marios271.thermals.ui.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class TrayManager {
    static MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            Window.init();
            if (e.getButton() == MouseEvent.BUTTON1)
                Window.init();
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

    public void start() {
        if (!SystemTray.isSupported()) {
            System.err.println("System does not support system tray, exiting");
            System.exit(1);
        }

        SystemTray sysTray = SystemTray.getSystemTray();
        Dimension sysTrayDims = sysTray.getTrayIconSize();

        // placeholder temps for now
        double cpuTemp = 50;
        double gpuTemp = 40;

        BufferedImage img = new TrayIconDrawer().draw(cpuTemp, sysTrayDims);

        PopupMenu menu = buildMenu();

        TrayIcon icon = new TrayIcon(img);
        icon.setImageAutoSize(false);
        icon.setToolTip("Thermals\n\nCPU: " + cpuTemp + " °C\nGPU: " + gpuTemp + " °C");
        icon.addMouseListener(mouseListener);
        icon.setPopupMenu(menu);

        try {
            sysTray.add(icon);
        } catch (AWTException e) {
            System.err.println("Exception while adding icon to tray: " + e);
        }
    }

    PopupMenu buildMenu() {
        MenuItem openWindow = new MenuItem("Open Popup Window");
        openWindow.addActionListener(e -> Window.init());

        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        PopupMenu menu = new PopupMenu();
        menu.add(openWindow);
        menu.add(exit);

        return menu;
    }
}
