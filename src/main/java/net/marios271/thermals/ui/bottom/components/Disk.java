package net.marios271.thermals.ui.bottom.components;

import net.marios271.thermals.ui.UICommons;
import net.marios271.thermals.ui.components.PercentageBar;

import javax.swing.*;
import java.awt.*;

public class Disk extends BaseBottomEntry {
    private final int GAP = 20;
    private final char UP_ARROW = '↑';
    private final char DOWN_ARROW = '↓';

    private long _totalGb;

    private JLabel _usageTextGb;
    private JLabel _usageText;
    private PercentageBar _usageBar;
    private UpDownLabel _readRate;
    private UpDownLabel _writeRate;

    public Disk(String diskName, long totalGb, String mountpoint, int usedPct, long usedGb, double readMBs, double writeMBs) {
        String name = diskName + " (" + mountpoint + ")";
        String display = name.length() > 35 ? name.substring(0, 35) + "..." : name;
        super(display);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.X_AXIS));
        details.setBackground(UICommons.PANEL_BACKGROUND_COLOR);

        _readRate = new UpDownLabel(UP_ARROW, Color.YELLOW, "MB/s", readMBs);
        _writeRate = new UpDownLabel(DOWN_ARROW, new Color(0xD0800C), "MB/s", writeMBs);
        _usageTextGb = new JLabel(usedGb + "GB / " + totalGb + "GB");
        _usageBar = new PercentageBar(60, 7, usedPct);
        _usageText = new JLabel(usedPct + "%");

        details.add(_readRate);
        details.add(Box.createHorizontalStrut(GAP / 2));
        details.add(_writeRate);
        details.add(Box.createHorizontalStrut(GAP + (GAP / 2)));
        details.add(_usageTextGb);
        details.add(Box.createHorizontalStrut(GAP / 2));
        details.add(_usageBar);
        details.add(Box.createHorizontalStrut(GAP / 2));
        details.add(_usageText);

        add(details, BorderLayout.EAST);

        _totalGb = totalGb;
    }

    public void update_values(int usedPct, long usedGb, double readMBs, double writeMBs) {
        _usageTextGb.setText(usedGb + "GB / " + _totalGb + "GB");
        _usageBar.setPercent(usedPct);
        _usageText.setText(usedPct + "%");
        _readRate.setValue(readMBs);
        _writeRate.setValue(writeMBs);
    }
}
