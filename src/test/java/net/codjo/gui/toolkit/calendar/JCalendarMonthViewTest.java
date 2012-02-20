package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import net.codjo.gui.toolkit.util.GuiUtil;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class JCalendarMonthViewTest extends UISpecTestCase {
    private static final Color BLACK = GuiUtil.DEFAULT_BLACK_COLOR;
    private static final Color NOTVALID_COLOR = DefaultCalendarRenderer.DEFAULT_NOT_VALID_FOREGROUND;


    public void test_nominal() throws Exception {
        JCalendarMonthView calendar = new JCalendarMonthView();
        calendar.setMonth(Calendar.FEBRUARY, "2012");

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendar);
        assertTrue(gui.getTextBox("MonthLabel_1").textEquals("Février 2012"));
        assertFalse(gui.getComboBox("MonthCombo").isVisible());
        assertFalse(gui.getComboBox("YearCombo").isVisible());
        assertFalse(gui.getTextBox("HelpLabel").isVisible());
        assertTrue(gui.getTable().isEnabled());

        assertTrue(gui.getTable().contentEquals(
              new String[]{"lun.", "mar.", "mer.", "jeu.", "ven.", "sam.", "dim."},
              new String[][]{
                    {"30", "31", "1", "2", "3", "4", "5"},
                    {"6", "7", "8", "9", "10", "11", "12"},
                    {"13", "14", "15", "16", "17", "18", "19"},
                    {"20", "21", "22", "23", "24", "25", "26"},
                    {"27", "28", "29", "1", "2", "3", "4"},
                    {"5", "6", "7", "8", "9", "10", "11"},
              }));
    }


    public void test_disableSelection() throws Exception {
        JCalendarMonthView calendar = new JCalendarMonthView();
        calendar.setMonth(Calendar.FEBRUARY, "2012");
        calendar.enableSelection(false);

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendar);
        assertFalse(gui.getTable().isEnabled());
    }


    public void test_editMonthView() {
        JCalendarMonthView calendar = new JCalendarMonthView();
        calendar.setMonth(Calendar.FEBRUARY, "2012");

        org.uispec4j.Panel gui = new org.uispec4j.Panel(calendar);

        assertTrue(gui.getTable().contentEquals(
              new String[]{"lun.", "mar.", "mer.", "jeu.", "ven.", "sam.", "dim."},
              new String[][]{
                    {"30", "31", "1", "2", "3", "4", "5"},
                    {"6", "7", "8", "9", "10", "11", "12"},
                    {"13", "14", "15", "16", "17", "18", "19"},
                    {"20", "21", "22", "23", "24", "25", "26"},
                    {"27", "28", "29", "1", "2", "3", "4"},
                    {"5", "6", "7", "8", "9", "10", "11"},
              }));

        gui.getTable().click(0, 0);
        assertEquals(0, calendar.getSelectedDates().size());

        gui.getTable().click(1, 2);
        assertSelectedDates(new String[]{"2012-02-08"}, calendar);
        assertTrue(gui.getTable("Calendar_1").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, NOTVALID_COLOR, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        gui.getTable().click(2, 3);
        assertSelectedDates(new String[]{"2012-02-08", "2012-02-16"}, calendar);
        assertTrue(gui.getTable("Calendar_1").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, NOTVALID_COLOR, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, NOTVALID_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));

        gui.getTable().click(1, 2);
        assertSelectedDates(new String[]{"2012-02-16"}, calendar);
        assertTrue(gui.getTable("Calendar_1").foregroundEquals(new Object[][]{
              {Color.WHITE, Color.WHITE, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, NOTVALID_COLOR, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, BLACK, BLACK, Color.lightGray, Color.lightGray},
              {BLACK, BLACK, BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
        }));
    }


    private void assertSelectedDates(String[] expectedDates, JCalendarMonthView calendar) {
        List<Date> dates = new ArrayList<Date>();
        for (String date : expectedDates) {
            dates.add(java.sql.Date.valueOf(date));
        }
        assertEquals(dates, calendar.getSelectedDates());
    }
}
