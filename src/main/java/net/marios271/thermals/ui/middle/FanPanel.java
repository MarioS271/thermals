package net.marios271.thermals.ui.middle;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.components.Stat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FanPanel extends ComponentPanel {
    ArrayList<Stat> fans = new ArrayList<>();

    public FanPanel() {
        super("Fan Speeds");

        fans.add(new Stat("3500", "RPM", "Fan 1"));
        fans.add(new Stat("3500", "RPM", "Fan 2"));
        fans.add(new Stat("3500", "RPM", "Fan 3"));

        int cols = (fans.size() + 1) / 2;
        int width = cols * (UICommons.PANEL_WIDTH / 3);
        setAllSizes(new Dimension(
            Math.max(width, UICommons.PANEL_WIDTH),
            UICommons.COMPONENT_PANEL_HEIGHT)
        );

        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(2, cols));
        stats.setBackground(UICommons.PANEL_BACKGROUND_COLOR);

        for (Stat fan : fans) {
            stats.add(fan);
        }

        add(stats);
    }
}
