package net.marios271.thermals.ui.bottom.components;

import net.marios271.thermals.Helpers;

import javax.swing.*;
import java.awt.*;

public class UpDownLabel extends JLabel {
    private char _c;
    private Color _activeColor;
    private String _unit;
    private double _value;

    public UpDownLabel(char c, Color activeColor, String unit, double initialValue) {
        _c = c;
        _activeColor = activeColor;
        _unit = unit;
        _value = initialValue;

        computeFgColor();
        setText();
    }

    public void setValue(double value) {
        _value = value;
        computeFgColor();
        setText();
    }

    private void computeFgColor() {
        if (_value == 0.0) {
            setForeground(Color.GRAY);
        } else {
            setForeground(_activeColor);
        }
    }

    private void setText() {
        setText(_c + " " + Helpers.doubleAsSinglePrecisionString(_value) + " " + _unit);
    }
}
