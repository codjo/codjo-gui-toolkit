package net.codjo.gui.toolkit.table;
import javax.swing.table.TableColumn;

public class GroupableTableColumn implements Column {
    private TableColumn tableColumn;


    public GroupableTableColumn(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }


    public TableColumn getTableColumn() {
        return tableColumn;
    }


    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GroupableTableColumn)) {
            return false;
        }
        GroupableTableColumn groupableTableColumn = (GroupableTableColumn)obj;
        return this == groupableTableColumn || tableColumn.equals(groupableTableColumn.getTableColumn());
    }


    public int hashCode() {
        return tableColumn.hashCode();
    }
}
