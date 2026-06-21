package net.marios271.thermals.tray;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrayIconDrawer {
    public BufferedImage draw(int tempCelsius, Dimension iconSize) {
        BufferedImage img = new BufferedImage(iconSize.width, iconSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, iconSize.width, iconSize.height);
        graphics.dispose();

        return img;
    }
}
