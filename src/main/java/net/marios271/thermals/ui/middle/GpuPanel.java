package net.marios271.thermals.ui.middle;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.hardware.Gpu;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.components.Graph;
import net.marios271.thermals.ui.components.Stat;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class GpuPanel extends ComponentPanel {
    private static final Color usagePctColor = Color.GREEN;
    private static final Color tempCColor = new Color(180, 255, 180);

    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int datasetCurrentCol = 0;

    HwManager hwManager;
    int gpuIndex;

    Stat usageStat;
    Stat tempStat;
    Stat vramUsedStat;

    public GpuPanel(HwManager _hwManager, int _gpuIndex) {
        hwManager = _hwManager;
        gpuIndex = _gpuIndex;

        Gpu gpu = _hwManager.gpus().get(_gpuIndex);

        super("GPU  -  " + gpu.getGpuName() + " (" + gpu.getVramTotalGbRounded() + " GB VRAM)");
        setAllSizes(UICommons.DEFAULT_PANEL_SIZE);

        usageStat = new Stat(
            Integer.toString((int)gpu.getUsagePct()),
            "%",
            "Usage",
            usagePctColor
        );
        tempStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(gpu.getTempC()),
            "°C",
            "Temp",
            tempCColor
        );
        vramUsedStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(gpu.getVramUsedGbOneTenth()),
            "GB",
            "VRAM used"
        );

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, UICommons.PANEL_STAT_SPACING, 0));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        stats.add(usageStat);
        stats.add(tempStat);
        stats.add(vramUsedStat);

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
        Gpu gpu = hwManager.gpus().get(gpuIndex);

        final int usagePct = (int)gpu.getUsagePct();
        final double tempC = gpu.getTempC();

        if (datasetCurrentCol >= UICommons.MAX_GRAPH_DATASET_SIZE)
            dataset.removeColumn(0);

        ++datasetCurrentCol;

        dataset.addValue((Number)usagePct, "usagePct", datasetCurrentCol);
        dataset.addValue((Number)tempC, "tempC", datasetCurrentCol);

        usageStat.setValue(Integer.toString(usagePct));
        tempStat.setValue(Helpers.doubleAsSinglePrecisionString(tempC));
        vramUsedStat.setValue(Helpers.doubleAsSinglePrecisionString(gpu.getVramUsedGbOneTenth()));
    }
}
