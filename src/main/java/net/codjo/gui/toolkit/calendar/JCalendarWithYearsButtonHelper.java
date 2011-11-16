package net.codjo.gui.toolkit.calendar;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Date;
import javax.swing.JDialog;
/**
 *
 */
public class JCalendarWithYearsButtonHelper extends AbstractCalendarHelper {
    public JCalendarWithYearsButtonHelper(
          CallBack callBack) {
        super(callBack);
    }


    public JCalendarWithYearsButtonHelper() {
    }


    @Override
    protected JCalendar newCalendar(final Date selectedDate) {
        final JCalendarWithYearsButton cal = new JCalendarWithYearsButton();
        cal.setSelectedDate(selectedDate);
        cal.setName("jCalendar");
        return cal;
    }


    @Override
    protected void correctLocationWithStartMenu(JDialog dialog, Point point) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = dialog.getSize();
        int yAxis = dialog.getLocation().y;
        int xAxis = new Double(point.getX()).intValue();

        if (yAxis + dialogSize.height > (screenSize.height - 20)) {
            yAxis -= yAxis + dialogSize.height - (screenSize.height - 20);
        }
        if (xAxis + dialogSize.width+110 < (screenSize.width - 20)) {
            xAxis = xAxis +110;
        }else{
            xAxis=dialog.getLocation().x;
        }
        dialog.setLocation( xAxis, yAxis);
    }
}
