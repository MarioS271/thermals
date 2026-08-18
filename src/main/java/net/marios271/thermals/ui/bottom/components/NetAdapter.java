package net.marios271.thermals.ui.bottom.components;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class NetAdapter extends BaseBottomEntry {
    final int GAP = 10;
    final char UP_ARROW = '↑';
    final char DOWN_ARROW = '↓';

    UpDownLabel _download;
    UpDownLabel _upload;
    
    public NetAdapter(String adapterName, int readRate, int writeRate) {
        String display = adapterName.length() > 37 ? adapterName.substring(0, 37) + "..." : adapterName;
        super(display);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.X_AXIS));
        details.setBackground(UICommons.PANEL_BACKGROUND_COLOR);

        _download = new UpDownLabel(DOWN_ARROW, Color.CYAN, "MB/s", writeRate);
        _upload = new UpDownLabel(UP_ARROW, Color.MAGENTA, "MB/s", readRate);

        details.add(_download);
        details.add(Box.createHorizontalStrut(GAP));
        details.add(_upload);

        add(details, BorderLayout.EAST);
    }

    public void update_values(double download, double upload) {
        _download.setValue(download);
        _upload.setValue(upload);
    }
}
