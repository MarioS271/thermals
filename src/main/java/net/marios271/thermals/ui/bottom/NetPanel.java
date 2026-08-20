package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.hardware.Net;
import net.marios271.thermals.ui.bottom.components.NetAdapter;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class NetPanel extends ComponentPanel {
    private ArrayList<NetAdapter> adapters = new ArrayList<>();

    public NetPanel() {
        super("Network Adapters");

        setMinimumSize(UICommons.DEFAULT_PANEL_SIZE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (Net net : HwManager.nets()) {
            NetAdapter adapter = new NetAdapter(
                net.getName(),
                (int)net.getDownloadMBs(),
                (int)net.getUploadMBs()
            );
            adapters.add(adapter);
            container.add(adapter);
        }

        container.add(Box.createVerticalGlue());
        add(container, BorderLayout.CENTER);
    }

    public void update(List<Net> net) {
        for (int i = 0; i < adapters.size(); i++) {
            Net data = net.get(i);
            adapters.get(i).update_values(
                data.getDownloadMBs(),
                data.getUploadMBs()
            );
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    @Override
    public Dimension getPreferredSize() {
        int diskHeight = 30; // ik i should NOT hardcode this here but get this: im lazy af :)
        int padding = 50;
        int needed = adapters.size() * diskHeight + padding;
        int normal = super.getPreferredSize().height;
        return new Dimension(super.getPreferredSize().width, Math.max(normal, needed));
    }
}
