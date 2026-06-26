package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;

public class GpuPanel extends JPanelRounded {
    JLabel gpuLabel = new JLabel("GPU");

    public GpuPanel() {
        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorderRadius(20);
        add(gpuLabel);
    }
}
