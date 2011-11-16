package net.codjo.gui.toolkit.text;
import junit.framework.TestCase;
/**
 *
 */
public class UpperCaseFieldTest extends TestCase {
    protected UpperCaseField upperCaseField;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        upperCaseField = new UpperCaseField();
    }


    public void testName() throws Exception {
        upperCaseField.setText("abcd1234");
        assertEquals("ABCD1234", upperCaseField.getText());
    }
}
