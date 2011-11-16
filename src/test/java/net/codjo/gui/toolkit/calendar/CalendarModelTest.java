/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import junit.framework.TestCase;
/**
 * Class de test de <code>CalendarModel</code>
 *
 * @author Boris
 * @version $Revision: 1.7 $
 */
public class CalendarModelTest extends TestCase {
    private final Date april2002 = java.sql.Date.valueOf("2002-04-01");
    private final Date february2002 = java.sql.Date.valueOf("2002-02-01");
    private final Date march2000 = java.sql.Date.valueOf("2000-03-01");
    private final Date march2002 = java.sql.Date.valueOf("2002-03-01");
    private final Date october2002 = java.sql.Date.valueOf("2002-10-01");


    public void test_getColumnName() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE);
        assertEquals("lun.", calendarmodel.getColumnName(0));
        assertEquals("dim.", calendarmodel.getColumnName(6));
    }


    public void test_getColumnName_ENGLISH() {
        CalendarModel calendarmodel = new CalendarModel(Locale.ENGLISH);
        assertEquals("Sun", calendarmodel.getColumnName(0));
        assertEquals("Sat", calendarmodel.getColumnName(6));
    }


    public void test_getDayIndex() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE);
        int inc = 0;
        assertEquals("MONDAY", Calendar.MONDAY, calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("TUESDAY", Calendar.TUESDAY,
                     calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("WEDNESDAY", Calendar.WEDNESDAY,
                     calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("THURSDAY", Calendar.THURSDAY,
                     calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("FRIDAY", Calendar.FRIDAY, calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("SATURDAY", Calendar.SATURDAY,
                     calendarmodel.getDayOfWeekFromIndex(inc++));
        assertEquals("SUNDAY", Calendar.SUNDAY, calendarmodel.getDayOfWeekFromIndex(inc));
    }


    public void test_getDayIndex_ENGLISH() {
        CalendarModel calendarmodel = new CalendarModel(Locale.ENGLISH);
        assertEquals("SUNDAY", Calendar.SUNDAY, calendarmodel.getDayOfWeekFromIndex(0));
    }


    public void test_getRowCount() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE, february2002);
        assertEquals(5, calendarmodel.getRowCount());
    }


    public void test_getRowCount_March2002_FRANCE() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE, march2002);
        assertEquals(5, calendarmodel.getRowCount());
    }


    public void test_getRowCount_March2000_ENGLISH() {
        CalendarModel calendarmodel = new CalendarModel(Locale.ENGLISH, march2000);
        assertEquals(5, calendarmodel.getRowCount());
    }


    public void test_getValueAt() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE, february2002);
        assertEquals(february2002, calendarmodel.getValueAt(0, 4));
    }


    public void test_getValueAt_ENGLISH() {
        CalendarModel calendarmodel = new CalendarModel(Locale.ENGLISH, february2002);
        assertEquals(february2002, calendarmodel.getValueAt(0, 5));
    }


    public void test_getValueAt_april() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE, april2002);
        assertEquals(april2002, calendarmodel.getValueAt(0, 0));
    }


    public void test_getValueAt_october() {
        CalendarModel calendarmodel = new CalendarModel(Locale.FRANCE, october2002);
        assertEquals(october2002, calendarmodel.getValueAt(0, 1));
    }


    public void test_setDate_Bug() {
        CalendarModel calendarmodel = new CalendarModel();
        final java.sql.Date aDate = java.sql.Date.valueOf("2003-12-03");
        calendarmodel.setDate(aDate);
        assertEquals(aDate, calendarmodel.getDate());
    }


    public void test_setDate__noTime() {
        CalendarModel calendarmodel = new CalendarModel();
        final java.sql.Date aDate = java.sql.Date.valueOf("2003-12-03");
        final Date aDateWithTime = java.sql.Timestamp.valueOf("2003-12-03 11:20:05.5");
        calendarmodel.setDate(aDateWithTime);
        assertEquals(aDate, calendarmodel.getDate());
    }
}
