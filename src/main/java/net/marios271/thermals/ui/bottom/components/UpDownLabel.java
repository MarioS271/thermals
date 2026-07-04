package net.marios271.thermals.ui.bottom.components;

import javax.swing.*;
import java.awt.*;

public class UpDownLabel extends JLabel {
    char _c;
    Color _activeColor;
    String _unit;
    int _value;

    public UpDownLabel(char c, Color activeColor, String unit, int initialValue) {
        _c = c;
        _activeColor = activeColor;
        _unit = unit;
        _value = initialValue;

        computeFgColor();
        setText();
    }

    public void setValue(int value) {
        _value = value;
        computeFgColor();
        setText();
    }

    private void computeFgColor() {
        if (_value == 0) {
            setForeground(Color.GRAY);
        } else {
            setForeground(_activeColor);
        }
    }

    private void setText() {
        if (_value == 0) {
            setText(_c + " Idle");
        } else {
            setText(_c + " " + _value + " " + _unit);
        }
    }
}
