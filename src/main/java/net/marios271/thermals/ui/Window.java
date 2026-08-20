package net.marios271.thermals.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.tray.TrayManager;
import net.marios271.thermals.ui.bottom.BottomPanel;
import net.marios271.thermals.ui.middle.MiddlePanel;
import net.marios271.thermals.ui.top.TopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window {
    private static long shownAt = 0;

    static WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            frame = null;
        }

        @Override
        public void windowIconified(WindowEvent e) {
            if (TrayManager.isSupported() && System.currentTimeMillis() - shownAt > 1000)
                e.getWindow().dispose();
        }

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}
    };

    private static JFrame frame = null;
    private static TopPanel topPanel = null;
    private static MiddlePanel middlePanel = null;
    private static BottomPanel bottomPanel = null;

    public static void init() {
        FlatDarkLaf.setup();
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.track", UICommons.WINDOW_BACKGROUND_COLOR);
        UIManager.put("ScrollBar.hoverTrackColor", UICommons.WINDOW_BACKGROUND_COLOR);
        UIManager.put("ScrollBar.thumbInsets", new Insets(0, 2, 0, 0));

        HwManager.addUpdateListener(Window::onHwUpdate);

        open();
    }

    public static void open() {
        if (frame != null) {
            SwingUtilities.invokeLater(() -> {
                frame.setExtendedState(Frame.NORMAL);
                frame.setVisible(true);
                frame.toFront();
                frame.requestFocus();
            });
            return;
        }

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Thermals");
            frame.setResizable(false);
            frame.setSize(new Dimension(1100, 850));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(windowListener);
            frame.getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, UICommons.WINDOW_BACKGROUND_COLOR);

            topPanel = new TopPanel();
            middlePanel = new MiddlePanel();
            bottomPanel = new BottomPanel();

            JPanel main = new JPanel();
            main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
            main.setBackground(UICommons.WINDOW_BACKGROUND_COLOR);
            main.add(topPanel);
            main.add(middlePanel);
            main.add(bottomPanel);

            JScrollPane scroll = new JScrollPane(main);
            scroll.getVerticalScrollBar().setUnitIncrement(12);
            scroll.setBorder(null);
            scroll.getViewport().setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

            frame.add(scroll);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();

            shownAt = System.currentTimeMillis();
        });
    }

    public static void onHwUpdate() {
        if (frame == null) return;
        SwingUtilities.invokeLater(() -> {
            if (topPanel != null) topPanel.update();
            if (middlePanel != null) middlePanel.update();
            if (bottomPanel != null) bottomPanel.update();
        });
    }
}
