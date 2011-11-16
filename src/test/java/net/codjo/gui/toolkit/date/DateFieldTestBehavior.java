package net.codjo.gui.toolkit.date;

import net.codjo.test.common.DateUtil;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.SetValueStep;
import net.codjo.test.release.task.gui.metainfo.SetValueDescriptor;
import java.awt.Component;
import java.text.ParseException;
import java.util.Date;
/**
 *
 */
public class DateFieldTestBehavior implements SetValueDescriptor {

    public void setValue(Component comp, SetValueStep step) {
        String value = step.getValue();
        try {
            Date date = DateUtil.createDate(value);

            comp.requestFocusInWindow();
            ((DateField)comp).setDate(date);
        }
        catch (ParseException e) {
            throw new GuiConfigurationException(value
                                                + " n'est pas une date valide. Utiliser le format yyyy-MM-dd");
        }
    }
}
