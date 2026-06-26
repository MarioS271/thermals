package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;

public class CpuPanel extends JPanelRounded {
    JLabel cpuLabel = new JLabel("CPU");

    public CpuPanel() {
        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorderRadius(20);

        add(cpuLabel);
    }
}
