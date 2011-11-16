/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;
import junit.framework.TestCase;
/**
 * Class de test de <code>YearComboBox</code>.
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
public class YearComboBoxTest extends TestCase {
    private final Date february2002 = java.sql.Date.valueOf("2002-02-01");


    public void testUserSelect() {
        YearComboBox yearcombobox = new YearComboBox();
        CalendarModel calendarModel = new CalendarModel(Locale.FRENCH, february2002);
        yearcombobox.setCalendarModel(calendarModel);

        yearcombobox.setSelectedItem("2004");

        assertEquals(500, yearcombobox.getModel().getSize());
        assertEquals(7, yearcombobox.getSelectedIndex());

        assertEquals("1997", yearcombobox.getItemAt(0));
        assertEquals("1998", yearcombobox.getItemAt(1));
        assertEquals("1999", yearcombobox.getItemAt(2));
        assertEquals("2000", yearcombobox.getItemAt(3));
        assertEquals("2001", yearcombobox.getItemAt(4));
        assertEquals("2002", yearcombobox.getItemAt(5));
        assertEquals("2003", yearcombobox.getItemAt(6));
        assertEquals("2004", yearcombobox.getItemAt(7));
        assertEquals("2005", yearcombobox.getItemAt(8));
        assertEquals("2006", yearcombobox.getItemAt(9));
    }


    public void test_setByUser() {
        YearComboBox yearcombobox = new YearComboBox();
        CalendarModel calendarModel = new CalendarModel(Locale.FRENCH, february2002);
        yearcombobox.setCalendarModel(calendarModel);

        yearcombobox.setSelectedItem("1999");
        assertEquals(500, yearcombobox.getModel().getSize());
        assertEquals(2, yearcombobox.getSelectedIndex());

        assertEquals("1999", yearcombobox.getItemAt(2));
    }


    public void test_setByUser_nullValue() {
        final YearComboBox yearcombobox = new YearComboBox();
        final CalendarModel calendarModel = new CalendarModel(Locale.FRENCH, february2002);
        yearcombobox.setCalendarModel(calendarModel);
        yearcombobox.setUserListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    calendarModel.setYear(Integer.parseInt((String)yearcombobox.getSelectedItem()));
                }
                catch (NumberFormatException ex) {
                    calendarModel.setYear(calendarModel.getYear());
                }
            }
        });

        yearcombobox.setSelectedItem("");
        assertEquals(500, yearcombobox.getModel().getSize());
        assertEquals(5, yearcombobox.getSelectedIndex());

        assertEquals("2002", yearcombobox.getItemAt(5));
    }


    public void test_setCalendarModel() {
        YearComboBox yearcombobox = new YearComboBox();
        CalendarModel calendarModel = new CalendarModel(Locale.FRENCH, february2002);
        yearcombobox.setCalendarModel(calendarModel);

        assertEquals(500, yearcombobox.getModel().getSize());
        assertEquals(5, yearcombobox.getSelectedIndex());

        assertEquals("2000", yearcombobox.getItemAt(3));
        assertEquals("2001", yearcombobox.getItemAt(4));
        assertEquals("2002", yearcombobox.getItemAt(5));
        assertEquals("2003", yearcombobox.getItemAt(6));
        assertEquals("2004", yearcombobox.getItemAt(7));
    }


    protected void setUp() {
    }


    protected void tearDown() {
    }
}
