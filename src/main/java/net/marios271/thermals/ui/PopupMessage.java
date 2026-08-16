package net.marios271.thermals.ui;

import javax.swing.*;

public class PopupMessage {
    public static void createErrPopup(String text) {
        JOptionPane.showMessageDialog(
            null,
            text,
            "Thermals",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void createWarnPopup(String text) {
        JOptionPane.showMessageDialog(
            null,
            text,
            "Thermals",
            JOptionPane.WARNING_MESSAGE
        );
    }
}
