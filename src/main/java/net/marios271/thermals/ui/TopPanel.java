package net.marios271.thermals.ui;

import net.marios271.thermals.ui.top.CpuPanel;
import net.marios271.thermals.ui.top.GpuPanel;

import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {
    static final int hPadding = 50;
    static final int vPadding = 10;

    static JFrame frame = null;
    static CpuPanel cpuPanel = null;
    static GpuPanel gpuPanel = null;

    public TopPanel(JFrame _frame) {
        super(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_SPACING, UICommons.PANEL_SPACING));

        frame = _frame;

        cpuPanel = new CpuPanel();
        gpuPanel = new GpuPanel();

        setPreferredSize(new Dimension(frame.getWidth(), 300));
        setBorder(BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding));

        add(cpuPanel);
        add(gpuPanel);
    }
}
