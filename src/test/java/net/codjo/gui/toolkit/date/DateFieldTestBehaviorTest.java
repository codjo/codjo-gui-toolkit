package net.codjo.gui.toolkit.date;
import net.codjo.test.common.DateUtil;
import net.codjo.test.release.task.gui.SetValueStep;
import junit.framework.TestCase;
/**
 *
 */
public class DateFieldTestBehaviorTest extends TestCase {
    private DateFieldTestBehavior behavior;


    public void test_dateFieldSetValue() throws Exception {
        SetValueStep step = new SetValueStep();
        step.setValue("2006-08-06");
        DateField dateField = new DateField();
        behavior.setValue(dateField, step);

        assertEquals(DateUtil.createDate("2006-08-06"), dateField.getDate());
    }


    @Override
    protected void setUp() {
        behavior = new DateFieldTestBehavior();
    }
}
