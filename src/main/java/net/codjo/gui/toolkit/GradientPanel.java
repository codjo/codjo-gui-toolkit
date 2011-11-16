/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import javax.swing.JPanel;
import javax.swing.UIManager;
/**
 * Panel avec un dégradé de couleur en fond.
 */
public class GradientPanel extends JPanel {
    private Color endColor = getDefaultEndColor();


    public GradientPanel() {
        setStartColor(getDefaultStartColor());
    }


    public GradientPanel(LayoutManager layout) {
        super(layout);
        setStartColor(getDefaultStartColor());
    }


    public static Color getDefaultEndColor() {
        return UIManager.getColor("control");
    }


    public static Color getDefaultStartColor() {
        return UIManager.getColor("InternalFrame.activeTitleBackground");
    }


    public Color getEndColor() {
        return endColor;
    }


    public Color getStartColor() {
        return getBackground();
    }


    public void setStartColor(Color startColor) {
        setBackground(startColor);
    }


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (!isOpaque()) {
            return;
        }
        Color control = getEndColor();
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2Graphics2D = (Graphics2D)graphics;
        Paint storedPaint = g2Graphics2D.getPaint();
        g2Graphics2D.setPaint(new GradientPaint(0, 0, getBackground(), width, 0, control));
        g2Graphics2D.fillRect(0, 0, width, height);
        g2Graphics2D.setPaint(storedPaint);
    }
}
