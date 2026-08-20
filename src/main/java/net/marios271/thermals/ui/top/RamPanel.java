package net.marios271.thermals.ui.top;

import net.marios271.thermals.Helpers;
import net.marios271.thermals.Platform;
import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.hardware.Ram;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.components.Graph;
import net.marios271.thermals.ui.components.Stat;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class RamPanel extends ComponentPanel {
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    int datasetCurrentCol = 0;

    HwManager hwManager;

    Stat usedStat;
    Stat cachedStat;
    Stat freeStat;

    public RamPanel(HwManager _hwManager) {
        hwManager = _hwManager;
        Ram ram = _hwManager.ram();

        String title = "RAM  -  " + ram.getCapacityGb() + "GB";
        if (!Platform.isLinux()) {
            title += " " + ram.getType() + "-" + ram.getSpeedMhz();
        }
        super(title);

        usedStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(ram.getUsedGb()),
            "GB",
            "Used"
        );
        cachedStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(ram.getCachedGb()),
            "GB",
            "Cached"
        );
        freeStat = new Stat(
            Helpers.doubleAsSinglePrecisionString(ram.getFreeGb()),
            "GB",
            "Free"
        );

        JPanel stats = new JPanel();
        stats.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, stats.getPreferredSize().height));
        stats.add(usedStat);
        stats.add(Box.createHorizontalStrut(UICommons.PANEL_STAT_SPACING));
        if (!Platform.isWindows()) stats.add(cachedStat);
        stats.add(Box.createHorizontalStrut(UICommons.PANEL_STAT_SPACING));
        stats.add(freeStat);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBorder(UICommons.uniformPadding(UICommons.PANEL_MAIN_SECTION_PADDING));
        main.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        main.add(new Graph(dataset, Color.BLUE));
        main.add(stats);

        add(main, BorderLayout.CENTER);
    }

    public void update() {
        Ram ram = hwManager.ram();

        final double usedPct = ram.getUsedPct();

        if (datasetCurrentCol >= UICommons.MAX_GRAPH_DATASET_SIZE)
            dataset.removeColumn(0);

        ++datasetCurrentCol;

        dataset.addValue((Number)usedPct, "usedPct", datasetCurrentCol);

        usedStat.setValue(Helpers.doubleAsSinglePrecisionString(ram.getUsedGb()));
        if (!Platform.isWindows()) cachedStat.setValue(Helpers.doubleAsSinglePrecisionString(ram.getCachedGb()));
        freeStat.setValue(Helpers.doubleAsSinglePrecisionString(ram.getFreeGb()));
    }
}
