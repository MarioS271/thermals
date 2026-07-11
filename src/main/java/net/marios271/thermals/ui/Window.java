package net.marios271.thermals.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.bottom.BottomPanel;
import net.marios271.thermals.ui.middle.MiddlePanel;
import net.marios271.thermals.ui.top.TopPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window {
    static WindowListener windowListener = new WindowListener() {
        @Override
        public void windowOpened(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }

        @Override
        public void windowClosed(WindowEvent e) {
            frame = null;
        }

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}
    };

    static JFrame frame = null;
    static TopPanel topPanel = null;
    static MiddlePanel middlePanel = null;
    static BottomPanel bottomPanel = null;

    static HwManager _hwManager;

    public static void init(HwManager hwManager) {
        FlatDarkLaf.setup();
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.track", UICommons.WINDOW_BACKGROUND_COLOR);
        UIManager.put("ScrollBar.hoverTrackColor", UICommons.WINDOW_BACKGROUND_COLOR);
        UIManager.put("ScrollBar.thumbInsets", new Insets(0, 2, 0, 0));

        if (frame != null)
            return;

        _hwManager = hwManager;

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Thermals");
            frame.setResizable(false);
            frame.setSize(new Dimension(1100, 850));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(windowListener);
            frame.getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_BACKGROUND, UICommons.WINDOW_BACKGROUND_COLOR);

            topPanel = new TopPanel(_hwManager);
            middlePanel = new MiddlePanel(_hwManager);
            bottomPanel = new BottomPanel(_hwManager);

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
        });
    }
}
