package net.marios271.thermals.ui;

import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class UICommons {
    // Statics
    public static final Color WINDOW_BACKGROUND_COLOR = new Color(0x222224);
    public static final Color PANEL_BACKGROUND_COLOR = new Color(0x404042);

    public static final Color CHART_LABEL_COLOR = new Color(0xe0e0e0);

    public static final int PANEL_SPACING = 15;
    public static final Dimension BIG_PANEL_SIZE = new Dimension(270, 166);
    public static final Dimension SMALL_PANEL_SIZE = new Dimension(210, 129);

    public static final int PANEL_HPADDING = 20;
    public static final int PANEL_VPADDING = 10;
    public static final int PANEL_MAIN_SECTION_PADDING = 4;

    // Helpers
    public static Border uniformPadding(int padding) {
        return BorderFactory.createEmptyBorder(padding, padding, padding, padding);
    }
    public static Border twoAxisPadding(int vpad, int hpad) {
        return BorderFactory.createEmptyBorder(vpad, hpad, vpad, hpad);
    }
    public static Border fourAxisPadding(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    private UICommons() {}
}
