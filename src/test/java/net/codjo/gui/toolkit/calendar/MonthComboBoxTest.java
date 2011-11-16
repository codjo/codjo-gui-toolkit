/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MonthComboBoxTest {
    private final Date february2002 = java.sql.Date.valueOf("2002-02-01");


    @Test
    public void testCalendarModelChange() {
        MonthComboBox monthcombobox = new MonthComboBox();
        CalendarModel calendarModel1 = new CalendarModel(Locale.FRENCH, february2002);
        monthcombobox.setCalendarModel(calendarModel1);

        calendarModel1.setMonth(Calendar.JANUARY);
        assertEquals(Calendar.JANUARY, monthcombobox.getSelectedIndex());
        assertEquals("janvier", monthcombobox.getSelectedItem());
    }


    @Test
    public void testComboBoxChange() {
        JCalendar cal = new JCalendar();
        MonthComboBox monthcombobox = cal.getMonthComboBox();
        monthcombobox.getCalendarModel().setDate(java.sql.Date.valueOf("2008-08-08"));
        monthcombobox.setSelectedIndex(Calendar.JANUARY);

        assertEquals(Calendar.JANUARY, monthcombobox.getSelectedIndex());
        assertEquals(java.sql.Date.valueOf("2008-01-01"), cal.getSelectedDate());
    }


    @Test
    public void testComboBoxChangeOnJanuary31() {
        final MonthComboBox comboBox = new MonthComboBox();

        final CalendarModel model =
              new CalendarModel(Locale.FRENCH, java.sql.Date.valueOf("2005-01-31"));

        comboBox.setUserListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.setMonth(comboBox.getSelectedIndex());
            }
        });
        comboBox.setCalendarModel(model);
        comboBox.setSelectedIndex(Calendar.APRIL);

        assertEquals(Calendar.APRIL, comboBox.getSelectedIndex());
        assertEquals(Calendar.APRIL, comboBox.getCalendarModel().getMonth());
    }


    @Test
    public void test_setCalendarModel_ENGLISH() {
        MonthComboBox monthcombobox = new MonthComboBox();
        CalendarModel calendarModel1 = new CalendarModel(Locale.ENGLISH, february2002);
        monthcombobox.setCalendarModel(calendarModel1);

        assertEquals(13, monthcombobox.getModel().getSize());
        assertEquals(Calendar.FEBRUARY, monthcombobox.getSelectedIndex());
        assertEquals("February", monthcombobox.getSelectedItem());
    }


    @Test
    public void test_setCalendarModel_FRENCH() {
        MonthComboBox monthcombobox = new MonthComboBox();

        assertEquals(0, monthcombobox.getModel().getSize());

        CalendarModel calendarModel1 = new CalendarModel(Locale.FRENCH, february2002);
        monthcombobox.setCalendarModel(calendarModel1);

        assertEquals(13, monthcombobox.getModel().getSize());
        assertEquals(Calendar.FEBRUARY, monthcombobox.getSelectedIndex());
        assertEquals("février", monthcombobox.getSelectedItem());
    }
}
