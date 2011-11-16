package net.codjo.gui.toolkit.date;
import org.uispec4j.Key;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

public class PeriodFieldTest extends UISpecTestCase {
    private PeriodField periodField;


    public void test_init() throws Exception {
        assertEquals("PeriodField", periodField.getName());
    }


    public void test_periodValid() throws Exception {
        periodField.setText("2007");
        assertFalse(periodField.isCorrect());

        periodField.setText("200701");
        assertTrue(periodField.isCorrect());
    }


    public void test_onlyPeriodString() throws Exception {
        periodField.setText("****");
        assertFalse(periodField.isCorrect());

        periodField.setText("wrong period");
        assertFalse(periodField.isCorrect());
    }


    public void test_maxLength() throws Exception {

        TextBox periodTestField = new TextBox(new PeriodField(true));

        periodTestField.pressKey(Key.d2);
        periodTestField.pressKey(Key.d0);
        periodTestField.pressKey(Key.d0);
        periodTestField.pressKey(Key.d8);
        periodTestField.pressKey(Key.d0);
        periodTestField.pressKey(Key.d4);

        assertTrue(periodTestField.textEquals("200804"));

        periodTestField.pressKey(Key.d1);
        periodTestField.pressKey(Key.d2);

        assertTrue(periodTestField.textEquals("200804"));
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        periodField = new PeriodField();
    }
}
