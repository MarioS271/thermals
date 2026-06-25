package net.marios271.thermals.ui;

import javax.swing.*;
import java.awt.*;

public class BottomPanel extends JPanel {
    static final int hPadding = 80;
    static final int vPadding = 30;

    static JFrame frame = null;

    public BottomPanel(JFrame _frame) {
        super(new GridLayout(1, 4, 10, 10));

        frame = _frame;

        setPreferredSize(new Dimension(frame.getWidth(), 300));
        setBorder(BorderFactory.createEmptyBorder(vPadding, hPadding, vPadding, hPadding));
    }
}
