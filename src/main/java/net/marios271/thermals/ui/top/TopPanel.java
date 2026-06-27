package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 0;
    final int sidePadding = 50;

    JFrame frame;

    CpuPanel cpuPanel;
    GpuPanel gpuPanel;
    RamPanel ramPanel;

    public TopPanel(JFrame _frame) {
        super(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_SPACING, UICommons.PANEL_SPACING));

        frame = _frame;

        cpuPanel = new CpuPanel();
        gpuPanel = new GpuPanel();
        ramPanel = new RamPanel();

        setBorder(UICommons.fourAxisPadding(
            topPadding,
            sidePadding,
            bottomPadding,
            sidePadding
        ));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        add(cpuPanel);
        add(gpuPanel);
        add(ramPanel);
    }
}
