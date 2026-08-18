package net.marios271.thermals.ui.bottom;

import net.marios271.thermals.hardware.HwManager;
import net.marios271.thermals.ui.components.ComponentPanel;
import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.bottom.components.Disk;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class DiskPanel extends ComponentPanel {
    ArrayList<Disk> disks = new ArrayList<>();

    public DiskPanel(HwManager hwManager) {
        super("Disks");

        setMinimumSize(UICommons.DEFAULT_PANEL_SIZE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (net.marios271.thermals.hardware.Disk hwDisk : hwManager.disks()) {
            Disk disk = new Disk(hwDisk.getName(), hwDisk.getTotalGb(), hwDisk.getMountpoint(),
                (int)hwDisk.getUsedPct(), hwDisk.getUsedGb(), hwDisk.getReadMBs(), hwDisk.getWriteMBs());
            disks.add(disk);
        }

        for (Disk disk : disks) {
            container.add(disk);
        }
        container.add(Box.createVerticalGlue());

        add(container, BorderLayout.CENTER);
    }

    public void update(List<net.marios271.thermals.hardware.Disk> hwDisk) {
        for (int i = 0; i < disks.size(); i++) {
            var data = hwDisk.get(i);
            disks.get(i).update_values(
                (int) data.getUsedPct(),
                data.getUsedGb(),
                data.getReadMBs(),
                data.getWriteMBs()
            );
        }
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
