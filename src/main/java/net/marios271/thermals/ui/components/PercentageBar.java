package net.marios271.thermals.ui.components;

import javax.swing.*;
import java.awt.*;

public class PercentageBar extends JComponent {
    final int BORDER_RADIUS = 7;
    final Color BG_COLOR = Color.GRAY;
    int _pct;
    Color _fillColor;

    public PercentageBar(int width, int height, int initialPct) {
        _pct = initialPct;
        computeFillColor();

        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
    }

    private void computeFillColor() {
        if (_pct >= 0 && _pct <= 70) _fillColor = Color.GREEN;
        else if (_pct > 70 && _pct <= 85) _fillColor = Color.YELLOW;
        else if (_pct > 85 && _pct <= 100) _fillColor = Color.RED;
    }

    public void setPercent(int percent) {
        _pct = percent;
        computeFillColor();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(BG_COLOR);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);

        int filledWidth = (int)(getWidth() * (_pct / 100.0));
        g2d.setColor(_fillColor);
        g2d.fillRoundRect(0, 0, filledWidth, getHeight(), BORDER_RADIUS, BORDER_RADIUS);
    }
}
