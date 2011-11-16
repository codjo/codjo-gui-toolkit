/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.number;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
/**
 * Interface décrivant un champs IHM numérique.
 *
 * @version $Revision: 1.7 $
 */
public interface NumberFieldInterface {
    void setBackground(Color color);


    void setForeground(Color color);


    void setFont(Font font);


    void setText(String txt);


    Number getNumber();


    void setBorder(Border border);
}
