/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 * Renderer par defaut pour un calendrier. Le renderer peut etre parametre par DateHandler.
 *
 * @author Boris
 * @version $Revision: 1.10 $
 */
public class DefaultCalendarRenderer extends DefaultTableCellRenderer {
    private DateHandler dateHandler;
    private NotInCurrentMonthHandler notInMonth = new NotInCurrentMonthHandler();


    public DefaultCalendarRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setDateHandler(new WeekEndHandler());
    }


    public void setValidDate(List validDate) {
        final InListHandler newHandler = new InListHandler(validDate);
        newHandler.setSuccessor(new WeekEndHandler());
        setDateHandler(newHandler);
    }


    public void setDateHandler(DateHandler newHandler) {
        notInMonth.setSuccessor(newHandler);
        this.dateHandler = newHandler;
    }


    public DateHandler getDateHandler() {
        return this.dateHandler;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        // Init gui (au cas ou les handler bidouille)
        resetGuiStatus(table);

        if (value == null) {
            return super.getTableCellRendererComponent(table, value, isSelected,
                                                       hasFocus, row, column);
        }

        super.getTableCellRendererComponent(table, toDayOfMonth((Date)value), isSelected,
                                            hasFocus, row, column);

        notInMonth.setCurrentMonth(((CalendarModel)table.getModel()).getMonth());

        return getRootDateHandler().renderer((Date)value, this, table, isSelected,
                                             hasFocus, row, column);
    }


    private void resetGuiStatus(JTable table) {
        setForeground(table.getForeground());
        setBackground(table.getBackground());
        setBorder(null);
        setFont(table.getFont());
        setEnabled(true);
    }


    private String toDayOfMonth(Date value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        return Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    }


    public DateHandler getRootDateHandler() {
        return notInMonth;
    }


    /**
     * Handler des dates hors mois courant.
     */
    private static class NotInCurrentMonthHandler extends DateHandler {
        private final Calendar calendar = Calendar.getInstance();
        private int currentMonth;


        @Override
        public boolean handle(Date input) {
            calendar.setTime(input);
            return calendar.get(Calendar.MONTH) != currentMonth;
        }


        public void setCurrentMonth(int month) {
            this.currentMonth = month;
        }


        @Override
        protected JLabel handleRenderer(Date input, JLabel renderer, JTable table,
                                        boolean isSelected, boolean hasFocus, int row, int column) {
            renderer.setForeground(table.getBackground());
            renderer.setBackground(table.getBackground());
            renderer.setBorder(null);
            return renderer;
        }
    }

    /**
     * Handler des dates week-End.
     */
    private static class WeekEndHandler extends DateHandler {
        @Override
        public boolean handle(Date input) {
            return isWeekEnd(input);
        }


        @Override
        protected JLabel handleRenderer(Date input, JLabel renderer) {
            renderer.setForeground(Color.lightGray);
            return renderer;
        }
    }

    /**
     * Verifie que la date appartient à la liste des dates valide.
     */
    private static class InListHandler extends DateHandler {
        private final List<Object> validTime;


        InListHandler(List validDate) {
            this.validTime = new ArrayList<Object>(validDate);
        }


        @Override
        public boolean handle(Date input) {
            return !validTime.contains(input);
        }


        @Override
        protected JLabel handleRenderer(Date input, JLabel renderer) {
            renderer.setForeground(Color.lightGray);
            return renderer;
        }
    }
}
