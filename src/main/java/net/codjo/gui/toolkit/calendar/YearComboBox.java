/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 * Combo des années.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class YearComboBox extends JComboBox {
    private TableModelListener calendarListener =
        new TableModelListener() {
            public void tableChanged(TableModelEvent event) {
                initLocalStuff();
            }
        };

    private ActionListener userListener ;

    private DefaultComboBoxModel yearModel = new DefaultComboBoxModel();
    private CalendarModel calendarModel;

    public YearComboBox() {
        setModel(yearModel);
        setEditable(true);
    }

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

        yearModel.removeAllElements();
        int year = calendarModel.getYear() - 5;
        for (int i = 0; i < 500; i++) {
            yearModel.addElement(Integer.toString(year + i));
        }
        setSelectedItem(Integer.toString(calendarModel.getYear()));

        addListeners();
    }


    private void removeListeners() {
        removeActionListener(userListener);
        calendarModel.removeTableModelListener(calendarListener);
    }


   

     public void setUserListener(ActionListener userListener) {
        this.userListener = userListener;
         addActionListener( this.userListener);
    }
}
