/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.table;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import junit.framework.TestCase;
/**
 *
 */
public class TableFilterComboTest extends TestCase {
    TableFilterCombo filterCombo;
    TableFilter filterModel;
    TableFilterTest.BasicModel model;


    @Override
    protected void setUp() throws java.lang.Exception {
        model = new TableFilterTest.BasicModel();
        filterModel = new TableFilter(model);
        filterCombo = new TableFilterCombo();
    }


    public void test_tableFilterOk() throws Exception {
        TableModel tableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "1"},
                    {"b", "2"},
                    {"c", "3"}
              }, new String[]{"lettres", "chiffres"});
        JTable table = new JTable(tableModel);

        TableFilter tableFilter = new TableFilter(table.getModel());
        table.setModel(tableFilter);
        TableFilterCombo tableFilterCombo = new TableFilterCombo(tableFilter, 0);
        tableFilterCombo.setSelectedIndex(1);
        assertEquals(1, table.getRowCount());

        tableFilter.clearAllColumnFilter();
        assertEquals(3, table.getRowCount());

        tableFilterCombo.setAutoApplyFilter(false);
        tableFilterCombo.setSelectedIndex(1);
        assertEquals(3, table.getRowCount());

        tableFilterCombo.applyFilter();
        assertEquals(1, table.getRowCount());
    }


    public void test_filterDisabledWithSetter() throws Exception {
        TableModel tableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "1"},
                    {"b", "2"},
                    {"c", "3"}
              }, new String[]{"lettres", "chiffres"});
        JTable table = new JTable(tableModel);

        TableFilter tableFilter = new TableFilter(table.getModel());
        table.setModel(tableFilter);
        TableFilterCombo tableFilterCombo = new TableFilterCombo(tableFilter, 0);
        tableFilterCombo.setAutoApplyFilter(false);
        tableFilterCombo.setSelectedIndex(1);
        assertEquals(3, table.getRowCount());
    }


    public void test_filterDisabledWithConstructor() throws Exception {
        TableModel tableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "1"},
                    {"b", "2"},
                    {"c", "3"}
              }, new String[]{"lettres", "chiffres"});
        JTable table = new JTable(tableModel);

        TableFilter tableFilter = new TableFilter(table.getModel());
        table.setModel(tableFilter);
        TableFilterCombo tableFilterCombo = new TableFilterCombo(false);
        tableFilterCombo.setTableFilter(tableFilter, 0);
        tableFilterCombo.setSelectedIndex(1);
        assertEquals(3, table.getRowCount());
    }


    public void test_tableFilterTwoTables() throws Exception {
        TableModel firstTableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "10"},
                    {"c", "30"},
                    {"b", "20"}
              }, new String[]{"lettres", "chiffres"});
        JTable firstTable = new JTable(firstTableModel);

        TableFilter firstTableFilter = new TableFilter(firstTable.getModel());
        firstTable.setModel(firstTableFilter);

        TableModel secondTableModel =
              new DefaultTableModel(new String[][]{
                    {"31", "b"},
                    {"11", "a"},
                    {"41", "c"},
                    {"51", "c"},
                    {"21", "b"}
              }, new String[]{"lettres", "chiffres"});
        JTable secondTable = new JTable(secondTableModel);

        TableFilter secondTableFilter = new TableFilter(secondTable.getModel());
        secondTable.setModel(secondTableFilter);
        TableFilterCombo tableFilterCombo = new TableFilterCombo();
        tableFilterCombo.addTableFilter(firstTableFilter, 0);
        tableFilterCombo.addTableFilter(secondTableFilter, 1);

        tableFilterCombo.setSelectedIndex(1);
        assertEquals(2, tableFilterCombo.getTableFilterList().size());
        assertEquals(2, tableFilterCombo.getColumnList().size());
        assertEquals(1, firstTable.getRowCount());
        assertColumn(new String[]{"10"}, firstTable, 1);
        assertEquals(1, secondTable.getRowCount());
        assertColumn(new String[]{"11"}, secondTable, 0);

        tableFilterCombo.setSelectedIndex(0);
        assertEquals(3, firstTable.getRowCount());
        assertColumn(new String[]{"10", "30", "20"}, firstTable, 1);
        assertEquals(5, secondTable.getRowCount());
        assertColumn(new String[]{"31", "11", "41", "51", "21"}, secondTable, 0);

        tableFilterCombo.setSelectedIndex(2);
        assertEquals(1, firstTable.getRowCount());
        assertColumn(new String[]{"20"}, firstTable, 1);
        assertEquals(2, secondTable.getRowCount());
        assertColumn(new String[]{"31", "21"}, secondTable, 0);
    }


    public void test_tableFilterTwoTablesWithComparator()
          throws Exception {
        TableModel firstTableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "10"},
                    {"c", "30"},
                    {"b", "20"}
              }, new String[]{"lettres", "chiffres"});
        JTable firstTable = new JTable(firstTableModel);

        TableFilter firstTableFilter = new TableFilter(firstTable.getModel());
        firstTable.setModel(firstTableFilter);

        TableModel secondTableModel =
              new DefaultTableModel(new String[][]{
                    {"31", "b"},
                    {"11", "a"},
                    {"41", "c"},
                    {"51", "c"},
                    {"21", "b"}
              }, new String[]{"lettres", "chiffres"});
        JTable secondTable = new JTable(secondTableModel);

        TableFilter secondTableFilter = new TableFilter(secondTable.getModel());
        secondTable.setModel(secondTableFilter);

        TableFilterCombo tableFilterCombo = new TableFilterCombo();
        tableFilterCombo.setComparator(new InvertComparator());
        tableFilterCombo.addTableFilter(firstTableFilter, 0);
        tableFilterCombo.addTableFilter(secondTableFilter, 1);

        tableFilterCombo.setSelectedIndex(1);
        assertEquals(1, firstTable.getRowCount());
        assertColumn(new String[]{"30"}, firstTable, 1);
        assertEquals(2, secondTable.getRowCount());
        assertColumn(new String[]{"41", "51"}, secondTable, 0);
    }


    public void test_nullValuesFilter() throws Exception {
        TableModel tableModel =
              new DefaultTableModel(new String[][]{
                    {"a", "10"},
                    {"c", "30"},
                    {"b", "20"}
              }, new String[]{"lettres", "chiffres"});
        JTable firstTable = new JTable(tableModel);

        TableFilter firstTableFilter = new TableFilter(firstTable.getModel());
        firstTable.setModel(firstTableFilter);

        TableFilterCombo tableFilterCombo = new TableFilterCombo();
        tableFilterCombo.addTableFilter(firstTableFilter, 0);

        tableFilterCombo.setSelectedIndex(1);
        assertEquals(1, firstTable.getRowCount());
        assertColumn(new String[]{}, firstTable, 1);
    }


    public void test_booleanValue() {
        Object[] colNames = {"col_name"};
        Object[][] data = {
              {Boolean.FALSE},
              {Boolean.TRUE}
        };
        DefaultTableModel booleanModel = new DefaultTableModel(data, colNames);
        filterModel = new TableFilter(booleanModel);

        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(3, filterCombo.getModel().getSize());
        assertEquals(TableFilterCombo.NO_FILTER, filterCombo.getModel().getElementAt(0));
        assertEquals(Boolean.FALSE, filterCombo.getModel().getElementAt(1));
        assertEquals(Boolean.TRUE, filterCombo.getModel().getElementAt(2));
    }


    public void test_filterAlreadyExist() {
        filterModel.setFilter(0, 1);
        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(filterCombo.getSelectedItem(), filterModel.getFilterValue(0));
    }


    public void test_filterSetProgramaticaly() {
        filterCombo.setTableFilter(filterModel, 0);
        assertNull("NoFilter", filterModel.getFilterValue(0));
        assertEquals(filterCombo.getSelectedItem(), TableFilterCombo.NO_FILTER);

        filterModel.setFilter(0, 1);
        assertEquals("setFilter", filterCombo.getSelectedItem(), 1);
    }


    public void test_noDuplicateItem() {
        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(filterCombo.getModel().getSize(), 3);
        assertEquals(filterCombo.getModel().getElementAt(0), TableFilterCombo.NO_FILTER);
        assertEquals(filterCombo.getModel().getElementAt(1), 0);
        assertEquals(filterCombo.getModel().getElementAt(2), 1);
    }


    public void test_sortOrder() {
        filterCombo.setComparator(new InvertComparator());
        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(filterCombo.getModel().getSize(), 3);
        assertEquals(filterCombo.getModel().getElementAt(0), TableFilterCombo.NO_FILTER);
        assertEquals(filterCombo.getModel().getElementAt(1), 1);
        assertEquals(filterCombo.getModel().getElementAt(2), 0);
    }


    public void test_sortOrder_lateUpdate() {
        filterCombo.setTableFilter(filterModel, 0);
        filterCombo.setComparator(new InvertComparator());
        assertEquals(filterCombo.getModel().getSize(), 3);
        assertEquals(filterCombo.getModel().getElementAt(0), TableFilterCombo.NO_FILTER);
        assertEquals(filterCombo.getModel().getElementAt(1), 1);
        assertEquals(filterCombo.getModel().getElementAt(2), 0);
    }


    public void test_sortOrder_nullValue() {
        // Creation du modele
        Object[] colNames = {"col_name"};
        Object[][] data = {
              {1},
              {2},
              {null}
        };
        DefaultTableModel nullModel = new DefaultTableModel(data, colNames);
        filterModel = new TableFilter(nullModel);

        // Init du Combo
        filterCombo.setComparator(new InvertComparator());
        filterCombo.setTableFilter(filterModel, 0);

        // Verification du resultat
        assertEquals(4, filterCombo.getModel().getSize());
        assertEquals(TableFilterCombo.NO_FILTER, filterCombo.getModel().getElementAt(0));
        assertEquals(TableFilterCombo.NULL_FILTER, filterCombo.getModel().getElementAt(1));
        assertEquals(2, filterCombo.getModel().getElementAt(2));
        assertEquals(1, filterCombo.getModel().getElementAt(3));
    }


    public void test_noDuplicateItem_ModelUpdate() {
        filterCombo.setTableFilter(filterModel, 0);
        model.simulateDataChange(5);
        assertEquals(filterCombo.getModel().getSize(), 3);
        assertEquals(filterCombo.getModel().getElementAt(0), TableFilterCombo.NO_FILTER);
        assertEquals(filterCombo.getModel().getElementAt(1), 5);
        assertEquals(filterCombo.getModel().getElementAt(2), 6);
    }


    /**
     * Test le cas ou le model contient des valeurs nulle.
     */
    public void test_nullValue() {
        Object[] colNames = {"col_name"};
        Object[][] data = {
              {"valA"},
              {null}
        };
        DefaultTableModel nullModel = new DefaultTableModel(data, colNames);
        filterModel = new TableFilter(nullModel);

        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(3, filterCombo.getModel().getSize());
        assertEquals(TableFilterCombo.NO_FILTER, filterCombo.getModel().getElementAt(0));
        assertEquals(TableFilterCombo.NULL_FILTER, filterCombo.getModel().getElementAt(1));
        assertEquals("valA", filterCombo.getModel().getElementAt(2));
    }


    public void test_scenario() {
        filterCombo.setTableFilter(filterModel, 0);
        assertEquals(filterCombo.getModel().getSize(), 3);
        filterModel.setFilter(0, 1);
        assertEquals(filterCombo.getModel().getSize(), 3);
    }


    public void test_userSelectFilter() {
        filterCombo.setTableFilter(filterModel, 0);
        assertEquals("Par defaut NO_FILTER", filterCombo.getSelectedItem(),
                     TableFilterCombo.NO_FILTER);
        assertEquals("NoFilter", filterModel.getFilterValue(0), null);

        filterCombo.setSelectedItem(0);
        assertEquals("Combo setFilter", filterModel.getFilterValue(0), 0);
    }


    private void assertColumn(String[] expectedColumn, JTable actualTable, int column) {
        for (int i = 0; i < expectedColumn.length; i++) {
            String columnValue = expectedColumn[i];
            assertEquals(actualTable.getValueAt(i, column), columnValue);
        }
    }


    private static class InvertComparator implements java.util.Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 instanceof Integer) {
                return -1 * ((Integer)o1).compareTo((Integer)o2);
            }
            return -1 * ((String)o1).compareTo((String)o2);
        }
    }
}
