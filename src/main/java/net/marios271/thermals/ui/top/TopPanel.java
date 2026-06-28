package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.middle.GpuPanel;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 0;
    final int sidePadding = 10;

    JFrame frame;

    CpuPanel cpuPanel;
    RamPanel ramPanel;

    public TopPanel(JFrame _frame) {
        super(new GridBagLayout());

        frame = _frame;

        cpuPanel = new CpuPanel();
        ramPanel = new RamPanel();

        setBorder(UICommons.fourAxisPadding(
            topPadding,
            sidePadding,
            bottomPadding,
            sidePadding
        ));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;

        constraints.gridx = 0;
        constraints.weightx = 2;
        add(cpuPanel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(0, UICommons.PANEL_SPACING, 0, 0);
        add(ramPanel, constraints);
    }
}
