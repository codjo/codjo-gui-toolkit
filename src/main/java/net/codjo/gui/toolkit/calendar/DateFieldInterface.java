/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.util.Date;
import javax.swing.JButton;
/**
 * Interface designant un champs texte.
 *
 * @version $Revision: 1.6 $
 */
public interface DateFieldInterface {
    Date getDate();


    void setDate(Date newDate);


    JButton getCalendarButton();
}
