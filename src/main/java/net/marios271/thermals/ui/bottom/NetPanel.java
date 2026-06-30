package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;

import java.awt.*;

public class NetPanel extends ComponentPanel {
    public NetPanel() {
        super("Network Adapters");

        setMinimumSize(UICommons.DEFAULT_PANEL_SIZE);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
