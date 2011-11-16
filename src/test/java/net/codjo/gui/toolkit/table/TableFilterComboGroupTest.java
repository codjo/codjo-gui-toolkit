package net.codjo.gui.toolkit.table;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class TableFilterComboGroupTest extends UISpecTestCase {

    public void test_applyFilterOnComboGroup() {
        TableModel model = new DefaultTableModel(
              new String[][]{{"1", "A"},
                             {"2", "B"},
                             {"3", "C"},
                             {"1", "A"}},
              new String[]{"colA", "colB"});

        JTable jTable = new JTable(model);
        Table table = new Table(jTable);
        TableFilter tableFilter = new TableFilter(model);
        jTable.setModel(tableFilter);
        TableFilterCombo comboColA = new TableFilterCombo(tableFilter, 0);
        TableFilterCombo comboColB = new TableFilterCombo(tableFilter, 1);
        comboColA.setAutoApplyFilter(false);
        comboColB.setAutoApplyFilter(false);

        TableFilterComboGroup group = new TableFilterComboGroup();
        group.add(comboColA);
        group.add(comboColB);

        assertTrue(table.contentEquals(
              new String[][]{{"1", "A"},
                             {"2", "B"},
                             {"3", "C"},
                             {"1", "A"}}));

        comboColA.setSelectedItem("1");
        comboColB.setSelectedItem("A");
        group.applyFilter();

        assertTrue(table.contentEquals(
              new String[][]{{"1", "A"},
                             {"1", "A"}}));
    }
}
