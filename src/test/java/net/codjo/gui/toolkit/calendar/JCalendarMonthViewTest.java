package net.codjo.gui.toolkit.calendar;
import java.util.Calendar;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class JCalendarMonthViewTest extends UISpecTestCase {
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
}
