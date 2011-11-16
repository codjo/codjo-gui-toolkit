package net.codjo.gui.toolkit.date;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import javax.swing.JFrame;
import org.junit.Before;
import org.junit.Test;
import org.uispec4j.Key;
import org.uispec4j.TextBox;
public class PopupDateFieldTest {
    private PopupDateField popupDateField;
    private TextBox popupDateFieldTest;


    @Test
    public void testBUG() throws Exception {
        popupDateFieldTest.pressKey(Key.d2);
        popupDateFieldTest.pressKey(Key.d5);
        popupDateFieldTest.pressKey(Key.SLASH);

        popupDateFieldTest.pressKey(Key.d1);
        popupDateFieldTest.pressKey(Key.d2);
        popupDateFieldTest.pressKey(Key.SLASH);

        popupDateFieldTest.pressKey(Key.d1);
        popupDateFieldTest.pressKey(Key.d9);
        popupDateFieldTest.pressKey(Key.d9);
        popupDateFieldTest.pressKey(Key.d9);
        assertThat(popupDateFieldTest.getText(), equalTo("25/12/1999"));
    }


    @Test
    public void test_getDate() throws Exception {
        popupDateFieldTest.pressKey(Key.d2);
        popupDateFieldTest.pressKey(Key.d5);
        popupDateFieldTest.pressKey(Key.SLASH);
        popupDateFieldTest.pressKey(Key.d1);
        popupDateFieldTest.pressKey(Key.d2);
        popupDateFieldTest.pressKey(Key.SLASH);
        popupDateFieldTest.pressKey(Key.d2);
        popupDateFieldTest.pressKey(Key.d0);
        popupDateFieldTest.pressKey(Key.d0);
        popupDateFieldTest.pressKey(Key.d1);

        assertThat(new java.sql.Date(popupDateField.getDate().getTime()),
                   equalTo(java.sql.Date.valueOf("2001-12-25")));
    }


    @Test
    public void test_setDate() throws InterruptedException {
        popupDateField.setDate(java.sql.Date.valueOf("2001-12-25"));
        Thread.sleep(250);
        assertThat(popupDateFieldTest.getText(), equalTo("25/12/2001"));
    }


    @Test
    public void test_setDate_monthAndDayAutoCompletion() throws InterruptedException {
        popupDateField.setDate(java.sql.Date.valueOf("2001-1-2"));
        Thread.sleep(250);
        assertThat(popupDateFieldTest.getText(), equalTo("02/01/2001"));
    }


    @Test
    public void test_setDate_null() throws InterruptedException {
        popupDateField.setDate(java.sql.Date.valueOf("2001-12-25"));
        Thread.sleep(250);
        popupDateField.setDate(null);
        Thread.sleep(250);
        assertThat(popupDateFieldTest.getText(), equalTo(""));
    }

 


    @Before
    public void setUp() throws Exception {

        popupDateField = new PopupDateField();
        popupDateFieldTest = new TextBox(popupDateField.getDateTextField());

        JFrame window = new JFrame();
        window.getContentPane().add(popupDateField);
    }
}
