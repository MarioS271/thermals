package net.marios271.thermals.ui;

import net.marios271.thermals.ui.bottom.DiskPanel;
import net.marios271.thermals.ui.bottom.NetworkPanel;

import javax.swing.*;
import java.util.ArrayList;

public class BottomPanel extends JPanel {
    final int topPadding = 0;
    final int bottomPadding = 70;
    final int sidePadding = 80;

    JFrame frame;
    ArrayList<DiskPanel> diskPanels = new ArrayList<>();
    ArrayList<NetworkPanel> netPanels = new ArrayList<>();

    public BottomPanel(JFrame _frame) {
        super(new WrapLayout(WrapLayout.LEFT, UICommons.PANEL_SPACING, UICommons.PANEL_SPACING));

        frame = _frame;

        diskPanels.add(new DiskPanel(1));
        diskPanels.add(new DiskPanel(2));
        diskPanels.add(new DiskPanel(3));
        diskPanels.add(new DiskPanel(4));
        diskPanels.add(new DiskPanel(5));
        diskPanels.add(new DiskPanel(6));
        diskPanels.add(new DiskPanel(7));
        diskPanels.add(new DiskPanel(8));
        netPanels.add(new NetworkPanel(0));
        netPanels.add(new NetworkPanel(1));
        netPanels.add(new NetworkPanel(2));
        netPanels.add(new NetworkPanel(3));
        netPanels.add(new NetworkPanel(4));

        setBorder(BorderFactory.createEmptyBorder(topPadding, sidePadding, bottomPadding, sidePadding));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        for (DiskPanel panel : diskPanels) {
            add(panel);
        }
        for (NetworkPanel panel : netPanels) {
            add(panel);
        }
    }
}
