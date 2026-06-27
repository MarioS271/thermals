package net.marios271.thermals.ui.components;

import javax.swing.*;
import java.awt.*;

public class JPanelRounded extends JPanel {
    int _borderRadius;

    public JPanelRounded() {
        super(new FlowLayout());
        setOpaque(false);
    }
    public JPanelRounded(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public void setBorderRadius(int borderRadius) {
        _borderRadius = borderRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), _borderRadius, _borderRadius);
        g2d.dispose();
    }
}
