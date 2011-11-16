package net.codjo.gui.toolkit.text;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class AntialiasedJLabel extends JLabel {
    public AntialiasedJLabel() {
    }


    public AntialiasedJLabel(String text) {
        super(text);
    }


    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D)graphics;

        Object previous = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        super.paint(graphics);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, previous);
    }
}
