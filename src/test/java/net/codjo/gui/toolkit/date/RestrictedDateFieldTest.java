package net.codjo.gui.toolkit.date;

import static net.codjo.gui.toolkit.date.AbstractDateField.BAD_DATE;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import junit.extensions.jfcunit.JFCTestCase;

public class RestrictedDateFieldTest extends JFCTestCase {

    public void test_invalidDates() throws Exception {
        List<Date> invalidDates = new ArrayList<Date>();
        invalidDates.add(java.sql.Date.valueOf("2001-12-25"));
        RestrictedDateField dateField = new RestrictedDateField(true, true, invalidDates);

        java.sql.Date newDate = java.sql.Date.valueOf("2001-12-24");
        dateField.setDate(newDate);
        flushAWT();
        assertEquals(newDate, new java.sql.Date(dateField.buildDateValueFromGUI().getTime()));

        newDate = java.sql.Date.valueOf("2001-12-25");
        dateField.setDate(newDate);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());
    }


    public void test_allowWeekEnds() throws Exception {
        RestrictedDateField dateField = new RestrictedDateField(true, false);

        java.sql.Date newDate = java.sql.Date.valueOf("2009-07-24");    // Friday
        dateField.setDate(newDate);
        flushAWT();
        assertEquals(newDate, new java.sql.Date(dateField.buildDateValueFromGUI().getTime()));

        newDate = java.sql.Date.valueOf("2009-07-25");  // Saturday
        dateField.setDate(newDate);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());

        newDate = java.sql.Date.valueOf("2009-07-26");  // Sunday
        dateField.setDate(newDate);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());
    }


    public void test_allowPastDate() throws Exception {
        RestrictedDateField dateField = new RestrictedDateField(true, true);

        Calendar calendar = Calendar.getInstance();
        java.sql.Date todayDate = getSQLDateFromCalendar(calendar);
        dateField.setDate(todayDate);
        flushAWT();
        assertEquals(todayDate, dateField.buildDateValueFromGUI());

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        java.sql.Date yesterday = getSQLDateFromCalendar(calendar);
        dateField.setDate(yesterday);
        flushAWT();
        assertEquals(yesterday, dateField.buildDateValueFromGUI());

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        java.sql.Date tomorrow = getSQLDateFromCalendar(calendar);
        dateField.setDate(tomorrow);
        flushAWT();
        assertEquals(tomorrow, dateField.buildDateValueFromGUI());
    }


    public void test_noAllowPastDate() throws Exception {
        RestrictedDateField dateField = new RestrictedDateField(false, true);

        Calendar calendar = Calendar.getInstance();
        java.sql.Date today = getSQLDateFromCalendar(calendar);
        dateField.setDate(today);
        flushAWT();
        assertEquals(today, dateField.buildDateValueFromGUI());

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        java.sql.Date yesterday = getSQLDateFromCalendar(calendar);
        dateField.setDate(yesterday);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        java.sql.Date tomorrow = getSQLDateFromCalendar(calendar);
        dateField.setDate(tomorrow);
        flushAWT();
        assertEquals(tomorrow, dateField.buildDateValueFromGUI());

        java.sql.Date maxPastDate = java.sql.Date.valueOf("2009-06-30");
        dateField.setMaxDateRange(maxPastDate);

        dateField.setDate(maxPastDate);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());

        dateField.setDate(java.sql.Date.valueOf("2009-06-29"));
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());

        java.sql.Date firstDayJuly = java.sql.Date.valueOf("2009-07-01");
        dateField.setDate(firstDayJuly);
        flushAWT();
        assertEquals(BAD_DATE, dateField.buildDateValueFromGUI());
        dateField.setAllowPastDate(true);
        assertEquals(firstDayJuly, dateField.buildDateValueFromGUI());
    }


    public void test_clearExcludedDateRanges() throws Exception {
        RestrictedDateField field = new RestrictedDateField();

        field.addExcludedDateRange(java.sql.Date.valueOf("2009-01-01"), java.sql.Date.valueOf("2010-01-01"));
        assertEquals(field.getExcludedDateRanges().size(), 1);

        field.clearExcludedDateRanges();
        assertEquals(field.getExcludedDateRanges().size(), 0);

        field.addExcludedDateRange(java.sql.Date.valueOf("2009-01-01"), java.sql.Date.valueOf("2010-01-01"));
        assertEquals(field.getExcludedDateRanges().size(), 1);
    }


    public void test_isInvalidDate() {
        RestrictedDateField field = new RestrictedDateField();

        List<Date> list = new ArrayList<Date>();
        list.add(java.sql.Date.valueOf("2009-01-01"));
        list.add(java.sql.Date.valueOf("2010-01-01"));
        field.setInvalidDate(list);

        assertEquals(field.isInvalidDate(java.sql.Date.valueOf("2009-06-01")), false);
        assertEquals(field.isInvalidDate(java.sql.Date.valueOf("2009-01-01")), true);
        assertEquals(field.isInvalidDate(java.sql.Date.valueOf("2010-01-01")), true);

        assertEquals(field.isInvalidDate(java.sql.Date.valueOf("2011-06-01")), false);
        assertEquals(field.isInvalidDate(java.sql.Date.valueOf("2008-06-01")), false);
        assertEquals(field.isInvalidDate(RestrictedDateField.MAX_DATE), false);
        assertEquals(field.isInvalidDate(RestrictedDateField.MIN_DATE), false);
    }


    public void test_isOutOfRangeDate() {
        RestrictedDateField field = new RestrictedDateField();

        field.addExcludedDateRange(java.sql.Date.valueOf("2009-01-01"), java.sql.Date.valueOf("2010-01-01"));
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2009-06-01")), true);
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2009-01-01")), true);
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2010-01-01")), true);

        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2011-06-01")), false);
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2008-06-01")), false);
        assertEquals(field.containsExcludedDate(RestrictedDateField.MAX_DATE), false);
        assertEquals(field.containsExcludedDate(RestrictedDateField.MIN_DATE), false);
    }


    public void test_isOutOfRangeDateWithMinDate() {
        RestrictedDateField field = new RestrictedDateField();

        field.setMaxDateRange(java.sql.Date.valueOf("2009-01-01"));
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2008-06-01")), true);
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2009-01-01")), true);
        assertEquals(field.containsExcludedDate(java.sql.Date.valueOf("2010-01-01")), false);
        assertEquals(field.containsExcludedDate(RestrictedDateField.MAX_DATE), false);
        assertEquals(field.containsExcludedDate(RestrictedDateField.MIN_DATE), true);
    }


    private java.sql.Date getSQLDateFromCalendar(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return java.sql
              .Date
              .valueOf(year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day);
    }
}
