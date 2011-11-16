package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.calendar.JCalendar;
import net.codjo.gui.toolkit.date.DateField;
import net.codjo.test.common.DateUtil;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.uispec4j.Button;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;
/**
 *
 */
public class DateFieldEditorTest extends UISpecTestCase {
    private JTable jTable;


    public void test_stopEditionAfterChoosingfromCalendar() throws Exception {
        jTable.editCellAt(0, 1);
        Panel dateFieldPanel = new Panel((DateField)jTable.getEditorComponent());
        final Button calendarButton = dateFieldPanel.getButton("BirthdayDate.calendarButton");

        WindowInterceptor.init(calendarButton.triggerClick())
              .process(new WindowHandler() {
                  @Override
                  public Trigger process(Window window) throws Exception {
                      assertTrue(jTable.isEditing());
                      Panel jCalendarPanel = window.getPanel("jCalendar");
                      JCalendar jCalendar = (JCalendar)jCalendarPanel.getAwtComponent();
                      jCalendar.setSelectedDate(DateUtil.createDate("2008-06-19"));
                      return window.getButton("Calendar.OK").triggerClick();
                  }
              }).run();

        assertFalse(jTable.isEditing());
        String birthdayDate = (String)jTable.getModel().getValueAt(0, 1);
        assertEquals("2008-06-19", birthdayDate);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JFrame window = new JFrame();
        DefaultTableModel model = new DefaultTableModel(
              new Object[][]{{"Vanessa", "2004-06-19"},
                             {"Alex", "1978-06-29"}},
              new Object[]{"prenom", "dateNaissance"});
        jTable = new JTable(model);
        jTable.getColumnModel().getColumn(1).setCellEditor(
              new DateFieldEditor(true, "BirthdayDate", new JButtonThatMockGetLocationOnScreen()));
        window.getContentPane().add(jTable);
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
