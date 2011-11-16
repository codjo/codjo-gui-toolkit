/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**
 * Implements a user interface and generates FindFilter for selecting files by date.
 *
 * @author Thierrou
 * @version $Revision: 1.4 $
 */
class FindByDate extends JPanel implements FindFilterFactory {
    public static final String THE_BIG_BANG = "Sans limite";
    public static final String THE_BIG_CRUNCH = "Sans limitefca";
    public static final String YESTERDAY = "Hier";
    public static final String TODAY = "Aujourd\'hui";
    public static final String NOW = "Maintenant";
    public static final String MODIFIED_LABEL = "Modifié";
    public static final String FORMAT_LABEL = "mm/dd/yyyy";
    public static final String FROM_DATE_LABEL = "entre";
    public static final String TO_DATE_LABEL = "et";
    private JComboBox fromDateField = null;
    private JComboBox toDateField = null;
    private String[] fromDateItems = {THE_BIG_BANG, YESTERDAY, TODAY};
    private String[] toDateItems = {THE_BIG_CRUNCH, TODAY, NOW, YESTERDAY};

    FindByDate() {
        setLayout(new BorderLayout());

        Font font = new Font("Helvetica", Font.PLAIN, 10);

        // Grid Layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 2, 2));

        // Date selection criteria
        JLabel modified = new JLabel(MODIFIED_LABEL, SwingConstants.LEFT);
        modified.setFont(font);
        modified.setForeground(Color.black);
        panel.add(modified);

        // format note
        JLabel format = new JLabel(FORMAT_LABEL, SwingConstants.LEFT);
        format.setFont(font);
        format.setForeground(Color.black);
        panel.add(format);

        // between
        JLabel betweenLabel = new JLabel(FROM_DATE_LABEL, SwingConstants.RIGHT);
        betweenLabel.setFont(font);
        betweenLabel.setForeground(Color.black);
        panel.add(betweenLabel);

        // from date
        fromDateField = new JComboBox(fromDateItems);
        fromDateField.setFont(font);
        fromDateField.setEditable(true);
        panel.add(fromDateField);

        // and
        JLabel andLabel = new JLabel(TO_DATE_LABEL, SwingConstants.RIGHT);
        andLabel.setFont(font);
        andLabel.setForeground(Color.black);
        panel.add(andLabel);

        //toDateField = new JTextField(8);
        toDateField = new JComboBox(toDateItems);
        toDateField.setFont(font);
        toDateField.setEditable(true);
        panel.add(toDateField);

        add(panel, BorderLayout.NORTH);
    }

    public JComboBox getFromDateField() {
        return fromDateField;
    }


    public JComboBox getToDateField() {
        return toDateField;
    }


    public String[] getFromDateItems() {
        return fromDateItems;
    }


    public String[] getToDateItems() {
        return toDateItems;
    }


    /**
     * Generate a search filter object based on the setting of this UI component.
     *
     * @return a FindFilter object that implements the selection criteria
     */
    public FindFilter createFindFilter() {
        long from = startDateToTime((String)fromDateField.getSelectedItem());
        long to = endDateToTime((String)toDateField.getSelectedItem());

        return new DateFilter(from, to);
    }


    /**
     * Convenience method for converting the start date text to milliseconds since
     * January 1, 1970.
     *
     * @param stringDate date to convert
     *
     * @return milliseconds since January 1, 1970
     */
    protected long startDateToTime(String stringDate) {
        if (stringDate == null) {
            return -1;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = formatter.parse(stringDate, new ParsePosition(0));
        if (date == null) {
            if (stringDate.equalsIgnoreCase(TODAY)) {
                String today = formatter.format(new Date());
                date = formatter.parse(today, new ParsePosition(0));
            }
            else if (stringDate.equalsIgnoreCase(YESTERDAY)) {
                String yesterday =
                    formatter.format(new Date(new Date().getTime() - 24 * 60 * 60 * 1000));
                date = formatter.parse(yesterday, new ParsePosition(0));
            }
            else if (stringDate.equalsIgnoreCase(THE_BIG_BANG)) {
                return 0;
                // Not exactly the beginning of time, but
                //close enough for computer work
            }
        }
        if (date != null) {
            return date.getTime();
        }
        return -1;
    }


    /**
     * Convenience method for converting the end date text to milliseconds since January
     * 1, 1970. The end time is the end of the specified day.
     *
     * @param dateText date to convert
     *
     * @return milliseconds since January 1, 1970
     */
    protected long endDateToTime(String dateText) {
        if (dateText == null) {
            return -1;
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        long time = -1;
        Date date = dateFormatter.parse(dateText, new ParsePosition(0));
        if (date == null) {
            if (dateText.equalsIgnoreCase(TODAY)) {
                String today = dateFormatter.format(new Date());
                date = dateFormatter.parse(today, new ParsePosition(0));
                if (date != null) {
                    time = date.getTime() + (24L * 3600L * 1000L);
                }
            }
            else if (dateText.equalsIgnoreCase(YESTERDAY)) {
                String yesterday =
                    dateFormatter.format(new Date(new Date().getTime()
                            - 24 * 60 * 60 * 1000));
                date = dateFormatter.parse(yesterday, new ParsePosition(0));
                if (date != null) {
                    time = date.getTime() + (24L * 3600L * 1000L);
                }
            }
            else if (dateText.equalsIgnoreCase(NOW)) {
                time = System.currentTimeMillis();
            }
            else if (dateText.equalsIgnoreCase(THE_BIG_CRUNCH)) {
                time = Long.MAX_VALUE;
            }
        }
        else {
            // Valid date. Now add 24 hours to make sure that the
            // date is inclusive
            time = date.getTime() + (24L * 3600L * 1000L);
        }

        return time;
    }

    /**
     * Filter object for selecting files by the date range specified by the UI.
     *
     * @author Thierrou
     * @version $Revision: 1.4 $
     */
    class DateFilter implements FindFilter {
        private long startTime = -1;
        private long endTime = -1;

        DateFilter(long from, long to) {
            startTime = from;
            endTime = to;
        }

        public long getStartTime() {
            return startTime;
        }


        public long getEndTime() {
            return endTime;
        }


        public boolean accept(File file, FindProgressCallback callback) {
            if (file == null) {
                return false;
            }

            long lastModificationTime = file.lastModified();

            if (startTime >= 0) {
                if (lastModificationTime < startTime) {
                    return false;
                }
            }
            if (endTime >= 0) {
                if (lastModificationTime > endTime) {
                    return false;
                }
            }

            return true;
        }
    }
}
