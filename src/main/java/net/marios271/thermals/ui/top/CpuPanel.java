package net.marios271.thermals.ui.top;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.Graph;
import net.marios271.thermals.ui.components.Stat;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class CpuPanel extends ComponentPanel {
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    HwManager _hwManager;

    public CpuPanel(HwManager hwManager) {
        _hwManager = hwManager;

        String cpuName = hwManager.cpu().getCpuName();
        super("CPU  -  " + cpuName);

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_STAT_SPACING, 0));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        stats.add(new Stat("42", "%", "Usage"));
        stats.add(new Stat("3.8", "/8", "Cores used"));
        stats.add(new Stat("8", "/10", "Threads"));
        stats.add(new Stat("50", "°C", "Temp"));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(UICommons.uniformPadding(UICommons.PANEL_MAIN_SECTION_PADDING));
        main.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        main.add(new Graph(dataset, Color.RED));
        main.add(stats);

        add(main, BorderLayout.CENTER);

        for (int i = 0; i < 50; ++i) {
            dataset.addValue(Math.random() * 100, "cpu", String.valueOf(i));
        }
    }
}
