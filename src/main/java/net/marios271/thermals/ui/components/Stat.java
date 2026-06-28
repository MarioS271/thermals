package net.marios271.thermals.ui.components;

import net.marios271.thermals.ui.UICommons;

import javax.swing.*;
import java.awt.*;

public class Stat extends JPanel {
    public Stat(String _value, String _valueSuffix, String _statName) {
        super(new GridBagLayout());
        setBackground(UICommons.PANEL_BACKGROUND_COLOR);

        JLabel valueLabel = new JLabel(_value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 28f));

        JLabel suffixLabel = new JLabel(_valueSuffix);

        JLabel nameLabel = new JLabel(_statName);
        nameLabel.setForeground(Color.GRAY);
        nameLabel.setFont(nameLabel.getFont().deriveFont(11f));

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        add(valueLabel, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.SOUTHWEST;
        c.insets = new Insets(0, 2, 3, 0);
        add(suffixLabel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        add(nameLabel, c);
    }
}
