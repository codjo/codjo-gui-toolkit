/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
/**
 * Un composant calendrier.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class JCalendar extends JPanel {
    public static final String DATE_PROPERTY_NAME = "selectedDate";
    public static final String PERIOD_PROPERTY_NAME = "period";
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JLabel helpLabel = new JLabel();
    private JTable calendar = new JTable();
    private JTableHeader calendarHeader = new JTableHeader();
    private CalendarModel calendarModel = new CalendarModel();
    private HeaderRenderer headerRender = new HeaderRenderer();
    private MonthComboBox monthComboBox;
    protected YearComboBox yearComboBox;
    private PeriodChangeListener periodChangeListener = new PeriodChangeListener();
    private ListSelectionListener tableSelectionListener = new TableSelectionListener();
    private java.util.Date selectedDate;


    public JCalendar() {
        monthComboBox = new MonthComboBox();
        monthComboBox.setUserListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                updateModelWithMonth();
                selectCalendarModelDate();
            }
        });

        yearComboBox = new YearComboBox();
        yearComboBox.setUserListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                updateModelWithYear();
                selectCalendarModelDate();
            }
        });

        setLocale(calendarModel.getLocale());
        addTableSelectionListener();
        jbInit();
        calendar.setName("CalendarTable");
        monthComboBox.setName("MonthCombo");
        yearComboBox.setName("YearCombo");
        calendar.getModel().addTableModelListener(periodChangeListener);
        calendar.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        calendar.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setDateRenderer(new DefaultCalendarRenderer());
    }


    public void updateModelWithMonth() {
        calendarModel.setMonth(monthComboBox.getSelectedIndex());
    }


    public void updateModelWithYear() {
        try {
            calendarModel.setYear(Integer.parseInt((String)yearComboBox.getSelectedItem()));
        }
        catch (NumberFormatException ex) {
            calendarModel.setYear(calendarModel.getYear());
        }
    }


    public void setDateRenderer(DefaultCalendarRenderer dateRenderer) {
        calendar.setDefaultRenderer(Date.class, dateRenderer);
    }


    public DefaultCalendarRenderer getDateRenderer() {
        return (DefaultCalendarRenderer)calendar.getDefaultRenderer(Date.class);
    }


    public void setDateHandler(DateHandler newHandler) {
        getDateRenderer().setDateHandler(newHandler);
    }


    public DateHandler getDateHandler() {
        return getDateRenderer().getDateHandler();
    }


    private DateHandler getRootDateHandler() {
        return getDateRenderer().getRootDateHandler();
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        calendar.setEnabled(enabled);
        calendarHeader.setEnabled(enabled);
        monthComboBox.setEnabled(enabled);
        yearComboBox.setEnabled(enabled);
    }


    @Override
    public void setLocale(Locale locale) {
        calendarModel.setLocale(locale);
        super.setLocale(locale);
    }


    public void setSelectedDate(final Date date) {
        removeTableSelectionListener();
        calendarModel.setDate(date);

        selectCalendarModelDate();
    }


    private void selectCalendarModelDate() {
        for (int c = 0; c < calendar.getColumnCount(); c++) {
            for (int r = 0; r < calendar.getRowCount(); r++) {
                if (calendar.getValueAt(r, c).equals(calendarModel.getDate())) {
                    calendar.setRowSelectionInterval(r, r);
                    calendar.setColumnSelectionInterval(c, c);
                    break;
                }
            }
        }
        setSelectedDateTemp(calendarModel.getDate());
        addTableSelectionListener();
    }


    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        calendar.setToolTipText(text);
        calendarHeader.setToolTipText(text);
        monthComboBox.setToolTipText(text);
        yearComboBox.setToolTipText(text);
    }


    public java.util.Date getSelectedDate() {
        return selectedDate;
    }


    void jbInit() {
        this.setLayout(gridBagLayout1);
        calendarModel.setForcedRowCount(6);
        calendar.setColumnSelectionAllowed(true);
        calendar.setModel(calendarModel);
        calendar.setShowHorizontalLines(false);
        calendar.setShowVerticalLines(false);
        calendar.setTableHeader(calendarHeader);
        calendarHeader.setDefaultRenderer(headerRender);
        calendarHeader.setReorderingAllowed(false);
        calendarHeader.setResizingAllowed(false);
        calendarHeader.setColumnModel(calendar.getColumnModel());
        monthComboBox.setFont(new java.awt.Font("Dialog", 0, 10));
        helpLabel.setFont(new java.awt.Font("Dialog", 0, 10));
        monthComboBox.setMinimumSize(new Dimension(80, 19));
        monthComboBox.setMaximumRowCount(5);
        monthComboBox.setCalendarModel(calendarModel);
        yearComboBox.setFont(new java.awt.Font("Dialog", 0, 10));
        yearComboBox.setMinimumSize(new Dimension(74, 19));
        yearComboBox.setMaximumRowCount(5);
        yearComboBox.setPreferredSize(new Dimension(74, 19));
        yearComboBox.setCalendarModel(calendarModel);
        this.setPreferredSize(new Dimension(220, 170));
        helpLabel.setText(" ");
        this.add(yearComboBox,
                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                                        GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(monthComboBox,
                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                        GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        this.add(calendarHeader,
                 new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        this.add(calendar,
                 new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                                        GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
        this.add(helpLabel,
                 new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }


    private void setSelectedDateTemp(java.util.Date selectedDate) {
        java.util.Date oldSelectedDate = this.selectedDate;
        if (selectedDate != null && !getRootDateHandler().selectable(selectedDate)) {
            this.selectedDate = null;
        }
        else {
            this.selectedDate = selectedDate;
        }
        updateHelpLabel();
        firePropertyChange(DATE_PROPERTY_NAME, oldSelectedDate, selectedDate);
    }


    private void addTableSelectionListener() {
        calendar.getSelectionModel().addListSelectionListener(tableSelectionListener);
        calendar.getColumnModel().getSelectionModel().addListSelectionListener(tableSelectionListener);
    }


    private void removeTableSelectionListener() {
        calendar.getSelectionModel().removeListSelectionListener(tableSelectionListener);
        calendar.getColumnModel().getSelectionModel().removeListSelectionListener(tableSelectionListener);
    }


    private void updateHelpLabel() {
        Date value = getSelectedDate();
        if (value == null) {
            helpLabel.setText(" ");
        }
        else {
            helpLabel.setText(DateFormat.getDateInstance(DateFormat.FULL, getLocale())
                  .format(value));
        }
    }


    /**
     * Renderer pour le header du calendrier.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.10 $
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        HeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(Color.gray);
            setForeground(Color.lightGray);
            setFont(new Font(getFont().getName(), Font.PLAIN, getFont().getSize()));
        }


        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            setText((String)value);
            return this;
        }
    }

    private class PeriodChangeListener implements TableModelListener {
        private Date old;


        public void tableChanged(TableModelEvent event) {
            if (event.getFirstRow() != TableModelEvent.HEADER_ROW) {
                return;
            }
            firePropertyChange(PERIOD_PROPERTY_NAME, old, calendarModel.getDate());
            old = calendarModel.getDate();
        }
    }

    /**
     * Listener qui ecoute les modifications de la selection du calendrier.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.10 $
     */
    private class TableSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            if (calendar.getSelectedRow() == -1 || calendar.getSelectedColumn() == -1) {
                setSelectedDateTemp(null);
            }
            else {
                setSelectedDateTemp((Date)calendar.getValueAt(calendar.getSelectedRow(),
                                                              calendar.getSelectedColumn()));
            }
        }
    }


    public YearComboBox getYearComboBox() {
        return yearComboBox;
    }


    public MonthComboBox getMonthComboBox() {
        return monthComboBox;
    }
}
