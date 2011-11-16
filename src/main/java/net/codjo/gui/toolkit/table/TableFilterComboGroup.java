package net.codjo.gui.toolkit.table;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class TableFilterComboGroup {
    private List<TableFilterCombo> tableFilterComboList = new ArrayList<TableFilterCombo>();


    public void add(TableFilterCombo filterCombo) {
        tableFilterComboList.add(filterCombo);
    }


    public void applyFilter() {
        for (TableFilterCombo tableFilterCombo : tableFilterComboList) {
            tableFilterCombo.applyFilter();
        }
    }
}