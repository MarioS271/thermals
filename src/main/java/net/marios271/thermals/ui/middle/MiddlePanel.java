package net.marios271.thermals.ui.middle;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.ComponentPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MiddlePanel extends JPanel {
    final int topPadding = 10;
    final int bottomPadding = 0;
    final int sidePadding = 10;

    HwManager _hwManager;

    ArrayList<ComponentPanel> panels = new ArrayList<>();

    public MiddlePanel(HwManager hwManager) {
        super(new BorderLayout());

        _hwManager = hwManager;

        panels.add(new GpuPanel());
        panels.add(new FanPanel());

        setBorder(UICommons.fourAxisPadding(topPadding, sidePadding, bottomPadding, sidePadding));
        setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        int scrollbarHeight = new JScrollBar(JScrollBar.HORIZONTAL).getPreferredSize().height;
        int fixedHeight = UICommons.COMPONENT_PANEL_HEIGHT + topPadding + bottomPadding + scrollbarHeight;
        setPreferredSize(new Dimension(100, fixedHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, fixedHeight));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
        inner.setBackground(UICommons.WINDOW_BACKGROUND_COLOR);

        boolean first = true;
        for (ComponentPanel panel : panels) {
            if (first) first = false;
            else inner.add(Box.createHorizontalStrut(UICommons.PANEL_SPACING));

            inner.add(panel);
        }

        JScrollPane scroll = new JScrollPane(
            inner,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scroll.setBackground(UICommons.WINDOW_BACKGROUND_COLOR);
        scroll.getHorizontalScrollBar().setUnitIncrement(6);
        scroll.setBorder(null);

        add(scroll, BorderLayout.CENTER);
    }
}
