package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.bottom.components.NetAdapter;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class NetPanel extends ComponentPanel {
    ArrayList<NetAdapter> adapters = new ArrayList<>();

    public NetPanel() {
        super("Network Adapters");

        setMinimumSize(UICommons.DEFAULT_PANEL_SIZE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        adapters.add(new NetAdapter("WiFi", 24, 8));
        adapters.add(new NetAdapter("Ethernet", 167, 0));
        adapters.add(new NetAdapter("Tailscale", 0, 0));

        for (NetAdapter adapter : adapters) {
            container.add(adapter);
        }
        container.add(Box.createVerticalGlue());

        add(container, BorderLayout.CENTER);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
