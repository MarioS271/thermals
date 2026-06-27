package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.components.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class DiskPanel extends JPanelRounded {
    int diskId;

    JLabel diskLabel = null;

    public DiskPanel(int _diskId) {
        diskId = _diskId;

        super(new BorderLayout());

        diskLabel = new JLabel("Disk " + diskId);

        setPreferredSize(UICommons.SMALL_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        diskLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(diskLabel, BorderLayout.NORTH);
    }
}
