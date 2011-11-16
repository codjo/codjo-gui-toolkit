/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import net.codjo.test.common.LogString;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.uispec4j.Button;
import org.uispec4j.Key;
import org.uispec4j.Panel;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
/**
 * Classe de test de <code>DateField</code>.
 *
 * @author Boris
 * @version $Revision: 1.13 $
 */
public class DateFieldTest extends UISpecTestCase {
    private DateField dateField;
    private TextBox dayFieldTest;
    private TextBox monthFieldTest;
    private TextBox yearFieldTest;
    private LogString log = new LogString();


    public void testBUG() throws Exception {
        dayFieldTest.pressKey(Key.d2);
        dayFieldTest.pressKey(Key.d5);

        monthFieldTest.pressKey(Key.d1);
        monthFieldTest.pressKey(Key.d2);

        yearFieldTest.pressKey(Key.d1);
        yearFieldTest.pressKey(Key.d9);
        yearFieldTest.pressKey(Key.d9);
        yearFieldTest.pressKey(Key.d9);

        assertEquals("25", dayFieldTest.getText());
        assertEquals("12", monthFieldTest.getText());
        assertEquals("1999", yearFieldTest.getText());
    }

    // TODO : a remettre quand setup et teardown dans UISpecTestCase seront corrigés. JFCUnit et UISpec ne peut pas cohabiter ensemble
    /*  public void testTransitFocus() throws Exception {
        // Test transit du focus de Jours vers Mois
        dayFieldTest.pressKey(Key.d1);
        dayFieldTest.pressKey(Key.d0);

        assertEquals("10", dayFieldTest.getText());

        assertEquals(false, dateField.getDateTextField().hasFocus());

        assertEquals(true, dateField.getMonthField().hasFocus());

        monthFieldTest.pressKey(Key.d1);
        monthFieldTest.pressKey(Key.d0);

        assertEquals("10", monthFieldTest.getText());
        assertEquals(false, dateField.getMonthField().hasFocus());
        assertEquals(true, dateField.getYearField().hasFocus());
    }*/


    public void test_getDate() throws Exception {
        dayFieldTest.pressKey(Key.d2);
        dayFieldTest.pressKey(Key.d5);

        monthFieldTest.pressKey(Key.d1);
        monthFieldTest.pressKey(Key.d2);

        yearFieldTest.pressKey(Key.d2);
        yearFieldTest.pressKey(Key.d0);
        yearFieldTest.pressKey(Key.d0);
        yearFieldTest.pressKey(Key.d1);

        assertEquals(java.sql.Date.valueOf("2001-12-25"), dateField.getDate());
    }


    public void test_setDate() throws InterruptedException {
        dateField.setDate(java.sql.Date.valueOf("2001-12-25"));

        assertTrue(dayFieldTest.textEquals("25"));
        assertTrue(monthFieldTest.textEquals("12"));
        assertTrue(yearFieldTest.textEquals("2001"));
    }


    public void test_setDate_monthAndDayAutoCompletion() {
        dateField.setDate(java.sql.Date.valueOf("2001-1-2"));

        assertTrue(dayFieldTest.textEquals("02"));
        assertTrue(monthFieldTest.textEquals("01"));
        assertTrue(yearFieldTest.textEquals("2001"));
    }


