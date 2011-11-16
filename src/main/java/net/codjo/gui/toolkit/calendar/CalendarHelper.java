/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.util.Date;
/**
 * Assistant pour créer une fenêtre Popup avec un calendrier.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class CalendarHelper extends AbstractCalendarHelper {
    private DefaultCalendarRenderer dateRenderer;


    public CalendarHelper(CallBack callBack) {
        super(callBack);
    }


    public CalendarHelper() {
    }


    @Override
    protected JCalendar newCalendar(final Date selectedDate) {
        final JCalendar cal = new JCalendar();
        cal.setSelectedDate(selectedDate);
        cal.setName("jCalendar");
        if (dateRenderer != null) {
            cal.setDateRenderer(dateRenderer);
        }
        return cal;
    }


    public void setDateRenderer(DefaultCalendarRenderer dateRenderer) {
        this.dateRenderer = dateRenderer;
    }
}
