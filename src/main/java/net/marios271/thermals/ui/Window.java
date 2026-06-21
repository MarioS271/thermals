package net.marios271.thermals.ui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window {
    static JFrame frame = null;

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

    public static void init() {
        FlatDarkLaf.setup();

        if (frame != null)
            return;

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Thermals");
            frame.setSize(600, 350);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(windowListener);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(new JLabel("Hello, World!"));

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
