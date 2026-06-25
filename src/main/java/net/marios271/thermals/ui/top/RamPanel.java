package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;

public class RamPanel extends JPanelRounded {
    static JLabel ramLabel = new JLabel("RAM");

    public RamPanel() {
        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorderRadius(20);
        add(ramLabel);
    }
}
