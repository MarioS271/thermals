package net.marios271.thermals.ui.top;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.hardware.Cpu;
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
    int datasetCurrentCol = 0;

    HwManager hwManager;

    Stat usageStat;
    Stat coresUsageStat;
    Stat clockSpdStat;
    Stat tempStat;

    public CpuPanel(HwManager _hwManager) {
        hwManager = _hwManager;

        String cpuName = _hwManager.cpu().getCpuName();
        super("CPU  -  " + cpuName);

        usageStat = new Stat(
            Integer.toString(hwManager.cpu().getCpuUsagePct()),
            "%",
            "Usage"
        );
        coresUsageStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(hwManager.cpu().getCpuCoreUsage()),
            String.format("/%d", hwManager.cpu().getLogicalCores()),
            "Core Usage"
        );
        clockSpdStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(hwManager.cpu().getClockSpeedGhz()),
            "GHz",
            "Clock Speed"
        );
        tempStat = new Stat(
            "50",
            "°C",
            "Temperature"
        );

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_STAT_SPACING, 0));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        stats.add(usageStat);
        stats.add(coresUsageStat);
        stats.add(clockSpdStat);
        stats.add(tempStat);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(UICommons.uniformPadding(UICommons.PANEL_MAIN_SECTION_PADDING));
        main.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        main.add(new Graph(dataset, Color.RED));
        main.add(stats);

        add(main, BorderLayout.CENTER);
    }

    public void update() {
        Cpu cpu = hwManager.cpu();

        final int usagePct = cpu.getCpuUsagePct();

        if (datasetCurrentCol >= UICommons.MAX_GRAPH_DATASET_SIZE)
            dataset.removeColumn(0);

        ++datasetCurrentCol;

        dataset.addValue((Number)usagePct, "cpu", datasetCurrentCol);

        usageStat.setValue(Integer.toString(usagePct));
        coresUsageStat.setValue(Helpers.doubleAsSinglePrecisionString(cpu.getCpuCoreUsage()));
        clockSpdStat.setValue(Helpers.doubleAsSinglePrecisionString(cpu.getClockSpeedGhz()));
        tempStat.setValue(Helpers.doubleAsSinglePrecisionString(cpu.getTempC()));
    }
}
