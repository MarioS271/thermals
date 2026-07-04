package net.marios271.thermals.ui.bottom.components;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class BaseBottomEntry extends JPanel {
    public BaseBottomEntry(String name) {
        setLayout(new BorderLayout());
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setPreferredSize(new Dimension(getWidth(), 30));

        add(new JLabel(name), BorderLayout.WEST);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
