package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import net.codjo.gui.toolkit.util.GuiUtil;
import net.codjo.test.common.LogString;
import org.uispec4j.UISpecTestCase;
/**
 *
 */

public class JCalendarYearTest extends UISpecTestCase {
    private static final Color BLACK = GuiUtil.DEFAULT_BLACK_COLOR;
    private static final Color HOLIDAY_COLOR = JCalendarYear.HOLIDAY_FOREGROUND;


    public void test_nominal() throws Exception {
        JCalendarYear calendarYear = new JCalendarYear();
        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendarYear);

        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.ENGLISH);
        assertTrue(gui.getTextBox("MonthLabel_0").textEquals("January 2012"));
        assertTrue(gui.getTextBox("MonthLabel_1").textEquals("February 2012"));
        assertTrue(gui.getTextBox("MonthLabel_2").textEquals("March 2012"));
        assertTrue(gui.getTextBox("MonthLabel_3").textEquals("April 2012"));
        assertTrue(gui.getTextBox("MonthLabel_4").textEquals("May 2012"));
        assertTrue(gui.getTextBox("MonthLabel_5").textEquals("June 2012"));
        assertTrue(gui.getTextBox("MonthLabel_6").textEquals("July 2012"));
        assertTrue(gui.getTextBox("MonthLabel_7").textEquals("August 2012"));
        assertTrue(gui.getTextBox("MonthLabel_8").textEquals("September 2012"));
        assertTrue(gui.getTextBox("MonthLabel_9").textEquals("October 2012"));
        assertTrue(gui.getTextBox("MonthLabel_10").textEquals("November 2012"));
        assertTrue(gui.getTextBox("MonthLabel_11").textEquals("December 2012"));
    }


    public void test_holidays() throws Exception {
        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.FRENCH);

        List<Date> holidays = new ArrayList<Date>();
        holidays.add(java.sql.Date.valueOf("2012-01-02"));
        holidays.add(java.sql.Date.valueOf("2012-01-05"));
        holidays.add(java.sql.Date.valueOf("2012-02-02"));
        holidays.add(java.sql.Date.valueOf("2012-03-02"));
        holidays.add(java.sql.Date.valueOf("2012-04-02"));
        holidays.add(java.sql.Date.valueOf("2012-05-02"));
        holidays.add(java.sql.Date.valueOf("2012-06-04"));
        holidays.add(java.sql.Date.valueOf("2012-07-02"));
        holidays.add(java.sql.Date.valueOf("2012-08-02"));
        holidays.add(java.sql.Date.valueOf("2012-09-04"));
        holidays.add(java.sql.Date.valueOf("2012-10-02"));
        holidays.add(java.sql.Date.valueOf("2012-11-02"));
        holidays.add(java.sql.Date.valueOf("2012-12-04"));

        // we put several times the same date in the list
        holidays.add(java.sql.Date.valueOf("2012-01-02"));
        holidays.add(java.sql.Date.valueOf("2012-09-04"));

        // we put a date with another year
        holidays.add(java.sql.Date.valueOf("2011-01-03"));

        calendarYear.setHolidays(holidays);


        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendarYear);
        assertFalse(gui.getTable("Calendar_0").isEnabled());
        assertTrue(gui.getTable("Calendar_0").rowEquals(0, new Object[]{"26", "27", "28", "29", "30", "31", "1"}));
        assertTrue(gui.getTable("Calendar_0").rowEquals(1, new Object[]{"2", "3", "4", "5", "6", "7", "8"}));
        assertTrue(gui.getTable("Calendar_0").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {HOLIDAY_COLOR, BLACK, BLACK, HOLIDAY_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_1").isEnabled());
        assertTrue(gui.getTable("Calendar_1").rowEquals(0, new Object[]{"30", "31", "1", "2", "3", "4", "5"}));
        assertTrue(gui.getTable("Calendar_1").rowEquals(1, new Object[]{"6", "7", "8", "9", "10", "11", "12"}));
        assertTrue(gui.getTable("Calendar_1").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, HOLIDAY_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_2").isEnabled());
        assertTrue(gui.getTable("Calendar_2").rowEquals(0, new Object[]{"27", "28", "29", "1", "2", "3", "4"}));
        assertTrue(gui.getTable("Calendar_2").rowEquals(1, new Object[]{"5", "6", "7", "8", "9", "10", "11"}));
        assertTrue(gui.getTable("Calendar_2").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, BLACK, HOLIDAY_COLOR, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_3").isEnabled());
        assertTrue(gui.getTable("Calendar_3").rowEquals(0, new Object[]{"26", "27", "28", "29", "30", "31", "1"}));
        assertTrue(gui.getTable("Calendar_3").rowEquals(1, new Object[]{"2", "3", "4", "5", "6", "7", "8"}));
        assertTrue(gui.getTable("Calendar_3").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {HOLIDAY_COLOR, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_4").isEnabled());
        assertTrue(gui.getTable("Calendar_4").rowEquals(0, new Object[]{"30", "1", "2", "3", "4", "5", "6"}));
        assertTrue(gui.getTable("Calendar_4").rowEquals(1, new Object[]{"7", "8", "9", "10", "11", "12", "13"}));
        assertTrue(gui.getTable("Calendar_4").foregroundEquals(new Object[][]{
              {Color.WHITE, BLACK, HOLIDAY_COLOR, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_5").isEnabled());
        assertTrue(gui.getTable("Calendar_5").rowEquals(0, new Object[]{"28", "29", "30", "31", "1", "2", "3"}));
        assertTrue(gui.getTable("Calendar_5").rowEquals(1, new Object[]{"4", "5", "6", "7", "8", "9", "10"}));
        assertTrue(gui.getTable("Calendar_5").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, BLACK, Color.lightGray, Color.lightGray},
              {HOLIDAY_COLOR, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_6").isEnabled());
        assertTrue(gui.getTable("Calendar_6").rowEquals(0, new Object[]{"25", "26", "27", "28", "29", "30", "1"}));
        assertTrue(gui.getTable("Calendar_6").rowEquals(1, new Object[]{"2", "3", "4", "5", "6", "7", "8"}));
        assertTrue(gui.getTable("Calendar_6").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {HOLIDAY_COLOR, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_7").isEnabled());
        assertTrue(gui.getTable("Calendar_7").rowEquals(0, new Object[]{"30", "31", "1", "2", "3", "4", "5"}));
        assertTrue(gui.getTable("Calendar_7").rowEquals(1, new Object[]{"6", "7", "8", "9", "10", "11", "12"}));
        assertTrue(gui.getTable("Calendar_7").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, HOLIDAY_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_8").isEnabled());
        assertTrue(gui.getTable("Calendar_8").rowEquals(0, new Object[]{"27", "28", "29", "30", "31", "1", "2"}));
        assertTrue(gui.getTable("Calendar_8").rowEquals(1, new Object[]{"3", "4", "5", "6", "7", "8", "9"}));
        assertTrue(gui.getTable("Calendar_8").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray, Color.lightGray},
              {BLACK, HOLIDAY_COLOR, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_9").isEnabled());
        assertTrue(gui.getTable("Calendar_9").rowEquals(0, new Object[]{"1", "2", "3", "4", "5", "6", "7"}));
        assertTrue(gui.getTable("Calendar_9").rowEquals(1, new Object[]{"8", "9", "10", "11", "12", "13", "14"}));
        assertTrue(gui.getTable("Calendar_9").foregroundEquals(new Object[][]{
              {BLACK, HOLIDAY_COLOR, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_10").isEnabled());
        assertTrue(gui.getTable("Calendar_10").rowEquals(0, new Object[]{"29", "30", "31", "1", "2", "3", "4"}));
        assertTrue(gui.getTable("Calendar_10").rowEquals(1, new Object[]{"5", "6", "7", "8", "9", "10", "11"}));
        assertTrue(gui.getTable("Calendar_10").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, BLACK, HOLIDAY_COLOR, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertFalse(gui.getTable("Calendar_11").isEnabled());
        assertTrue(gui.getTable("Calendar_11").rowEquals(0, new Object[]{"26", "27", "28", "29", "30", "1", "2"}));
        assertTrue(gui.getTable("Calendar_11").rowEquals(1, new Object[]{"3", "4", "5", "6", "7", "8", "9"}));
        assertTrue(gui.getTable("Calendar_11").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray, Color.lightGray},
              {BLACK, HOLIDAY_COLOR, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));
    }


    public void test_dateHighlighter() throws Exception {
        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.FRENCH);

        final List<Date> highlightDates = new ArrayList<Date>();
        highlightDates.add(java.sql.Date.valueOf("2012-01-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-01-05"));
        highlightDates.add(java.sql.Date.valueOf("2012-02-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-03-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-04-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-05-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-06-04"));
        highlightDates.add(java.sql.Date.valueOf("2012-07-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-08-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-09-04"));
        highlightDates.add(java.sql.Date.valueOf("2012-10-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-11-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-12-04"));

        // we put several times the same date in the list
        highlightDates.add(java.sql.Date.valueOf("2012-01-02"));
        highlightDates.add(java.sql.Date.valueOf("2012-09-04"));

        // we put a date with another year
        highlightDates.add(java.sql.Date.valueOf("2011-01-03"));

        calendarYear.setDateHighlighter(new DateHighlighter() {
            public boolean highlight(Date date) {
                return highlightDates.contains(date);
            }


            public Color getHighlightForeground() {
                return Color.BLUE;
            }


            public Color getHighlightBackground() {
                return Color.GREEN;
            }
        });

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendarYear);
        assertTrue(gui.getTable("Calendar_0").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {Color.BLUE, BLACK, BLACK, Color.BLUE, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_1").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, Color.BLUE, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_2").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, BLACK, Color.BLUE, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_3").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {Color.BLUE, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_4").foregroundEquals(new Object[][]{
              {Color.WHITE, BLACK, Color.BLUE, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_5").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, BLACK, Color.lightGray, Color.lightGray},
              {Color.BLUE, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_6").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {Color.BLUE, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_7").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, Color.BLUE, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_8").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray, Color.lightGray},
              {BLACK, Color.BLUE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_9").foregroundEquals(new Object[][]{
              {BLACK, Color.BLUE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_10").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, BLACK, Color.BLUE, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        assertTrue(gui.getTable("Calendar_11").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray, Color.lightGray},
              {BLACK, Color.BLUE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));
    }


    public void test_erroneousDates() throws Exception {
        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setYear("1600");
        calendarYear.setLocale(Locale.FRENCH);

        calendarYear.setHolidays(null);

        List<Date> holidays = new ArrayList<Date>();
        holidays.add(java.sql.Date.valueOf("2012-01-02"));
        holidays.add(null);
        holidays.add(java.sql.Date.valueOf("2012-01-04"));

        try {
            calendarYear.setHolidays(holidays);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("The holidays list contains null dates!!", e.getMessage());
        }
    }


    public void test_editionMode() {
        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setEditionMode(true);
        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.FRENCH);

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendarYear);

        List<Date> holidays = new ArrayList<Date>();
        holidays.add(java.sql.Date.valueOf("2012-01-02"));
        holidays.add(java.sql.Date.valueOf("2012-01-05"));

        calendarYear.setHolidays(holidays);

        assertTrue(gui.getTable("Calendar_0").rowEquals(0, new Object[]{"26", "27", "28", "29", "30", "31", "1"}));
        assertTrue(gui.getTable("Calendar_0").rowEquals(1, new Object[]{"2", "3", "4", "5", "6", "7", "8"}));
        assertTrue(gui.getTable("Calendar_0").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.lightGray},
              {HOLIDAY_COLOR, BLACK, BLACK, HOLIDAY_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        gui.getTable("Calendar_0").click(2, 3);
        gui.getTable("Calendar_1").click(2, 3);
        gui.getTable("Calendar_2").click(2, 3);
        gui.getTable("Calendar_3").click(2, 3);
        gui.getTable("Calendar_4").click(2, 3);
        gui.getTable("Calendar_5").click(2, 3);
        gui.getTable("Calendar_6").click(2, 3);
        gui.getTable("Calendar_7").click(2, 3);
        gui.getTable("Calendar_8").click(2, 3);
        gui.getTable("Calendar_9").click(2, 3);
        gui.getTable("Calendar_10").click(2, 3);
        gui.getTable("Calendar_11").click(2, 3);
        gui.getTable("Calendar_11").click(3, 3);
        gui.getTable("Calendar_11").click(4, 2);

        assertSelectedDates(new String[]{
              "2012-01-02",
              "2012-01-05",
              "2012-01-12",
              "2012-02-16",
              "2012-03-15",
              "2012-04-12",
              "2012-05-17",
              "2012-06-14",
              "2012-07-12",
              "2012-08-16",
              "2012-09-13",
              "2012-10-18",
              "2012-11-15",
              "2012-12-13",
              "2012-12-20",
              "2012-12-26"
        }, calendarYear);

        holidays.clear();
        holidays.add(java.sql.Date.valueOf("2012-02-23"));
        holidays.add(java.sql.Date.valueOf("2012-03-24"));

        calendarYear.setHolidays(holidays);
        assertSelectedDates(new String[]{"2012-02-23", "2012-03-24"}, calendarYear);

        holidays.clear();
        calendarYear.setHolidays(holidays);
        assertSelectedDates(new String[0], calendarYear);

        holidays.add(java.sql.Date.valueOf("2012-02-23"));
        holidays.add(java.sql.Date.valueOf("2012-03-24"));
        calendarYear.setHolidays(holidays);
        assertSelectedDates(new String[]{"2012-02-23", "2012-03-24"}, calendarYear);

        holidays.clear();
        holidays.add(java.sql.Date.valueOf("2012-05-10"));
        calendarYear.setHolidays(holidays);
        assertSelectedDates(new String[]{"2012-05-10"}, calendarYear);

        calendarYear.setHolidays(null);
        assertSelectedDates(new String[0], calendarYear);
    }


    public void test_dateSelectionListener() {
        LogString logString = new LogString();

        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setEditionMode(true);
        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.FRENCH);

        calendarYear.addDateSelectionListener(new DateSelectionListenerMock(logString));

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendarYear);

        gui.getTable("Calendar_0").click(2, 3);
        assertEquals("selectionChanged()", logString.getContent());

        gui.getTable("Calendar_1").click(0, 0);
        assertEquals("selectionChanged()", logString.getContent());

        gui.getTable("Calendar_1").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_2").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_3").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged(), selectionChanged()",
                     logString.getContent());

        logString.clear();

        gui.getTable("Calendar_4").click(2, 3);
        assertEquals("selectionChanged()", logString.getContent());

        gui.getTable("Calendar_5").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_6").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_7").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged(), selectionChanged()",
                     logString.getContent());

        logString.clear();

        gui.getTable("Calendar_8").click(2, 3);
        assertEquals("selectionChanged()", logString.getContent());

        gui.getTable("Calendar_9").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_10").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged()", logString.getContent());

        gui.getTable("Calendar_11").click(2, 3);
        assertEquals("selectionChanged(), selectionChanged(), selectionChanged(), selectionChanged()",
                     logString.getContent());

        logString.clear();

        gui.getTable("Calendar_11").click(3, 3);
        assertEquals("selectionChanged()", logString.getContent());

        gui.getTable("Calendar_11").click(4, 2);
        assertEquals("selectionChanged(), selectionChanged()", logString.getContent());
    }


    private void assertSelectedDates(String[] expectedDates, JCalendarYear calendarYear) {
        List<Date> dates = new ArrayList<Date>();
        for (String date : expectedDates) {
            dates.add(java.sql.Date.valueOf(date));
        }
        assertEquals(dates, calendarYear.getSelectedDates());
    }


    public static void main(String[] args) {
        JCalendarYear calendarYear = new JCalendarYear();
        calendarYear.setYear("2012");
        calendarYear.setLocale(Locale.FRENCH);

        List<Date> holidays = new ArrayList<Date>();
        holidays.add(java.sql.Date.valueOf("2012-01-02"));
        holidays.add(java.sql.Date.valueOf("2012-02-02"));
        holidays.add(java.sql.Date.valueOf("2012-03-02"));
        holidays.add(java.sql.Date.valueOf("2012-04-02"));
        holidays.add(java.sql.Date.valueOf("2012-05-02"));
        holidays.add(java.sql.Date.valueOf("2012-06-04"));
        holidays.add(java.sql.Date.valueOf("2012-07-02"));
        holidays.add(java.sql.Date.valueOf("2012-08-02"));
        holidays.add(java.sql.Date.valueOf("2012-09-04"));
        holidays.add(java.sql.Date.valueOf("2012-10-02"));
        holidays.add(java.sql.Date.valueOf("2012-11-02"));
        holidays.add(java.sql.Date.valueOf("2012-12-04"));
        calendarYear.setHolidays(holidays);

        JFrame frame = new JFrame("Test Renderer");
        frame.getContentPane().add(calendarYear);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
    }
}
