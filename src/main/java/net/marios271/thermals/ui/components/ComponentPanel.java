package net.marios271.thermals.ui.components;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class ComponentPanel extends JPanelRounded {
    JLabel panelTitle;

    public ComponentPanel(String _title) {
        super(new BorderLayout());

        setPreferredSize(new Dimension(0, UICommons.COMPONENT_PANEL_HEIGHT));
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);
        setBorder(UICommons.twoAxisPadding(
            UICommons.PANEL_VPADDING,
            UICommons.PANEL_HPADDING
        ));
        setBorderRadius(20);

        panelTitle = new JLabel(_title);
        add(panelTitle, BorderLayout.NORTH);
    }

    public void setAllSizes(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
    }
}
