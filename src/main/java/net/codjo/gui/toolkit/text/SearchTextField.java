package net.codjo.gui.toolkit.text;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

public class SearchTextField extends JTextField {
    private ImageIcon imageIcon;
    private static final int ARC_SIZE = 25;
    private static final Color FROM_COLOR = Color.GRAY;
    private static final Color TO_COLOR = new Color(220, 220, 220);


    public SearchTextField() {
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(3, 20, 3, 10));
        setColumns(10);
        imageIcon = new ImageIcon(getClass().getResource("search.gif"));
    }


    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D)graphics;

        RenderingHints hints = graphics2D.getRenderingHints();
        boolean antialiasOn = hints.containsValue(RenderingHints.VALUE_ANTIALIAS_ON);
        if (!antialiasOn) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        int width = getWidth();
        int height = getHeight();
        Paint storedPaint = graphics2D.getPaint();

        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRoundRect(0, 0, width, height, ARC_SIZE, ARC_SIZE);

        if (isEnabled()) {
            graphics2D.setPaint(new GradientPaint(width / 2, 0, FROM_COLOR,
                                                  width / 2, height / 2, TO_COLOR));
        }
        else {
            graphics2D.setColor(TO_COLOR);
        }

        graphics2D.setStroke(new BasicStroke(1.7f));
        graphics2D.drawRoundRect(0, 0, width - 1, height - 1, ARC_SIZE, ARC_SIZE);

        graphics2D.setPaint(storedPaint);

        if (!isEnabled()) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
        }

        imageIcon.paintIcon(this, graphics2D, 3, 3);

        super.paintComponent(graphics);

        if (!antialiasOn) {
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
    }
}
