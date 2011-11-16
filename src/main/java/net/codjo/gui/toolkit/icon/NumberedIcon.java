package net.codjo.gui.toolkit.icon;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
/**
 *
 */
public class NumberedIcon implements Icon {
    private final ImageIcon icon;
    private final String numberStr;


    public NumberedIcon(ImageIcon icon, int number) {
        this.icon = icon;
        numberStr = String.valueOf(number);
    }


    public void paintIcon(Component component, Graphics graphics, int xPos, int yPos) {
        Font oldFont = graphics.getFont();
        graphics.setFont(oldFont.deriveFont(Font.PLAIN, 10));

        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();

        int xNumber = xPos + iconWidth;
        int yNumber = yPos + iconHeight - 4;

        icon.paintIcon(component, graphics, xPos, yPos);
        graphics.setColor(component.getBackground().darker());
        graphics.drawString(numberStr, xNumber, yNumber);
        graphics.setFont(oldFont);
    }


    public int getIconWidth() {
        return icon.getIconWidth() + 10;
    }


    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
