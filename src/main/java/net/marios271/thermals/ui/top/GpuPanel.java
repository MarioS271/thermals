package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.components.JPanelRounded;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class GpuPanel extends JPanelRounded {
    JLabel gpuLabel = new JLabel("GPU");

    public GpuPanel() {
        super(new BorderLayout());

        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        gpuLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(gpuLabel, BorderLayout.NORTH);
    }
}
