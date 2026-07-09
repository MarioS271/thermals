package net.marios271.thermals.ui.bottom.components;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.PercentageBar;

import javax.swing.*;
import java.awt.*;

public class Disk extends BaseBottomEntry {
    final int GAP = 20;
    final char UP_ARROW = '↑';
    final char DOWN_ARROW = '↓';

    JLabel _diskType;
    JLabel _usageText;
    PercentageBar _usageBar;
    UpDownLabel _download;
    UpDownLabel _upload;

    public Disk(String diskName, String diskType, int usagePct, int readRate, int writeRate) {
        super(diskName);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.X_AXIS));
        details.setBackground(UICommons.PANEL_BACKGROUND_COLOR);

        _diskType = new JLabel(diskType);
        _usageText = new JLabel(usagePct + "%");
        _usageBar = new PercentageBar(60, 7, usagePct);
        _download = new UpDownLabel(DOWN_ARROW, Color.PINK, "MB/s", writeRate);
        _upload = new UpDownLabel(UP_ARROW, Color.YELLOW, "MB/s", readRate);

        details.add(_diskType);
        details.add(Box.createHorizontalStrut(GAP));
        details.add(_usageBar);
        details.add(Box.createHorizontalStrut(GAP / 2));
        details.add(_usageText);
        details.add(Box.createHorizontalStrut(GAP));
        details.add(_download);
        details.add(Box.createHorizontalStrut(GAP / 2));
        details.add(_upload);

        add(details, BorderLayout.EAST);
    }
}
