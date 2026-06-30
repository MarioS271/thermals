package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 10;
    final int sidePadding = 10;

    DiskPanel diskPanel;
    NetPanel netPanel;

    public BottomPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        diskPanel = new DiskPanel();
        netPanel = new NetPanel();

        setBorder(UICommons.fourAxisPadding(topPadding, sidePadding, bottomPadding, sidePadding));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        add(diskPanel);
        add(Box.createHorizontalStrut(UICommons.PANEL_SPACING));
        add(netPanel);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
