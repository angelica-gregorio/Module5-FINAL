package exercise2;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GRect;

public class MyButton extends GCompound {

    private static final String FONT = "SansSerif-bold-40";
    private GRect key;
    private GLabel keyText;

    public MyButton(double x, double y, double width, double height, String text) {
        key = new GRect(x, y, width, height);
        add(key);

        keyText = new GLabel(text);
        keyText.setFont(FONT);
        double labelWidth = keyText.getWidth();
        double labelHeight = keyText.getAscent();
        double centerX = getCenterX(width, labelWidth);
        double centerY = getCenterY(height, labelHeight);
        add(keyText, x + centerX, y + centerY);
    }

    public String getText() {
        return keyText.getLabel();
    }

    public void setText(String text) {
        keyText.setLabel(text);
        double width = key.getWidth();
        double labelWidth = keyText.getWidth();
        double labelHeight = keyText.getAscent();
        double centerX = getCenterX(width, labelWidth);
        double centerY = getCenterY(key.getHeight(), labelHeight);
        keyText.setLocation(key.getX() + centerX, key.getY() + centerY);
    }

    private double getCenterX(double width, double labelWidth) {
        return (width - labelWidth) / 2.0;
    }

    private double getCenterY(double height, double ascent) {
        return (ascent + (height - ascent) / 2.0);
    }
}
