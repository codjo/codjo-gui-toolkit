/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
/**
 * Classe de test de <code>DefaultCalendarRenderer</code>.
 *
 * @version $Revision: 1.6 $
 */
public class DefaultCalendarRendererTest extends UISpecTestCase {
    private static final Color DEFAULT_BLACK = new JLabel().getForeground();

    private DefaultCalendarRenderer renderer;
    private JTable tableOfDates;


    public void test_default() throws Exception {
        Table theTable = new Table(tableOfDates);

        assertTrue(theTable.contentEquals(
              new Object[][]{
                    {"1", "2", "3", "4", "5", "6", "7"},
                    {"8", "9", "10", "11", "12", "13", "14"},
                    {"15", "16", "17", "18", "19", "20", "21"},
                    {"22", "23", "24", "25", "26", "27", "28"},
                    {"29", "30", "31", "1", "2", "3", "4"},
              }
        ));

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));

        renderer.setWeekEndColor(Color.RED);

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.RED, Color.RED},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.RED, Color.RED},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.RED,
                     Color.RED},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.RED,
                     Color.RED},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));
    }


    public void test_defaultRootHandler() throws Exception {
        renderer = new DefaultCalendarRenderer();
        assertTrue(renderer.getRootDateHandler().selectable(new Date()));
    }


    public void test_setValidDate() throws Exception {
        final List<Date> validDates = new ArrayList<Date>();
        validDates.add(newDate("2004-03-01"));
        validDates.add(newDate("2004-03-02"));
        renderer.setValidDate(validDates);

        Table theTable = new Table(tableOfDates);

        assertTrue(theTable.contentEquals(
              new Object[][]{
                    {"1", "2", "3", "4", "5", "6", "7"},
                    {"8", "9", "10", "11", "12", "13", "14"},
                    {"15", "16", "17", "18", "19", "20", "21"},
                    {"22", "23", "24", "25", "26", "27", "28"},
                    {"29", "30", "31", "1", "2", "3", "4"},
              }
        ));

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.WHITE, Color.WHITE, Color.WHITE,
                     Color.WHITE},
              }
        ));

        renderer.setNotValidForeground(Color.RED);

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {DEFAULT_BLACK, DEFAULT_BLACK, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED},
                    {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED},
                    {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED},
                    {Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED, Color.RED},
                    {Color.RED, Color.RED, Color.RED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));
    }


    public void test_setNoValidDate() throws Exception {
        final List<Date> noValidDates = new ArrayList<Date>();
        noValidDates.add(newDate("2004-03-01"));
        noValidDates.add(newDate("2004-03-02"));
        renderer.setNoValidDate(noValidDates);

        Table theTable = new Table(tableOfDates);

        assertTrue(theTable.contentEquals(
              new Object[][]{
                    {"1", "2", "3", "4", "5", "6", "7"},
                    {"8", "9", "10", "11", "12", "13", "14"},
                    {"15", "16", "17", "18", "19", "20", "21"},
                    {"22", "23", "24", "25", "26", "27", "28"},
                    {"29", "30", "31", "1", "2", "3", "4"},
              }
        ));

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {Color.lightGray, Color.lightGray, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));

        renderer.setNotValidForeground(Color.RED);

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {Color.RED, Color.RED, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray,
                     Color.lightGray},
                    {DEFAULT_BLACK, DEFAULT_BLACK, DEFAULT_BLACK, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));
    }


    public void test_highlightDates() throws Exception {
        final List<Date> validDates = new ArrayList<Date>();
        validDates.add(newDate("2004-03-01"));
        validDates.add(newDate("2004-03-02"));
        renderer.setValidDate(validDates);

        Table theTable = new Table(tableOfDates);

        assertTrue(theTable.contentEquals(
              new Object[][]{
                    {"1", "2", "3", "4", "5", "6", "7"},
                    {"8", "9", "10", "11", "12", "13", "14"},
                    {"15", "16", "17", "18", "19", "20", "21"},
                    {"22", "23", "24", "25", "26", "27", "28"},
                    {"29", "30", "31", "1", "2", "3", "4"},
              }
        ));

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {DEFAULT_BLACK, DEFAULT_BLACK, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray,
                     Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.WHITE, Color.WHITE, Color.WHITE,
                     Color.WHITE},
              }
        ));

        assertTrue(theTable.backgroundEquals(
              new Object[][]{
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));

        final List<Date> datesToHighlight = new ArrayList<Date>();
        datesToHighlight.add(newDate("2004-03-01"));
        datesToHighlight.add(newDate("2004-03-06"));
        datesToHighlight.add(newDate("2004-03-18"));
        datesToHighlight.add(newDate("2004-04-02"));

        renderer.setDateHighlighter(new DateHighlighter() {
            public boolean highlight(Date date) {
                return datesToHighlight.contains(date);
            }


            public Color getHighlightForeground() {
                return Color.BLUE;
            }


            public Color getHighlightBackground() {
                return Color.GREEN;
            }
        });

        assertTrue(theTable.foregroundEquals(
              new Object[][]{
                    {Color.BLUE, DEFAULT_BLACK, Color.lightGray, Color.lightGray, Color.lightGray, Color.BLUE, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.BLUE, Color.lightGray, Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray, Color.lightGray},
                    {Color.lightGray, Color.lightGray, Color.lightGray, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));

        assertTrue(theTable.backgroundEquals(
              new Object[][]{
                    {Color.GREEN, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.GREEN, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.GREEN, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
                    {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE},
              }
        ));
    }


    public static void main(String[] args) {
        JCalendarMonthView jcal = new JCalendarMonthView();
        jcal.setMonth(0, "2012");
        jcal.setSelectedDate(new Date(System.currentTimeMillis()));
        final DateHandler handler =
              new DateHandler() {
                  public boolean handle(java.util.Date input) {
                      return input.compareTo(new java.util.Date()) < 0;
                  }


                  protected JLabel handleRenderer(java.util.Date input, JLabel renderer) {
                      if (isWeekEnd(input)) {
                          renderer.setForeground(Color.gray);
                          final Font font = renderer.getFont();
                          renderer.setFont(font.deriveFont(Font.BOLD).deriveFont(Font.ITALIC));
                      }
                      else {
                          renderer.setForeground(Color.green);
                          // Le background ne sera pris en compte que lors de la selection
                          // car le renderer n'est pas opaque par défaut.
                          renderer.setBackground(Color.red);
                          renderer.setBorder(BorderFactory.createLineBorder(Color.green));
                      }
                      return renderer;
                  }
              };
        handler.setSuccessor(jcal.getDateHandler());
        jcal.setDateHandler(handler);

        JFrame frame = new JFrame("Test Renderer");
        frame.getContentPane().add(jcal);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
    }


    @Override
    protected void setUp() throws Exception {
        renderer = new DefaultCalendarRenderer();
        tableOfDates = new JTable();
        tableOfDates.setDefaultRenderer(Date.class, renderer);
        tableOfDates.setModel(new CalendarModel(Locale.FRANCE, java.sql.Timestamp.valueOf("2004-03-01 14:25:00")));
    }


    private static Date newDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }
}