    public void test_setDate_null() {
        dateField.setDate(java.sql.Date.valueOf("2001-12-25"));

        dateField.setDate(null);

        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());
    }


    public void test_calendar_not_visible() {
        dateField = new DateField(false);
        Panel panel = new Panel(dateField);

        Button calendarButton = panel.getButton();
        assertFalse(calendarButton.isVisible());
    }


    public void test_setDateWithNullValueNotAuthorized() {
        dateField.setNullDateValueAuthorized(false);

        dateField.setDate(java.sql.Date.valueOf("2001-12-25"));

        dateField.setDate(null);

        assertTrue(dayFieldTest.textEquals("25"));
        assertTrue(monthFieldTest.textEquals("12"));
        assertTrue(yearFieldTest.textEquals("2001"));
    }


    public void test_calendar_defaultDateIsRecorded() {
        initializeDateField(new DateField(true, null, new JButtonThatMockGetLocationOnScreen()));

        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());

        WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {

                Button calendarButton = new Button(dateField.getCalendarButton());
                calendarButton.click();
            }
        }).process(new WindowHandler() {
            @Override
            public Trigger process(Window window) throws Exception {
                return window.getButton("OK").triggerClick();
            }
        }).run();

        Calendar cal = Calendar.getInstance();

        assertTrue(dayFieldTest.textEquals(getIntAsString(cal.get(Calendar.DAY_OF_MONTH))));
        assertTrue(monthFieldTest.textEquals(getIntAsString(cal.get(Calendar.MONTH) + 1)));
        assertTrue(yearFieldTest.textEquals("" + cal.get(Calendar.YEAR)));
    }


    public void test_hasLostFocus() throws Exception {
        DateField.DateFieldGlobalFocusValidator dateValidator = dateField.new DateFieldGlobalFocusValidator();
        FocusEvent focusEvent = mock(FocusEvent.class);
        when(focusEvent.getOppositeComponent()).thenReturn(dateField.getMonthField());
        assertThat(dateValidator.hasGloballyLostFocus(focusEvent), equalTo(false));
        when(focusEvent.getOppositeComponent()).thenReturn(dateField.getDayField());
        assertThat(dateValidator.hasGloballyLostFocus(focusEvent), equalTo(false));
        when(focusEvent.getOppositeComponent()).thenReturn(dateField.getYearField());
        assertThat(dateValidator.hasGloballyLostFocus(focusEvent), equalTo(false));
        when(focusEvent.getOppositeComponent()).thenReturn(new JTextField());
        assertThat(dateValidator.hasGloballyLostFocus(focusEvent), equalTo(true));
    }


    public void test_setDate_likeBefore() throws ParseException {
        DateField.DateFieldGlobalFocusValidator globalFocusValidator
              = dateField.new DateFieldGlobalFocusValidator();
        dateField.addFocusListener(globalFocusValidator);
        dayFieldTest.pressKey(Key.d2);
        dayFieldTest.pressKey(Key.d5);
        monthFieldTest.pressKey(Key.d1);
        monthFieldTest.pressKey(Key.d2);
        yearFieldTest.pressKey(Key.d2);
        yearFieldTest.pressKey(Key.d0);
        yearFieldTest.pressKey(Key.d0);
        yearFieldTest.pressKey(Key.d1);
        assertFalse(dateField.getDate() == null);
        Date expectedDate = convertInDate("25/12/2001");
        assertTrue(expectedDate.equals(dateField.getDate()));
    }


    public void test_enabledFields() throws Exception {
        dateField.setEnabled(true, false);
        assertEquals(false, dateField.getCalendarButton().isEnabled());
        assertEquals(true, dateField.getDayField().isEnabled());
        assertEquals(true, dateField.getMonthField().isEnabled());
        assertEquals(true, dateField.getYearField().isEnabled());
        dateField.setEnabled(false, true);
        assertEquals(true, dateField.getCalendarButton().isEnabled());
        assertEquals(false, dateField.getDayField().isEnabled());
        assertEquals(false, dateField.getMonthField().isEnabled());
        assertEquals(false, dateField.getYearField().isEnabled());
    }


    public void test_actionListener() throws Exception {
        dateField.setDate(new Date());
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.call("actionPerformed", ((JComponent)e.getSource()).getName());
            }
        };

        dateField.addActionListener(actionListener);
        ((JTextField)dayFieldTest.getAwtComponent()).postActionEvent();
        log.assertAndClear("actionPerformed(null.dayField)");
        ((JTextField)monthFieldTest.getAwtComponent()).postActionEvent();
        log.assertAndClear("actionPerformed(null.monthField)");
        ((JTextField)yearFieldTest.getAwtComponent()).postActionEvent();
        log.assertAndClear("actionPerformed(null.yearField)");

        dateField.setDate(null);
        log.assertAndClear("actionPerformed(null)");

        dateField.removeActionListener(actionListener);
        ((JTextField)dayFieldTest.getAwtComponent()).postActionEvent();
        ((JTextField)monthFieldTest.getAwtComponent()).postActionEvent();
        ((JTextField)yearFieldTest.getAwtComponent()).postActionEvent();
        dateField.setDate(null);

        log.assertContent("");
    }


    private static Date convertInDate(String value) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(value);
    }


    private String getIntAsString(int number) {
        if (number >= 10) {
            return "" + number;
        }
        else {
            return "0" + number;
        }
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initializeDateField(new DateField());
    }


    private void initializeDateField(DateField field) {
        this.dateField = field;
        this.dateField.setDisplayingDayOfWeek(true);
        dayFieldTest = new TextBox(this.dateField.getDayField());
        monthFieldTest = new TextBox(this.dateField.getMonthField());
        yearFieldTest = new TextBox(this.dateField.getYearField());
        JFrame window = new JFrame();
        window.getContentPane().add(this.dateField);
    }


    private static class JButtonThatMockGetLocationOnScreen extends JButton {

        @Override
        public Point getLocationOnScreen() {
            return new Point(40, 50);
        }
    }
}
