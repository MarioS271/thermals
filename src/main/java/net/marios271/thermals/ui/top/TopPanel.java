package net.marios271.thermals.ui.top;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 0;
    final int sidePadding = 10;

    HwManager _hwManager;

    CpuPanel cpuPanel;
    RamPanel ramPanel;

    public TopPanel(HwManager hwManager) {
        super(new GridBagLayout());

        _hwManager = hwManager;

        cpuPanel = new CpuPanel(_hwManager);
        ramPanel = new RamPanel();

        setBorder(UICommons.fourAxisPadding(
            topPadding,
            sidePadding,
            bottomPadding,
            sidePadding
        ));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);
        int fixedHeight = UICommons.COMPONENT_PANEL_HEIGHT + topPadding + bottomPadding;
        setPreferredSize(new Dimension(100, fixedHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, fixedHeight));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.0;

        constraints.gridx = 0;
        constraints.weightx = 2;
        add(cpuPanel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, UICommons.PANEL_SPACING, 0, 0);
        add(ramPanel, constraints);
    }

    public void update() {
        cpuPanel.update();
    }
}
