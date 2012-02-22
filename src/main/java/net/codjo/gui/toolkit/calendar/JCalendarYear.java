package net.codjo.gui.toolkit.calendar;
import com.jidesoft.comparator.DateComparator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
/**
 *
 */
public class JCalendarYear {
    public static final Color HOLIDAY_FOREGROUND = Color.BLACK;
    public static final Color HOLIDAY_BACKGROUND = new Color(200, 200, 255);

    private JPanel mainPanel;
    private JCalendarMonthView january;
    private JCalendarMonthView february;
    private JCalendarMonthView march;
    private JCalendarMonthView april;
    private JCalendarMonthView may;
    private JCalendarMonthView june;
    private JCalendarMonthView july;
    private JCalendarMonthView august;
    private JCalendarMonthView september;
    private JCalendarMonthView october;
    private JCalendarMonthView november;
    private JCalendarMonthView december;

    private Map<Integer, JCalendarMonthView> monthIndexToCalendar = new HashMap<Integer, JCalendarMonthView>();


    public JCalendarYear() {
        monthIndexToCalendar.put(Calendar.JANUARY, january);
        monthIndexToCalendar.put(Calendar.FEBRUARY, february);
        monthIndexToCalendar.put(Calendar.MARCH, march);
        monthIndexToCalendar.put(Calendar.APRIL, april);
        monthIndexToCalendar.put(Calendar.MAY, may);
        monthIndexToCalendar.put(Calendar.JUNE, june);
        monthIndexToCalendar.put(Calendar.JULY, july);
        monthIndexToCalendar.put(Calendar.AUGUST, august);
        monthIndexToCalendar.put(Calendar.SEPTEMBER, september);
        monthIndexToCalendar.put(Calendar.OCTOBER, october);
        monthIndexToCalendar.put(Calendar.NOVEMBER, november);
        monthIndexToCalendar.put(Calendar.DECEMBER, december);

        setEditionMode(false);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void initCalendars(String year) {
        initCalendar(Calendar.JANUARY, year, january);
        initCalendar(Calendar.FEBRUARY, year, february);
        initCalendar(Calendar.MARCH, year, march);
        initCalendar(Calendar.APRIL, year, april);
        initCalendar(Calendar.MAY, year, may);
        initCalendar(Calendar.JUNE, year, june);
        initCalendar(Calendar.JULY, year, july);
        initCalendar(Calendar.AUGUST, year, august);
        initCalendar(Calendar.SEPTEMBER, year, september);
        initCalendar(Calendar.OCTOBER, year, october);
        initCalendar(Calendar.NOVEMBER, year, november);
        initCalendar(Calendar.DECEMBER, year, december);
    }


    private void initCalendar(int month, String year, JCalendarMonthView calendar) {
        calendar.setLocale(Locale.ENGLISH);
        calendar.setMonth(month, year);
        calendar.getDateRenderer().setNotValidForeground(HOLIDAY_FOREGROUND);
        calendar.getDateRenderer().setNotValidBackground(HOLIDAY_BACKGROUND);
    }


    public void setEditionMode(boolean editionMode) {
        for (JCalendarMonthView calendarMonthView : monthIndexToCalendar.values()) {
            calendarMonthView.enableSelection(editionMode);
        }
    }


    public void setYear(String year) {
        initCalendars(year);
    }


    public void setLocale(Locale locale) {
        for (JCalendarMonthView monthView : monthIndexToCalendar.values()) {
            monthView.setLocale(locale);
        }
    }


    public void setHolidays(List<Date> holidays) {
        if (holidays == null) {
            return;
        }

        Calendar calendar = Calendar.getInstance();

        Map<Integer, List<Date>> monthIndexToHolidays = new HashMap<Integer, List<Date>>();
        for (Date holiday : holidays) {
            if (holiday == null) {
                throw new IllegalArgumentException("The holidays list contains null dates!!");
            }
            calendar.setTime(holiday);
            int monthIndex = calendar.get(Calendar.MONTH);
            List<Date> holidayList = monthIndexToHolidays.get(monthIndex);
            if (holidayList == null) {
                holidayList = new ArrayList<Date>();
                monthIndexToHolidays.put(monthIndex, holidayList);
            }
            holidayList.add(holiday);
        }

        for (Entry<Integer, List<Date>> entryHoliday : monthIndexToHolidays.entrySet()) {
            Integer monthIndex = entryHoliday.getKey();
            List<Date> holidayList = entryHoliday.getValue();
            JCalendarMonthView calendarMonthView = monthIndexToCalendar.get(monthIndex);
            calendarMonthView.getDateRenderer().setNoValidDate(holidayList);
        }
    }


    public List<Date> getSelectedDates() {
        List<Date> selectedDates = new ArrayList<Date>();
        for (JCalendarMonthView calendarMonthView : monthIndexToCalendar.values()) {
            selectedDates.addAll(calendarMonthView.getSelectedDates());
        }
        Collections.sort(selectedDates, DateComparator.getInstance());
        return selectedDates;
    }


    public void addDateSelectionListener(DateSelectionListener listener) {
        for (JCalendarMonthView monthView : monthIndexToCalendar.values()) {
            monthView.addDateSelectionListener(listener);
        }
    }


    public void removeDateSelectionListener(DateSelectionListener listener) {
        for (JCalendarMonthView monthView : monthIndexToCalendar.values()) {
            monthView.removeDateSelectionListener(listener);
        }
    }
}
