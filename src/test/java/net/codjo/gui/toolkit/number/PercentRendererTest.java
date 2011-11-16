package net.codjo.gui.toolkit.number;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import junit.framework.TestCase;
/**
 *
 */
public class PercentRendererTest extends TestCase {
    public void test_getTableCellRendererComponent()
          throws Exception {
        JTable table = new JTable();
        PercentRenderer renderer = new PercentRenderer(new DefaultTableCellRenderer());
        renderer.getTableCellRendererComponent(table, "0", false, false, 0, 0);
    }


    public void test_getTableCellRendererComponent_Error()
          throws Exception {
        JTable table = new JTable();
        PercentRenderer renderer = new PercentRenderer(new DefaultListCellRenderer());
        try {
            renderer.getTableCellRendererComponent(table, "0", false, false, 0, 0);
            fail("Exception attendue.");
        }
        catch (IllegalArgumentException exception) {
            assertEquals(PercentRenderer.LIST_NOT_TABLE_ERROR,
                         exception.getLocalizedMessage());
        }
    }


    public void test_getListCellRendererComponent()
          throws Exception {
        JList list = new JList();
        PercentRenderer renderer = new PercentRenderer(new DefaultListCellRenderer());
        renderer.getListCellRendererComponent(list, "0", 0, false, false);
    }


    public void test_getListCellRendererComponent_Error()
          throws Exception {
        JList list = new JList();
        PercentRenderer renderer = new PercentRenderer(new DefaultTableCellRenderer());
        try {
            renderer.getListCellRendererComponent(list, "0", 0, false, false);
            fail("Exception attendue.");
        }
        catch (IllegalArgumentException exception) {
            assertEquals(PercentRenderer.TABLE_NOT_LIST_ERROR,
                         exception.getLocalizedMessage());
        }
    }
}
