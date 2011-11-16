package net.codjo.gui.toolkit.calendar;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.SetCalendarStep;
import net.codjo.test.release.task.gui.metainfo.SetCalendarDescriptor;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JCalendarTestBehavior implements SetCalendarDescriptor {
    private String pattern = "yyyy-MM-dd";


    public void setCalendar(Component comp, SetCalendarStep step) {
        if (comp == null || !(comp instanceof JCalendar)) {
            throw new GuiConfigurationException("Le composant doit être de type JCalendar");
        }
        try {
            ((JCalendar)comp).setSelectedDate(getDate(step.getValue()));
        }
        catch (ParseException e) {
            String message = "Le format de date est incorrect et doit être du type : " + pattern;
            throw new GuiConfigurationException(message, e);
        }
    }


    private Date getDate(String value) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(value);
    }
}
