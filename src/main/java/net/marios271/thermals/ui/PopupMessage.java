package net.marios271.thermals.ui;

import javax.swing.*;

public class PopupMessage {
    private static final String TITLE = "Thermals";

    private static void show(String message, int messageType) {
        showDialog(message, messageType, JOptionPane.DEFAULT_OPTION, TITLE);
    }

    private static int showDialog(String message, int messageType, int optionType, String title) {
        JOptionPane pane = new JOptionPane(message, messageType, optionType);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
        dialog.dispose();
        Object v = pane.getValue();
        return (v instanceof Integer) ? (Integer) v : JOptionPane.CLOSED_OPTION;
    }

    public static void createErrPopup(String text) {
        show(text, JOptionPane.ERROR_MESSAGE);
    }

    public static void createWarnPopup(String text) {
        show(text, JOptionPane.WARNING_MESSAGE);
    }

    public static int createConfirmPopup(String message) {
        return showDialog(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, TITLE);
    }
}
