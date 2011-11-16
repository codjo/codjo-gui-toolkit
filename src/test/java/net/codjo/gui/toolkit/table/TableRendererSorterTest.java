package net.codjo.gui.toolkit.table;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;
import net.codjo.gui.toolkit.table.TableRendererSorter.SortingType;
import net.codjo.test.common.LogString;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class TableRendererSorterTest extends UISpecTestCase {
    private static final long BILLION = 1000000000;
    private Table table;


    public void test_sortByColumn_single() throws Exception {
        TableRendererSorter tableRendererSorter = getSorter(table);

        tableRendererSorter.sortByColumn(1, SortingType.ASCENDING);
        assertTrue(table.contentEquals(new Object[][]{
              {"a", "b", "1", new Date(BILLION).toString(), "true"},
              {"a", "b", "3", new Date(4 * BILLION).toString(), "true"},
              {"a", "c", "5", new Date(8 * BILLION).toString(), "false"},
              {"d", "e", "8", new Date(BILLION).toString(), "false"}}));

        tableRendererSorter.cleanSorting();
        tableRendererSorter.sortByColumn(2, SortingType.ASCENDING);
        assertTrue(table.contentEquals(new Object[][]{
              {"a", "b", "1", new Date(BILLION).toString(), "true"},
              {"a", "b", "3", new Date(4 * BILLION).toString(), "true"},
              {"a", "c", "5", new Date(8 * BILLION).toString(), "false"},
              {"d", "e", "8", new Date(BILLION).toString(), "false"}}));

        tableRendererSorter.cleanSorting();
        tableRendererSorter.sortByColumn(3, SortingType.ASCENDING);
        assertTrue(table.contentEquals(new Object[][]{
              {"a", "c", "5", new Date(8 * BILLION).toString(), "false"},
              {"a", "b", "3", new Date(4 * BILLION).toString(), "true"},
              {"a", "b", "1", new Date(BILLION).toString(), "true"},
              {"d", "e", "8", new Date(BILLION).toString(), "false"},
        }));
    }


    public void test_sortByColumn_multi() throws Exception {
        TableRendererSorter sorter = getSorter(table);
        sorter.sortByColumn(1, SortingType.ASCENDING);
        sorter.sortByColumn(2, SortingType.ASCENDING);

        assertTrue(table.contentEquals(new Object[][]{
              {"a", "b", "1", new Date(BILLION).toString(), "true"},
              {"a", "b", "3", new Date(4 * BILLION).toString(), "true"},
              {"a", "c", "5", new Date(8 * BILLION).toString(), "false"},
              {"d", "e", "8", new Date(BILLION).toString(), "false"}}));
    }


    public void test_sortByColumn_dontSortIfUpdateOneSortedColumn() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);
        assertTrue(table.contentEquals(new Object[][]{
              {"a", "1"},
              {"b", "3"},
              {"c", "2"}
        }));
        assertTrue(getSorter(table).isSorting());

        tableModel.setValueAt("neo-b", 2, 0);

        assertTrue(table.contentEquals(new Object[][]{
              {"a", "1"},
              {"neo-b", "3"},
              {"c", "2"}
        }));
        assertFalse(getSorter(table).isSorting());
    }


    public void test_sortByColumn_sortIfUpdateOneUnsortedColumn() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);

        tableModel.setValueAt("4", 2, 1);

        assertTrue(table.contentEquals(new Object[][]{
              {"a", "1"},
              {"b", "4"},
              {"c", "2"}
        }));
        assertTrue(getSorter(table).isSorting());
    }


    public void test_sortByColumn_dontSortIfAddOneRow() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);
        tableModel.addRow(new Object[]{"aa", "n/a"});

        assertTrue(table.contentEquals(new Object[][]{
              {"a", "1"},
              {"b", "3"},
              {"c", "2"},
              {"aa", "n/a"}
        }));
        assertFalse(getSorter(table).isSorting());
    }


    public void test_sortByColumn_dontSortIfInsertOneRow() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);
        tableModel.insertRow(0, new Object[]{"aa", "n/a"});

        assertTrue(table.contentEquals(new Object[][]{
              {"aa", "n/a"},
              {"a", "1"},
              {"b", "3"},
              {"c", "2"}
        }));
        assertFalse(getSorter(table).isSorting());
    }


    public void test_sortByColumn_dontSortIfRemoveOneRow() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);
        tableModel.removeRow(0);

        assertTrue(table.contentEquals(new Object[][]{
              {"b", "3"},
              {"c", "2"}
        }));
        assertTrue(getSorter(table).isSorting());
    }


    public void test_sortByColumn_sortOnStructureChange() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);

        tableModel.setDataVector(new Object[][]{{"a"}, {"c"}, {"b"}},
                                 new Object[]{"one-column"});

        assertTrue(table.contentEquals(new Object[][]{{"a"}, {"c"}, {"b"}}));
        assertFalse(getSorter(table).isSorting());
    }


    public void test_sortByColumn_sortOnGlobalDataChange() throws Exception {
        DefaultTableModel tableModel = createTable(new Object[][]{
              {"a", "1"},
              {"c", "2"},
              {"b", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);

        //noinspection unchecked
        ((Vector)tableModel.getDataVector().get(0)).set(0, "neo-a");
        tableModel.fireTableDataChanged();

        assertTrue(table.contentEquals(new Object[][]{
              {"b", "3"},
              {"c", "2"},
              {"neo-a", "1"}
        }));
        assertTrue(getSorter(table).isSorting());
    }


    public void test_event_sort() throws Exception {
        final LogString logString = new LogString();

        createTable(new Object[][]{{"a"}, {"c"}, {"b"}});

        getSorter(table).addTableModelListener(new TableModelLogger(logString));
        getSorter(table).sortByColumn(0);

        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[0/MAX])");
    }


    public void test_event() throws Exception {
        final LogString logString = new LogString();

        DefaultTableModel tableModel = createTable(new Object[][]{{"a"}, {"c"}, {"b"}});

        getSorter(table).addTableModelListener(new TableModelLogger(logString));

        // MAJ cellule
        tableModel.setValueAt("neo-a", 1, 0);
        logString.assertContent("tableChanged(update(TableRendererSorter) column[0] row[1])");
        logString.clear();

        // Ajout ligne
        tableModel.addRow(new Object[]{"aa"});
        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[0/MAX])");
        logString.clear();

        // Ajout ligne
        tableModel.insertRow(0, new Object[]{"aaa"});
        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[0/MAX])");
        logString.clear();

        // Suppression ligne
        tableModel.removeRow(0);
        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[0/MAX])");
        logString.clear();

        // Changement structure
        tableModel.setDataVector(new Object[][]{{"a"}, {"c"}, {"b"}}, new Object[]{"one-column"});
        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[ALL])");
        logString.clear();

        // Changement data sur l'ensemble de la table
        //noinspection unchecked
        ((Vector)tableModel.getDataVector().get(0)).set(0, "neo-a");
        tableModel.fireTableDataChanged();
        logString.assertContent("tableChanged(update(TableRendererSorter) column[ALL] row[0/MAX])");
        logString.clear();
    }


    public void test_setModel_null() throws Exception {
        final LogString logString = new LogString();

        DefaultTableModel tableModel = createTable(new Object[][]{{"a"}, {"c"}, {"b"}});

        getSorter(table).addTableModelListener(new TableModelLogger(logString));

        getSorter(table).setModel(null);

        tableModel.fireTableDataChanged();

        logString.assertContent("");
    }


    public void test_addMouseListenerToHeaderInTable() throws Exception {
        createTable();

        getSorter(table).addMouseListenerToHeaderInTable(table.getJTable());
        assertTrue(containsListener(getTableHeader(), "SorterMouseAdapter"));

        getSorter(table).removeMouseListenerToHeaderInTable(table.getJTable());
        assertFalse(containsListener(getTableHeader(), "SorterMouseAdapter"));
    }


    public void test_sort_string_not_case_sensitive() throws Exception {
        createTable(new Object[][]{
              {"b", "1"},
              {"AL", "2"},
              {"Ac", "3"},
        });

        ((JTable)table.getAwtComponent()).getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                return new JLabel(value.toString());
            }
        });
        getSorter(table).sortByColumn(0, SortingType.ASCENDING);

        assertTrue(table.contentEquals(new Object[][]{
              {"Ac", "3"},
              {"AL", "2"},
              {"b", "1"}
        }));
    }


    public void test_sort_object_not_case_sensitive() throws Exception {
        createTable(new Object[][]{
              {"b", "1"},
              {"AL", "2"},
              {"Ac", "3"},
        });

        getSorter(table).sortByColumn(0, SortingType.ASCENDING);

        assertTrue(table.contentEquals(new Object[][]{
              {"Ac", "3"},
              {"AL", "2"},
              {"b", "1"}
        }));
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        table = new Table(createTable());
    }


    private DefaultTableModel createTable(Object[][] data) {
        Object[] firstRow = data[0];
        List<String> header = new ArrayList<String>(firstRow.length);
        for (int i = 0; i < firstRow.length; i++) {
            header.add("col-" + i);
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, header.toArray());

        JTable jtable = new JTable(tableModel);

        TableRendererSorter tableRendererSorter = new TableRendererSorter(jtable);
        jtable.setModel(tableRendererSorter);

        table = new Table(jtable);
        return tableModel;
    }


    private static JTable createTable() {
        JTable jtable = new JTable(new Object[][]{
              {"a", "b", 1, new Date(BILLION), true},
              {"d", "e", 8, new Date(BILLION), false},
              {"a", "b", 3, new Date(4 * BILLION), true},
              {"a", "c", 5, new Date(8 * BILLION), false},
        }, new Object[]{"Alpha", "Beta", "Gamma", "Delta", "Vega"});

        TableRendererSorter tableRendererSorter = new TableRendererSorter(jtable);
        jtable.setModel(tableRendererSorter);
        tableRendererSorter.addMouseListenerToHeaderInTable(jtable);
        tableRendererSorter.changeHeaderRenderer(jtable);

        return jtable;
    }


    private static TableRendererSorter getSorter(Table table) {
        return ((TableRendererSorter)table.getJTable().getModel());
    }


    private JTableHeader getTableHeader() {
        return table.getJTable().getTableHeader();
    }


    private boolean containsListener(JTableHeader tableHeader, String listenerName) {
        for (MouseListener listener : tableHeader.getMouseListeners()) {
            if (listener.getClass().getName().contains(listenerName)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(createTable()));
        frame.pack();
        frame.setVisible(true);
    }
}
