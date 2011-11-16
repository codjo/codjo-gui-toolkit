/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 * Combo des mois.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class MonthComboBox extends JComboBox {
    private ActionListener userListener;
    private TableModelListener calendarListener =
          new TableModelListener() {
              public void tableChanged(TableModelEvent event) {
                  initLocalStuff();
              }
          };
    private CalendarModel calendarModel;
    private DateFormatSymbols dateSymbols;


    public void setCalendarModel(CalendarModel calendarModel) {
        this.calendarModel = calendarModel;
        initLocalStuff();
    }


    public CalendarModel getCalendarModel() {
        return calendarModel;
    }


    private void addListeners() {
        addActionListener(userListener);
        calendarModel.addTableModelListener(calendarListener);
    }


    private void initLocalStuff() {
        removeListeners();

        dateSymbols = new DateFormatSymbols(calendarModel.getLocale());
        setModel(new DefaultComboBoxModel(dateSymbols.getMonths()));
        setSelectedIndex(calendarModel.getMonth());

        addListeners();
    }


 


    private void removeListeners() {
        removeActionListener(userListener);
        calendarModel.removeTableModelListener(calendarListener);
    }


    public void setUserListener(ActionListener userListener) {
        this.userListener = userListener;
        addActionListener(this.userListener);
    }
}
