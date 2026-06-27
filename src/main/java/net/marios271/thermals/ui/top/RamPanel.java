package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.components.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class RamPanel extends JPanelRounded {
    JLabel ramLabel = new JLabel("RAM");

    public RamPanel() {
        super(new BorderLayout());

        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        ramLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(ramLabel, BorderLayout.NORTH);
    }
}
