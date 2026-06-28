package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.WrapLayout;

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
        netPanels.add(new NetworkPanel(1));
        netPanels.add(new NetworkPanel(2));
        netPanels.add(new NetworkPanel(3));

        setBorder(UICommons.fourAxisPadding(
            topPadding,
            sidePadding,
            bottomPadding,
            sidePadding
        ));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        for (DiskPanel panel : diskPanels) {
            add(panel);
        }
        for (NetworkPanel panel : netPanels) {
            add(panel);
        }
    }
}
