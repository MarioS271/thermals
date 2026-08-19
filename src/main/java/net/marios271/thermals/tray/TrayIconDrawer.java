package net.marios271.thermals.tray;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconDrawer {
    static final int cornerRadius = 10;

    static final float coldHue = 0.33f;
    static final float hotHue = 0f;
    static final int coldTemp = 30;  // °C
    static final int hotTemp = 90;  // °C

    public static BufferedImage draw(double tempCelsius, Dimension iconSize) {
        BufferedImage img = new BufferedImage(iconSize.width, iconSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        graphics.setColor(generateColor(tempCelsius));
        graphics.fillRoundRect(0, 0, iconSize.width, iconSize.height, cornerRadius, cornerRadius);
        graphics.dispose();

        return img;
    }

    private static Color generateColor(double temp) {
        float t = (float) (temp - coldTemp) / (hotTemp - coldTemp);
        float hue = coldHue + t * (hotHue - coldHue);
        return Color.getHSBColor(hue, 1f, 1f);
    }
}
