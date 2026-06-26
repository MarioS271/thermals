package net.marios271.thermals.ui;

import net.marios271.thermals.ui.top.CpuPanel;
import net.marios271.thermals.ui.top.GpuPanel;
import net.marios271.thermals.ui.top.RamPanel;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    final int hPadding = 50;
    final int vPadding = 10;

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

        setPreferredSize(new Dimension(frame.getWidth(), 300));
        setBorder(BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        add(cpuPanel);
        add(gpuPanel);
        add(ramPanel);
    }
}
