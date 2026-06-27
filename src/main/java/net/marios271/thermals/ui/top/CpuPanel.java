package net.marios271.thermals.ui.top;

import net.marios271.thermals.ui.components.JPanelRounded;
import net.marios271.thermals.ui.UICommons;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class CpuPanel extends JPanelRounded {
    JLabel cpuLabel;
    JLabel cpuNameLabel;

    JPanel main;
    JPanel mainText;
    ChartPanel mainGraph;

    static final int HISTORY = 60;
    int tick = 0;
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    public CpuPanel() {
        super(new BorderLayout());

        setPreferredSize(UICommons.BIG_PANEL_SIZE);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        cpuLabel = new JLabel("CPU");
        cpuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(cpuLabel, BorderLayout.NORTH);

        cpuNameLabel = new JLabel("Intel i7-13620H");
        cpuNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(cpuNameLabel, BorderLayout.SOUTH);

        mainText = new JPanel();
        mainText.setLayout(new BoxLayout(mainText, BoxLayout.Y_AXIS));
        mainText.setMinimumSize(new Dimension(0, 0));
        mainText.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        mainText.add(Box.createVerticalGlue());
        mainText.add(new JLabel("Cores: 3.7/8"));
        mainText.add(new JLabel("Usage: 42%"));
        mainText.add(new JLabel("Temp: 48°C"));
        mainText.add(Box.createVerticalGlue());

        mainGraph = buildChart();

        main = new JPanel(new GridBagLayout());
        main.setBorder(UICommons.uniformPadding(UICommons.PANEL_MAIN_SECTION_PADDING));
        main.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridx = 0;
        constraints.weightx = 1.0;
        main.add(mainText, constraints);

        JPanel graphWrapper = new JPanel(new BorderLayout());
        graphWrapper.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        graphWrapper.add(mainGraph, BorderLayout.CENTER);

        constraints.gridx = 1;
        constraints.weightx = 2.0;
        main.add(graphWrapper, constraints);

        add(main, BorderLayout.CENTER);

        for (int i = 0; i < 50; ++i) {
            dataset.addValue(Math.random() * 100, "cpu", String.valueOf(i));
        }
    }

    ChartPanel buildChart() {
        JFreeChart chart = ChartFactory.createLineChart(
            null,
            null,
            null,
            dataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );
        chart.setBackgroundPaint(UICommons.PANEL_BACKGROUND_COLOR);
        chart.setPadding(new RectangleInsets(0, 0, 0, 0));

        CategoryPlot plot = chart.getCategoryPlot();
        ValueAxis rangeAxis = plot.getRangeAxis();
        CategoryAxis domainAxis = plot.getDomainAxis();

        plot.setBackgroundPaint(UICommons.PANEL_BACKGROUND_COLOR);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setAxisLinePaint(UICommons.CHART_LABEL_COLOR);
        rangeAxis.setTickLabelPaint(UICommons.CHART_LABEL_COLOR);
        domainAxis.setTickLabelsVisible(false);
        domainAxis.setAxisLinePaint(UICommons.CHART_LABEL_COLOR);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setAutoPopulateSeriesStroke(false);
        renderer.setAutoPopulateSeriesPaint(false);
        renderer.setDefaultStroke(new BasicStroke(2.0f));
        renderer.setDefaultPaint(Color.RED);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMinimumSize(new Dimension(0, 0));
        panel.setPopupMenu(null);
        panel.setMouseZoomable(false);
        return panel;
    }
}
