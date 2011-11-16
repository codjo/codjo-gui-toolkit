/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import java.awt.Point;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.uispec4j.Button;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
/**
 *
 */
public class NoNullDateFieldTest extends UISpecTestCase {
    private static final Date NO_NULL =
          new GregorianCalendar(9999, GregorianCalendar.DECEMBER, 31).getTime();
    private NoNullDateField dateField;
    private TextBox dayFieldTest;
    private TextBox monthFieldTest;
    private TextBox yearFieldTest;


    public void test_noNull() {
        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());
    }


    public void test_notNullDate() {
        dateField.setDate(null);
        assertNotNull(dateField.getDate());
    }


    public void test_noNullAfterSetDate() {
        dateField.setDate(NO_NULL);

        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());

        dateField.setDate(null);

        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());

        Date birthday = new GregorianCalendar(1976, GregorianCalendar.DECEMBER, 10).getTime();
        dateField.setDate(birthday);

        assertTrue(dayFieldTest.textEquals("10"));
        assertTrue(monthFieldTest.textEquals("12"));
        assertTrue(yearFieldTest.textEquals("1976"));
    }


    /**
     * !!! Ce test ne fonctionne pas sous IDEA quand on lance tous les tests !!!
     */
    public void test_openCalendarWithNoNull() throws Exception {

        initializeDateField(new NoNullDateField(true, new JButtonThatMockGetLocationOnScreen()));
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

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals(calendar.getTime(), dateField.getDate());
    }


    public void test_setNoNullDate() throws Exception {
        Date infiniteDate = new GregorianCalendar(8888, Calendar.FEBRUARY, 5).getTime();
        dateField.setNoNullDate(infiniteDate);
        dateField.setDate(infiniteDate);

        assertTrue(dayFieldTest.textIsEmpty());
        assertTrue(monthFieldTest.textIsEmpty());
        assertTrue(yearFieldTest.textIsEmpty());
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initializeDateField(new NoNullDateField());
    }


    private void initializeDateField(NoNullDateField dateField) {
        this.dateField = dateField;
        dateField.setDate(NO_NULL);
        this.dateField.setName("dateField");
        this.dateField.setDisplayingDayOfWeek(true);
        dayFieldTest = new TextBox(this.dateField.getDayField());
        monthFieldTest = new TextBox(this.dateField.getMonthField());
        yearFieldTest = new TextBox(this.dateField.getYearField());
        JFrame window = new JFrame();
        window.getContentPane().add(this.dateField);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    private static class JButtonThatMockGetLocationOnScreen extends JButton {

        @Override
        public Point getLocationOnScreen() {
            return new Point(40, 50);
        }
    }
}
