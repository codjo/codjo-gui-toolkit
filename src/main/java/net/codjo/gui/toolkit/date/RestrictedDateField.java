package net.codjo.gui.toolkit.date;

import net.codjo.gui.toolkit.calendar.AbstractCalendarHelper;
import net.codjo.gui.toolkit.calendar.CalendarHelper;
import net.codjo.gui.toolkit.calendar.DateHandler;
import net.codjo.gui.toolkit.calendar.DefaultCalendarRenderer;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;

public class RestrictedDateField extends DateField {
    public static final Date MAX_DATE;


    static {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 9999);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        MAX_DATE = cal.getTime();
    }


    public static final Date MIN_DATE;


    static {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1900);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        MIN_DATE = cal.getTime();
    }


    private List<Date> invalidDate;
    private boolean allowPastDate;
    private boolean allowWeekEnds;
    private List<DateRange> excludedDateRanges = new ArrayList<DateRange>();


    public RestrictedDateField() {
        this(false, false);
    }


    public RestrictedDateField(List<Date> invalidDates) {
        this(false, false, invalidDates);
    }


    public RestrictedDateField(boolean allowPastDate, boolean allowWeekEnds) {
        this(allowPastDate, allowWeekEnds, new ArrayList<Date>());
    }


    public RestrictedDateField(boolean allowPastDate,
                               boolean allowWeekEnds,
                               List<Date> invalidDate) {
        this(allowPastDate, allowWeekEnds, invalidDate, true, null);
    }


    public RestrictedDateField(boolean allowPastDate,
                               boolean allowWeekEnds,
                               List<Date> invalidDate,
                               boolean showCalendar,
                               CallBack callBack) {
        super(showCalendar, callBack);

        this.allowPastDate = allowPastDate;
        this.allowWeekEnds = allowWeekEnds;
        this.invalidDate = invalidDate;
    }


    public void addExcludedDateRange(Date minDate, Date maxDate) {
        excludedDateRanges.add(new DateRange(minDate, maxDate));
    }


    public void setMaxDateRange(Date maxDate) throws IllegalArgumentException {
        final Iterator<DateRange> rangeIterator = excludedDateRanges.iterator();
        while (rangeIterator.hasNext()) {
            DateRange dateRange = rangeIterator.next();
            if (MIN_DATE == dateRange.getBeginDate()) {
                rangeIterator.remove();
            }
        }
        excludedDateRanges.add(new DateRange(MIN_DATE, maxDate));
    }


    public void setMinDateRange(Date minDate) throws IllegalArgumentException {
        final Iterator<DateRange> rangeIterator = excludedDateRanges.iterator();
        while (rangeIterator.hasNext()) {
            DateRange dateRange = rangeIterator.next();
            if (MAX_DATE == dateRange.getEndDate()) {
                rangeIterator.remove();
            }
        }
        excludedDateRanges.add(new DateRange(minDate, MAX_DATE));
    }


    public void setInvalidDate(List<Date> invalidDate) {
        this.invalidDate = invalidDate;
    }


    public void setAllowPastDate(boolean allowPastDate) {
        this.allowPastDate = allowPastDate;
    }


    public boolean isAllowPastDate() {
        return allowPastDate;
    }


    public boolean isAllowWeekEnds() {
        return allowWeekEnds;
    }


    public void addDate(Date newDate) {
        if (invalidDate == null) {
            invalidDate = new ArrayList<Date>();
        }
        this.invalidDate.add(newDate);
    }


    @Override
    public void setCalendarHelper(AbstractCalendarHelper calHelper) {
        if (calHelper instanceof CalendarHelper) {
            ((CalendarHelper)calHelper).setDateRenderer(createRenderer());
        }
        super.setCalendarHelper(calHelper);
    }


    private DefaultCalendarRenderer createRenderer() {
        DefaultCalendarRenderer renderer = new DefaultCalendarRenderer();
        DateHandler successor = new NotValidDatesHandler(this);

        if (!this.isAllowPastDate()) {
            PastDateHandler pastHandler = new PastDateHandler();
            pastHandler.setSuccessor(successor);
            successor = pastHandler;
        }

        DateHandler weekEndHandler = this.isAllowWeekEnds() ?
                                     renderer.getDateHandler() :
                                     new WeekEndHandler();
        weekEndHandler.setSuccessor(successor);
        successor = weekEndHandler;

        NotExcludeDatesRangeHandler notExcludeDatesRangeHandler = new NotExcludeDatesRangeHandler(this);
        notExcludeDatesRangeHandler.setSuccessor(successor);

        renderer.setDateHandler(notExcludeDatesRangeHandler);
        return renderer;
    }


    @Override
    protected Date buildDateValueFromGUI() {
        Date value = super.buildDateValueFromGUI();
        if (value != BAD_DATE) {
            if (!excludedDateRanges.isEmpty()) {
                for (DateRange dateRange : excludedDateRanges) {
                    if (dateRange.contains(value)) {
                        return BAD_DATE;
                    }
                    ;
                }
            }
            if (!allowPastDate) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, -1);

                if (value.before(cal.getTime())) {
                    return BAD_DATE;
                }
            }

            if (!allowWeekEnds) {
                WeekEndHandler we = new WeekEndHandler();
                if (we.handle(value)) {
                    return BAD_DATE;
                }
            }

            if (isInvalidDate(value)) {
                return BAD_DATE;
            }
        }
        return value;
    }


    public boolean isInvalidDate(Date value) {
        return !invalidDate.isEmpty() && invalidDate.contains(value);
    }


    public List<DateRange> getExcludedDateRanges() {
        return new ArrayList<DateRange>(excludedDateRanges);
    }


    public void clearExcludedDateRanges() {
        excludedDateRanges.clear();
    }


    public boolean containsExcludedDate(Date value) {
        List<DateRange> rangeDates = excludedDateRanges;
        for (DateRange rangeDate : rangeDates) {
            if (rangeDate.contains(value)) {
                return true;
            }
        }
        return false;
    }


    private static abstract class NotSelectableDatesHandler extends DateHandler {

        @Override
        protected boolean handleSelectable(Date input) {
            return false;
        }


        @Override
        protected JLabel handleRenderer(Date input,
                                        JLabel renderer,
                                        JTable table,
                                        boolean isSelected,
                                        boolean hasFocus,
                                        int row,
                                        int column) {

            renderer.setForeground(Color.lightGray);
            renderer.setBackground(table.getBackground());
            renderer.setBorder(null);

            return renderer;
        }
    }

    private static class NotValidDatesHandler extends NotSelectableDatesHandler {
        private RestrictedDateField restrictedDateField;


        private NotValidDatesHandler(RestrictedDateField restrictedDateField) {
            this.restrictedDateField = restrictedDateField;
        }


        @Override
        protected boolean handle(Date input) {
            return restrictedDateField.isInvalidDate(input);
        }
    }

    private static class NotExcludeDatesRangeHandler extends NotSelectableDatesHandler {
        private RestrictedDateField restrictedDateField;


        private NotExcludeDatesRangeHandler(RestrictedDateField restrictedDateField) {
            this.restrictedDateField = restrictedDateField;
        }


        @Override
        protected boolean handle(Date input) {
            for (DateRange dateRange : restrictedDateField.getExcludedDateRanges()) {
                if (dateRange.contains(input)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class PastDateHandler extends NotSelectableDatesHandler {

        @Override
        protected boolean handle(Date input) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            return !cal.getTime().before(input);
        }
    }

    private static class WeekEndHandler extends NotSelectableDatesHandler {

        @Override
        public boolean handle(Date input) {
            return isWeekEnd(input);
        }
    }
}
