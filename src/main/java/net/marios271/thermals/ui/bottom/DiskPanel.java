package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;

public class DiskPanel extends JPanelRounded {
    int diskId;

    JLabel diskLabel = null;

    public DiskPanel(int _diskId) {
        diskId = _diskId;

        diskLabel = new JLabel("Disk " + diskId);

        setPreferredSize(UICommons.SMALL_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorderRadius(20);

        add(diskLabel);
    }
}
