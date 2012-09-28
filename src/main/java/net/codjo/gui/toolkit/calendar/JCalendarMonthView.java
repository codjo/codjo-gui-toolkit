package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.border.Border;
import net.codjo.gui.toolkit.util.GuiUtil;
/**
 *
 */
public class JCalendarMonthView extends JCalendar {

    private JLabel monthLabel;
    private List<Date> selectedDates = new ArrayList<Date>();
    private int currentMonth;
    private List<DateSelectionListener> dateSelectionListeners = new ArrayList<DateSelectionListener>();
    private JCalendarMonthView.CalendarSelector calendarSelector = new CalendarSelector();


    public JCalendarMonthView() {
        monthLabel = new JLabel();
        monthLabel.setFont(GuiUtil.BOLD_FONT);
        monthComboBox.setVisible(false);
        yearComboBox.setVisible(false);

        helpLabel.setVisible(false);

        add(monthLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                               GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        calendar.addMouseListener(calendarSelector);
        calendar.setSelectionForeground(GuiUtil.DEFAULT_BLACK_COLOR);
        calendar.setSelectionBackground(Color.WHITE);
        setDateRenderer(new MonthViewCalendarRenderer());
    }


    public void setMonth(int month, String year) {
        currentMonth = month;
        calendar.setName("Calendar_" + month);

        monthComboBox.setSelectedIndex(month);
        yearComboBox.setSelectedItem(year);

        monthLabel.setName("MonthLabel_" + month);
        monthLabel.setText(
              capitalizeMonthText((String)monthComboBox.getSelectedItem()) + " " + yearComboBox.getSelectedItem());
    }


    public void setHolidays(List<Date> holidayList) {
        if (holidayList == null) {
            holidayList = new ArrayList<Date>();
        }
        this.selectedDates = holidayList;
        getDateRenderer().setNoValidDate(holidayList);
        repaint();
    }


    public void setTooltipForHoliday(Date holiday, String tooltip) {
        getDateRenderer().setTooltipForDate(holiday, tooltip);
    }


    public void removeAllTooltips() {
        getDateRenderer().removeAllTooltips();
    }


    @Override
    public Date getSelectedDate() {
        throw new RuntimeException("method not supported for " + getClass().getName());
    }


    public List<Date> getSelectedDates() {
        return Collections.unmodifiableList(selectedDates);
    }


    public void addDateSelectionListener(DateSelectionListener dateSelectionListener) {
        dateSelectionListeners.add(dateSelectionListener);
    }


    public void removeDateSelectionListener(DateSelectionListener dateSelectionListener) {
        dateSelectionListeners.remove(dateSelectionListener);
    }


    public void enableSelection(boolean enable) {
        calendar.setEnabled(enable);
        calendar.removeMouseListener(calendarSelector);
        if (enable) {
            calendar.addMouseListener(calendarSelector);
        }
    }


    @Override
    protected void selectCalendarModelDate() {
    }


    private String capitalizeMonthText(String month) {
        return month.substring(0, 1).toUpperCase() + month.substring(1);
    }


    private class CalendarSelector extends MouseAdapter {
        private final Calendar cal = Calendar.getInstance();


        @Override
        public void mouseReleased(MouseEvent e) {
            int row = calendar.rowAtPoint(e.getPoint());
            int col = calendar.columnAtPoint(e.getPoint());

            if (row < 0 || col < 0) {
                return;
            }

            Date date = (Date)calendar.getValueAt(row, col);

            if (date == null || !isInMonth(currentMonth, date)) {
                return;
            }

            if (selectedDates.contains(date)) {
                selectedDates.remove(date);
            }
            else {
                selectedDates.add(date);
            }

            JCalendarMonthView.this.getDateRenderer().setNoValidDate(selectedDates);
            calendar.repaint();

            fireDateSelectionChanged();
        }


        private boolean isInMonth(int month, Date date) {
            cal.setTime(date);
            return cal.get(Calendar.MONTH) == month;
        }
    }


    private void fireDateSelectionChanged() {
        for (DateSelectionListener dateSelectionListener : dateSelectionListeners) {
            dateSelectionListener.selectionChanged();
        }
    }


    private class MonthViewCalendarRenderer extends DefaultCalendarRenderer {
        @Override
        public void setBorder(Border border) {
        }
    }
}
