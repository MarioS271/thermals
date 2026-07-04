package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.bottom.components.Disk;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DiskPanel extends ComponentPanel {
    ArrayList<Disk> disks = new ArrayList<>();

    public DiskPanel() {
        super("Disks");

        setMinimumSize(UICommons.DEFAULT_PANEL_SIZE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        disks.add(new Disk("Windows (C:)", "NVMe", 71, 24, 8));
        disks.add(new Disk("Daten (D:)", "NVMe", 23, 167, 0));

        for (Disk disk : disks) {
            container.add(disk);
        }
        container.add(Box.createVerticalGlue());

        add(container, BorderLayout.CENTER);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
