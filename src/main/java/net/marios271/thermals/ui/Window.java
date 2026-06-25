package net.marios271.thermals.ui;

import com.formdev.flatlaf.FlatDarkLaf;

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
    static BottomPanel bottomPanel = null;

    public static void init() {
        FlatDarkLaf.setup();

        if (frame != null)
            return;

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Thermals");
            frame.setSize(1000, 600);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(windowListener);

            topPanel = new TopPanel(frame);
            bottomPanel = new BottomPanel(frame);

            JSplitPane splitPlane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
            splitPlane.setDividerLocation(frame.getHeight() / 3);
            splitPlane.setDividerSize(0);
            splitPlane.setEnabled(false);

            frame.add(splitPlane);
            frame.setVisible(true);
        });
    }
}
