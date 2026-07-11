package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 10;
    final int sidePadding = 10;

    HwManager _hwManager;

    DiskPanel diskPanel;
    NetPanel netPanel;

    public BottomPanel(HwManager hwManager) {
        super();
        setLayout(new GridBagLayout());

        _hwManager = hwManager;

        diskPanel = new DiskPanel();
        netPanel = new NetPanel();

        setBorder(UICommons.fourAxisPadding(topPadding, sidePadding, bottomPadding, sidePadding));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.weightx = 1.5;
        gbc.insets = new Insets(0, 0, 0, UICommons.PANEL_SPACING);
        add(diskPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(netPanel, gbc);
    }

    public void update() {

    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
