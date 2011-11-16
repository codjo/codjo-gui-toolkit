package net.codjo.gui.toolkit.table;
import javax.swing.table.TableColumn;
import junit.framework.TestCase;

public class GroupableTableColumnTest extends TestCase {

    public void test_equals() throws Exception {
        TableColumn tableColumn1 = new TableColumn();
        TableColumn tableColumn2 = new TableColumn();
        GroupableTableColumn groupableColumn1 = new GroupableTableColumn(tableColumn1);
        GroupableTableColumn groupableColumn2 = new GroupableTableColumn(tableColumn2);
        GroupableTableColumn groupableColumn3 = new GroupableTableColumn(tableColumn2);

        assertFalse(groupableColumn1.equals(new Integer(2)));
        assertTrue(groupableColumn1.equals(groupableColumn1));
        assertFalse(groupableColumn1.equals(groupableColumn2));
        assertTrue(groupableColumn2.equals(groupableColumn3));
    }
}
