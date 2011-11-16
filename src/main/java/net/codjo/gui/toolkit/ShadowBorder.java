/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
/**
 * Bordure avec un effet ombré.
 */
public class ShadowBorder extends AbstractBorder {
    private static final Insets INSETS = new Insets(1, 1, 3, 3);
    private Color shadow;
    private final Color lightShadow;
    private final Color lighterShadow;


    public ShadowBorder() {
        shadow = UIManager.getColor("controlShadow");
        if (shadow == null) {
            shadow = Color.gray;
        }
        lightShadow =
              new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 170);
        lighterShadow =
              new Color(shadow.getRed(), shadow.getGreen(), shadow.getBlue(), 70);
    }


    @Override
    public Insets getBorderInsets(Component component) {
        return INSETS;
    }


    /**
     * @noinspection MethodParameterNamingConvention
     */
    @Override
    public void paintBorder(Component component, Graphics graphics, int x, int y, int w,
                            int h) {
        graphics.translate(x, y);

        graphics.setColor(shadow);
        graphics.fillRect(0, 0, w - 3, 1);
        graphics.fillRect(0, 0, 1, h - 3);
        graphics.fillRect(w - 3, 1, 1, h - 3);
        graphics.fillRect(1, h - 3, w - 3, 1);

        graphics.setColor(lightShadow);
        graphics.fillRect(w - 3, 0, 1, 1);
        graphics.fillRect(0, h - 3, 1, 1);
        graphics.fillRect(w - 2, 1, 1, h - 3);
        graphics.fillRect(1, h - 2, w - 3, 1);

        graphics.setColor(lighterShadow);
        graphics.fillRect(w - 2, 0, 1, 1);
        graphics.fillRect(0, h - 2, 1, 1);
        graphics.fillRect(w - 2, h - 2, 1, 1);
        graphics.fillRect(w - 1, 1, 1, h - 2);
        graphics.fillRect(1, h - 1, w - 2, 1);
        graphics.translate(-x, -y);
    }
}
