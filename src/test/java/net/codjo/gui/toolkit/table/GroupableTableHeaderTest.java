package net.codjo.gui.toolkit.table;
import java.awt.BorderLayout;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import junit.framework.TestCase;

public class GroupableTableHeaderTest extends TestCase {
    private JTable table;

    private GroupableTableHeader groupableTableHeader;
    private TableColumn tableColumn0;
    private TableColumn tableColumn1;
    private TableColumn tableColumn2;
    private TableColumn tableColumn3;
    private TableColumn tableColumn4;
    private TableColumn tableColumn5;
    private GroupColumn languageGroupColumn;
    private GroupColumn othersGroupColumn;
    private GroupColumn nameGroupColumn;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DefaultTableModel dm = new DefaultTableModel(new Object[][]{{"119", "foo", "bar", "ja", "ko", "zh"},
                                                                    {"911", "bar", "foo", "en", "fr", "pt"}},
                                                     new Object[]{"SNo.", "1", "2", "Native", "4", "5"});

        table = new JTable(dm);
        groupableTableHeader = new GroupableTableHeader(table.getColumnModel());
        table.setTableHeader(groupableTableHeader);

        linkColumns();
    }


    private void linkColumns() {
        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumn0 = tableColumnModel.getColumn(0);
        tableColumn1 = tableColumnModel.getColumn(1);
        tableColumn2 = tableColumnModel.getColumn(2);
        tableColumn3 = tableColumnModel.getColumn(3);
        tableColumn4 = tableColumnModel.getColumn(4);
        tableColumn5 = tableColumnModel.getColumn(5);

        nameGroupColumn = new GroupColumn("Name");
        nameGroupColumn.add(tableColumn1);
        nameGroupColumn.add(tableColumn2);

        othersGroupColumn = new GroupColumn("Others");
        othersGroupColumn.add(tableColumn4);
        othersGroupColumn.add(tableColumn5);

        languageGroupColumn = new GroupColumn("Language");
        languageGroupColumn.add(tableColumn3);
        languageGroupColumn.add(othersGroupColumn);

        groupableTableHeader.addGroupColumn(nameGroupColumn);
        groupableTableHeader.addGroupColumn(languageGroupColumn);
    }


    public void test_getColumnGroups() throws Exception {
        assertGroupTableHeaderSize(1, groupableTableHeader, tableColumn1);
        assertGroupTableHeaderSize(0, groupableTableHeader, tableColumn0);
        assertGroupTableHeaderContains(nameGroupColumn, groupableTableHeader, tableColumn1);
        assertGroupTableHeaderSize(1, groupableTableHeader, tableColumn2);
        assertGroupTableHeaderContains(nameGroupColumn, groupableTableHeader, tableColumn2);
        assertGroupTableHeaderSize(1, groupableTableHeader, tableColumn3);
        assertGroupTableHeaderContains(languageGroupColumn, groupableTableHeader, tableColumn3);
        assertGroupTableHeaderSize(2, groupableTableHeader, tableColumn4);
        assertGroupTableHeaderContains(othersGroupColumn, groupableTableHeader, tableColumn4);
        assertGroupTableHeaderContains(languageGroupColumn, groupableTableHeader, tableColumn4);
        assertGroupTableHeaderSize(2, groupableTableHeader, tableColumn5);
        assertGroupTableHeaderContains(othersGroupColumn, groupableTableHeader, tableColumn5);
        assertGroupTableHeaderContains(languageGroupColumn, groupableTableHeader, tableColumn5);
    }


    private static void assertGroupTableHeaderSize(int expectedSize,
                                                   GroupableTableHeader header,
                                                   TableColumn tableColumn) {
        Iterator<GroupColumn> columnGroupsIt = header.getColumnGroups(tableColumn);
        int size = 0;
        while (columnGroupsIt.hasNext()) {
            columnGroupsIt.next();
            size++;
        }
        if (expectedSize != size) {
            throw new AssertionError("Expected " + expectedSize + "   Actual " + size);
        }
    }


    public void test_getRowCount() throws Exception {
        assertEquals(3, groupableTableHeader.getRowCount());
    }


    private static boolean assertGroupTableHeaderContains(GroupColumn expectedTableColumn,
                                                          GroupableTableHeader header,
                                                          TableColumn parentTableColumn) {
        Iterator<GroupColumn> columnGroupsIt = header.getColumnGroups(parentTableColumn);
        while (columnGroupsIt.hasNext()) {
            GroupColumn groupColumn = columnGroupsIt.next();
            if (groupColumn == expectedTableColumn) {
                return true;
            }
        }
        return false;
    }


    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        DefaultTableModel dm = new DefaultTableModel(new Object[][]{
              {"119", "foo", "bar", "ja", "ko", "zh", "EUR", "YEN", "USD", "GBP", "FRF"},
              {"911", "bar", "foo", "en", "fr", "pt", "GBP", "FRF", "EUR", "YEN", "USD"}},
                                                     new Object[]{"SNo.", "1", "2", "Native", "2", "3", "4",
                                                                  "5", "6", "7", "8"});

        table = new JTable(dm);

        GroupableTableHeaderBuilder.install(table)
              .createGroupColumn("Name", 1, 2)
              .createGroupColumn(0, "Language", 3)
              .createGroupColumn(1, "Others", 4, 5)
              .createGroupColumn("Currency", 6, 7, 8, 9, 10)
              .linkGroupColumns(0, 1)
              .build();

        mainPanel.add(new JScrollPane(table));

        return mainPanel;
    }


    public static void main(String[] args) {
        GroupableTableHeaderTest groupableTableHeaderTest = new GroupableTableHeaderTest();

        JFrame frame = new JFrame("Test GroupableTableHeader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(groupableTableHeaderTest.createMainPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
