/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import net.codjo.gui.toolkit.calendar.CalendarHelper;
import net.codjo.gui.toolkit.calendar.JCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
/**
 * DateField spécifique gérant les valeurs dites 'nulles' c'est à dire n'affichant rien lorsque la valeur est
 * égale à une date définie (31-12-9999 par défaut).
 */
public class NoNullDateField extends DateField {
    public static final Date DEFAULT_NO_NULL_DATE =
          new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31).getTime();

    private Date noNullDate = DEFAULT_NO_NULL_DATE;


    public NoNullDateField() {
        this(true);
        setDate(noNullDate);
    }


    public NoNullDateField(boolean showCalendar) {
        this(showCalendar, new JButton());
    }


    public NoNullDateField(boolean showCalendar, JButton calendarButton) {
        super(showCalendar, null, calendarButton);
        setCalendarHelper(new NoNullCalendar());
    }


    public Date getDate() {
        if (super.getDate() == null) {
            return noNullDate;
        }
        return super.getDate();
    }


    public void setDate(final Date newDate) {
        if (noNullDate.equals(newDate)) {
            super.setDate(null);
            return;
        }
        super.setDate(newDate);
    }


    public Date getNoNullDate() {
        return noNullDate;
    }


    public void setNoNullDate(Date newNoNullDate) {
        this.noNullDate = newNoNullDate;
    }


    private class NoNullCalendar extends CalendarHelper {
        protected JCalendar newCalendar(final Date selectedDate) {
            if (noNullDate.equals(selectedDate)) {
                final JCalendar calendar = new JCalendar();
                calendar.setSelectedDate(new Date());
                return calendar;
            }
            return super.newCalendar(selectedDate);
        }
    }
}
