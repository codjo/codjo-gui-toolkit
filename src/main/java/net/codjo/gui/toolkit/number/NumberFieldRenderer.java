/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.number;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import javax.swing.border.Border;
/**
 * Classe permettant de faire le rendue du nombre hors mode edition.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.5 $
 */
public class NumberFieldRenderer {
    private Color background;
    private Border border;
    private Font font;
    private Color foreground;
    private NumberFormat format;

    public NumberFieldRenderer(NumberFormat format, Font font, Color background,
        Color foreground, Border border) {
        this.setFormat(format);
        this.setFont(font);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setBorder(border);
    }


    public NumberFieldRenderer(NumberFormat format) {
        this(format, null, null, null, null);
    }


    public NumberFieldRenderer() {
        this(null, null, null, null, null);
    }

    public void setBackground(Color background) {
        this.background = background;
    }


    public void setBorder(Border border) {
        this.border = border;
    }


    public void setFont(Font font) {
        this.font = font;
    }


    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }


    public void setFormat(NumberFormat format) {
        this.format = format;
    }


    public Color getBackground() {
        return background;
    }


    public Border getBorder() {
        return border;
    }


    public Font getFont() {
        return font;
    }


    public Color getForeground() {
        return foreground;
    }


    public NumberFormat getFormat() {
        return format;
    }


    public void applyDisplayFormat(NumberFieldInterface field) {
        if (getFont() != null) {
            field.setFont(getFont());
        }

        if (getForeground() != null) {
            field.setForeground(getForeground());
        }

        if (getBackground() != null) {
            field.setBackground(getBackground());
        }

        if (getBorder() != null) {
            field.setBorder(getBorder());
        }

        updateGuiFrom(field.getNumber(), field);
    }


    public void updateGuiFrom(Number nb, NumberFieldInterface field) {
        field.setText(format(nb));
    }


    private String format(Number nb) {
        if (nb == null) {
            return "";
        }

        if (getFormat() != null) {
            return getFormat().format(nb);
        }
        else {
            return nb.toString();
        }
    }
}
