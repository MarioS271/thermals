package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    private final int topPadding = 10;
    private final int bottomPadding = 10;
    private final int sidePadding = 10;

    private DiskPanel diskPanel;
    private NetPanel netPanel;

    public BottomPanel() {
        super();
        setLayout(new GridBagLayout());

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
        diskPanel.update(HwManager.disks());
        netPanel.update(HwManager.nets());
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
