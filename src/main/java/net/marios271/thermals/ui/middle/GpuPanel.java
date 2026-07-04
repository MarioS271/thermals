package net.marios271.thermals.ui.middle;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.components.Graph;
import net.marios271.thermals.ui.components.Stat;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class GpuPanel extends ComponentPanel {
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public GpuPanel() {
        String gpuName = "NVIDIA RTX 4050";
        super("GPU  -  " + gpuName);

        setAllSizes(UICommons.DEFAULT_PANEL_SIZE);

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_STAT_SPACING, 0));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        stats.add(new Stat("72", "%", "Usage"));
        stats.add(new Stat("69", "°C", "Temp"));
        stats.add(new Stat("4.6", "GB", "VRAM used"));

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(UICommons.uniformPadding(UICommons.PANEL_MAIN_SECTION_PADDING));
        main.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        main.add(new Graph(dataset, Color.MAGENTA));
        main.add(stats);

        add(main, BorderLayout.CENTER);

        for (int i = 0; i < 50; ++i) {
            dataset.addValue(Math.random() * 100, "cpu", String.valueOf(i));
        }
    }
}
