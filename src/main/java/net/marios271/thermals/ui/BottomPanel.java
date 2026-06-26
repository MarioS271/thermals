package net.marios271.thermals.ui;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    final int hPadding = 80;
    final int vPadding = 30;

    JFrame frame;

    public BottomPanel(JFrame _frame) {
        super(new GridLayout(1, 4, 10, 10));

        frame = _frame;

        setPreferredSize(new Dimension(frame.getWidth(), 300));
        setBorder(BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding));
    }
}
