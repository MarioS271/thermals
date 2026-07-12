package net.marios271.thermals.ui.components;

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

import java.awt.*;

public class Graph extends ChartPanel {
    public Graph(DefaultCategoryDataset _dataset, Color _chartColor) {
        JFreeChart chart = ChartFactory.createLineChart(
            null,
            null,
            null,
            _dataset,
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
        plot.setRangeGridlinePaint(UICommons.GRAPH_GRID_LINE_COLOR);
        plot.setDomainGridlinesVisible(false);

        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setAxisLinePaint(UICommons.PANEL_BACKGROUND_COLOR);
        domainAxis.setVisible(false);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setAutoPopulateSeriesStroke(false);
        renderer.setAutoPopulateSeriesPaint(false);
        renderer.setDefaultStroke(new BasicStroke(2.0f));
        renderer.setDefaultPaint(_chartColor);

        super(chart);
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setMinimumSize(new Dimension(0, 0));
        setBorder(null);
        setPopupMenu(null);
        setMouseZoomable(false);
    }
}
