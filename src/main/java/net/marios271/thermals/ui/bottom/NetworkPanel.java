package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;

public class NetworkPanel extends JPanelRounded {
    int diskId;

    JLabel networkLabel = null;

    public NetworkPanel(int _diskId) {
        diskId = _diskId;

        networkLabel = new JLabel("Network " + diskId);

        setPreferredSize(UICommons.SMALL_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorderRadius(20);

        add(networkLabel);
    }
}
