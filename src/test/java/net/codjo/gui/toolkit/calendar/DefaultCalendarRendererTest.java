/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import junit.framework.TestCase;
/**
 * Classe de test de <code>DefaultCalendarRenderer</code>.
 *
 * @version $Revision: 1.6 $
 */
public class DefaultCalendarRendererTest extends TestCase {
    private DefaultCalendarRenderer renderer;
    private JTable tableOfDates;
    private Date aDate;


    public void test_default() throws Exception {
        // La date est dans le mois mais est un Week-end
        assertRenderer(newDate("2004-03-07"), "7", Color.lightGray,
                       tableOfDates.getBackground());

        // La date est dans le mois est n'est pas un Week-end
        assertRenderer(aDate, "1", tableOfDates.getForeground(),
                       tableOfDates.getBackground());

        // La date n'est pas dans le mois
        assertRenderer(newDate("2004-04-01"), "1", tableOfDates.getBackground(),
                       tableOfDates.getBackground());

        // La date est OK (pour vérifier si un état persiste).
        assertRenderer(aDate, "1", tableOfDates.getForeground(),
                       tableOfDates.getBackground());
    }


    public void test_defaultRootHandler() throws Exception {
        renderer = new DefaultCalendarRenderer();
        assertTrue(renderer.getRootDateHandler().selectable(new Date()));
    }


    public void test_setValidDate() throws Exception {
        final ArrayList validDates = new ArrayList();
        validDates.add(newDate("2004-03-01"));
        validDates.add(newDate("2004-03-02"));
        renderer.setValidDate(validDates);

        // La date est dans le mois et est une date valide
        assertRenderer(aDate, "1", tableOfDates.getForeground(),
                       tableOfDates.getBackground());

        // La date est OK mais pas dans la liste valide.
        assertRenderer(newDate("2004-03-03"), "3", Color.lightGray,
                       tableOfDates.getBackground());

        // La date n'est pas dans le mois
        assertRenderer(newDate("2004-04-01"), "1", tableOfDates.getBackground(),
                       tableOfDates.getBackground());

        // La date est dans le mois et est une date valide
        assertRenderer(newDate("2004-03-02"), "2", tableOfDates.getForeground(),
                       tableOfDates.getBackground());
    }


    private void assertRenderer(Date date, String txt, Color foreground, Color backGround) {
        Component comp =
              renderer.getTableCellRendererComponent(tableOfDates, date, false, false, 0, 0);
        assertEquals(txt, ((JLabel)comp).getText());
//        assertEquals("foreground color", foreground, comp.getForeground()); // TODO
        assertEquals("background color", backGround, comp.getBackground());
    }


    public static void main(String[] args) {
        JCalendar jcal = new JCalendar();
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


    protected void setUp() throws Exception {
        renderer = new DefaultCalendarRenderer();
        aDate = java.sql.Timestamp.valueOf("2004-03-01 14:25:00");
        tableOfDates = new JTable();
        tableOfDates.setModel(new CalendarModel(Locale.FRANCE, aDate));
    }


    private Date newDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }
}
