package net.codjo.gui.toolkit.calendar;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import net.codjo.test.release.task.gui.GuiConfigurationException;
import net.codjo.test.release.task.gui.SetCalendarStep;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;
import org.junit.Test;

public class JCalendarTestBehaviorTest {

    @Test(expected = GuiConfigurationException.class)
    public void test_badArgument() throws Exception {
        JCalendarTestBehavior behavior = new JCalendarTestBehavior();
        behavior.setCalendar(new JTextField(), new SetCalendarStep());
    }

    @Test
    public void test_nominal() throws Exception {
        JCalendarTestBehavior behavior = new JCalendarTestBehavior();
        JCalendar jCalendar = new JCalendar();
        final String strDate = "2009-10-10";
        SetCalendarStep valueStep = new SetCalendarStep() {
            @Override
            public String getValue() {
                return strDate;
            }
        };
        behavior.setCalendar(jCalendar, valueStep);

        assertThat(jCalendar.getSelectedDate(),
                   equalTo(new SimpleDateFormat("yyyy-MM-dd").parse(strDate)));
    }

    @Test(expected = GuiConfigurationException.class)
    public void test_badDate() throws Exception {
        JCalendarTestBehavior behavior = new JCalendarTestBehavior();
        JCalendar jCalendar = new JCalendar();
        final String strDate = "10/10/2009";
        SetCalendarStep valueStep = new SetCalendarStep() {
            @Override
            public String getValue() {
                return strDate;
            }
        };
        behavior.setCalendar(jCalendar, valueStep);
        assertThat(jCalendar.getSelectedDate(),
                   equalTo(new SimpleDateFormat("dd/MM/yyyy").parse(strDate)));
    }
}
