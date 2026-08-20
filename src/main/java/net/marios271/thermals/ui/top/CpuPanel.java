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
    private static final Color usagePctColor = Color.RED;
    private static final Color tempCColor = new Color(255, 150, 150);

    private final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private int datasetCurrentCol = 0;

    private Cpu cpu;

    private Stat usageStat;
    private Stat coresUsageStat;
    private Stat clockSpdStat;
    private Stat tempStat;

    public CpuPanel() {
        String cpuName = HwManager.cpu().getCpuName();
        super("CPU  -  " + cpuName);

        cpu = HwManager.cpu();

        usageStat = new Stat(
            Integer.toString(HwManager.cpu().getCpuUsagePct()),
            "%",
            "Usage",
            usagePctColor
        );
        coresUsageStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(cpu.getCpuCoreUsage()),
            String.format("/%d", cpu.getLogicalCores()),
            "Core Usage"
        );
        clockSpdStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(cpu.getClockSpeedGhz()),
            "GHz",
            "Clock Speed"
        );
        tempStat = new Stat(
            cpu.getTempCFormatted(),
            "°C",
            "Temperature",
            tempCColor
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
        main.add(new Graph(dataset, usagePctColor, tempCColor));
        main.add(stats);

        add(main, BorderLayout.CENTER);
    }

    public void update() {
        final int usagePct = cpu.getCpuUsagePct();
        final double tempC = cpu.getTempC();

        if (datasetCurrentCol >= UICommons.MAX_GRAPH_DATASET_SIZE)
            dataset.removeColumn(0);

        ++datasetCurrentCol;

        dataset.addValue((Number)usagePct, "usagePct", datasetCurrentCol);
        dataset.addValue((Number)tempC, "tempC", datasetCurrentCol);

        usageStat.setValue(Integer.toString(usagePct));
        coresUsageStat.setValue(Helpers.doubleAsSinglePrecisionString(cpu.getCpuCoreUsage()));
        clockSpdStat.setValue(Helpers.doubleAsSinglePrecisionString(cpu.getClockSpeedGhz()));
        tempStat.setValue(cpu.getTempCFormatted());
    }
}
