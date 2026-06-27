package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.components.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class NetworkPanel extends JPanelRounded {
    int diskId;

    JLabel networkLabel = null;

    public NetworkPanel(int _diskId) {
        diskId = _diskId;

        super(new BorderLayout());

        networkLabel = new JLabel("Network " + diskId);

        setPreferredSize(UICommons.SMALL_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        networkLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(networkLabel, BorderLayout.NORTH);
    }
}
