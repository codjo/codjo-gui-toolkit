/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
/**
 * Model de calendrier.
 *
 * @author Boris
 * @version $Revision: 1.11 $
 */
public class CalendarModel extends AbstractTableModel {
    private Date[] cells = {};
    private int forcedRowCount = 0;
    private transient PropertyChangeSupport propertyChangeListeners =
        new PropertyChangeSupport(this);
    private Calendar calendar;
    private Date calendarDate;
    private DateFormatSymbols dateSymbols;
    private Locale locale;

    public CalendarModel() {
        this(Locale.FRANCE);
    }


    public CalendarModel(Locale locale) {
        this(locale, new Date());
    }


    public CalendarModel(Locale locale, Date date) {
        this.locale = locale;
        init(date);
    }

    public void setDate(Date aDate) {
        if (aDate == null) {
            init(new Date());
        }
        else {
            init(aDate);
        }
    }


    public void setForcedRowCount(int forcedRowCount) {
        this.forcedRowCount = forcedRowCount;
        fillCells();
    }


    public void setLocale(Locale locale) {
        Locale old = this.locale;
        this.locale = locale;
        propertyChangeListeners.firePropertyChange("locale", old, locale);
        init(calendarDate);
    }


    public void setMonth(int month) {
        calendar.setTime(calendarDate);

        // Lorsqu'on change de mois, il faut se mettre au premier jour du mois
        // pour que set MONTH ne fasse pas passer au mois suivant si le jour n'existe pas.
        // Exemple : on est le 31 janvier, on veut passer à avril, mais comme il n'y a pas de
        // 31 avril le Calendar fait passer directement au premier mai.
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        calendar.set(Calendar.MONTH, month);
        setDate(calendar.getTime());
    }


    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}


    public void setYear(int year) {
        calendar.setTime(calendarDate);
        calendar.set(Calendar.YEAR, year);
        setDate(calendar.getTime());
    }


    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    public Class getColumnClass(int columnIndex) {
        return Date.class;
    }


    public int getColumnCount() {
        return calendar.getMaximum(Calendar.DAY_OF_WEEK);
    }


    public String getColumnName(int columnIndex) {
        String[] weekDays = dateSymbols.getShortWeekdays();
        return weekDays[getDayOfWeekFromIndex(columnIndex)];
    }


    public Date getDate() {
        return calendarDate;
    }


    public int getDayOfWeekFromIndex(int columnIndex) {
        int cols = getColumnCount();
        if (columnIndex + calendar.getFirstDayOfWeek() <= cols) {
            return columnIndex + calendar.getFirstDayOfWeek();
        }
        else {
            return (columnIndex + calendar.getFirstDayOfWeek()) % cols;
        }
    }


    public int getForcedRowCount() {
        return forcedRowCount;
    }


    public Locale getLocale() {
        return locale;
    }


    public int getMonth() {
        calendar.setTime(calendarDate);
        return calendar.get(Calendar.MONTH);
    }


    public int getRowCount() {
        return cells.length / getColumnCount();
    }


    public Object getValueAt(int rowIndex, int columnIndex) {
//        try {
        return cells[rowIndex * getColumnCount() + columnIndex];
//        }
//        catch (ArrayIndexOutOfBoundsException ex) {
//            return null;
//        }
    }


    public int getYear() {
        calendar.setTime(calendarDate);
        return calendar.get(Calendar.YEAR);
    }


    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.addPropertyChangeListener(listener);
    }


    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.removePropertyChangeListener(listener);
    }


    private void fillCells() {
        ArrayList list = new ArrayList();

        // Remplissage avant debut du mois
        calendar.setTime(calendarDate);
        resetTime(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek()) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
            list.add(0, calendar.getTime());
        }

        // Remplissage du mois
        calendar.setTime(calendarDate);
        resetTime(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int month = calendar.get(Calendar.MONTH);
        while (calendar.get(Calendar.MONTH) == month) {
            list.add(list.size(), calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        // Remplissage apres la fin du mois
        if (list.size() % getColumnCount() != 0) {
            int row = list.size() / getColumnCount() + 1;
            while (list.size() < row * getColumnCount()) {
                list.add(list.size(), calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        // Remplissage si forcé
        if (forcedRowCount > (list.size() / getColumnCount())) {
            while (list.size() < forcedRowCount * getColumnCount()) {
                list.add(list.size(), calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        cells = (Date[])list.toArray(new Date[] {});

        fireTableStructureChanged();
    }


    private void init(Date date) {
        calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        resetTime(calendar);
        this.calendarDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        dateSymbols = new DateFormatSymbols(locale);
        fillCells();
    }


    private static void resetTime(final Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
}
