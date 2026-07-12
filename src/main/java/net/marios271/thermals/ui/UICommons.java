package net.marios271.thermals.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class UICommons {
    // Statics
    public static final Color WINDOW_BACKGROUND_COLOR = new Color(0x1a1a1c);
    public static final Color PANEL_BACKGROUND_COLOR = new Color(0x2a2a2c);
    public static final Color PANEL_BORDER_COLOR = new Color(0x363638);
    public static final Color GRAPH_GRID_LINE_COLOR = new Color(0x565658);

    public static final int PANEL_SPACING = 15;
    public static final int PANEL_WIDTH = 400;
    public static final int COMPONENT_PANEL_HEIGHT = 200;
    public static final Dimension DEFAULT_PANEL_SIZE = new Dimension(PANEL_WIDTH, COMPONENT_PANEL_HEIGHT);

    public static final int PANEL_HPADDING = 15;
    public static final int PANEL_VPADDING = 10;
    public static final int PANEL_MAIN_SECTION_PADDING = 4;
    public static final int PANEL_STAT_SPACING = 30;

    public static final int MAX_GRAPH_DATASET_SIZE = 50;

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
