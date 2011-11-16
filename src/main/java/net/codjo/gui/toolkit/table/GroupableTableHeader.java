package net.codjo.gui.toolkit.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GroupableTableHeader extends JTableHeader {
    protected List<GroupColumn> groupColumns;


    public GroupableTableHeader(TableColumnModel tableColumnModel) {
        super(tableColumnModel);
        setUI(new GroupableTableHeaderUI());
        setReorderingAllowed(false);
    }


    @Override
    public void updateUI() {
        setUI(new GroupableTableHeaderUI());
    }


    @Override
    public void setReorderingAllowed(boolean allowed) {
        reorderingAllowed = false;
    }


    public void addGroupColumn(GroupColumn groupColumn) {
        if (groupColumns == null) {
            groupColumns = new ArrayList<GroupColumn>();
        }
        groupColumns.add(groupColumn);
    }


    public Iterator<GroupColumn> getColumnGroups(TableColumn tableColumn) {
        return getColumnGroupsList(tableColumn).iterator();
    }


    private List<GroupColumn> getColumnGroupsList(TableColumn tableColumn) {
        if (groupColumns == null) {
            return new ArrayList<GroupColumn>();
        }
        for (GroupColumn groupColumn : groupColumns) {
            GroupableTableColumn groupableTableColumn = new GroupableTableColumn(tableColumn);
            List<GroupColumn> columnGroups = groupColumn.getColumnGroups(groupableTableColumn,
                                                                         new ArrayList<GroupColumn>());
            if (columnGroups != null) {
                return columnGroups;
            }
        }
        return new ArrayList<GroupColumn>();
    }


    public int getRowCount() {
        int rowCount = 0;
        TableColumnModel tableColumnModel = table.getColumnModel();
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tableColumn = tableColumnModel.getColumn(col);
            List<GroupColumn> columns = getColumnGroupsList(tableColumn);
            rowCount = Math.max(rowCount, columns.size());
        }
        return rowCount+1;
    }


    public void setColumnMargin() {
        if (groupColumns == null) {
            return;
        }
        int columnMargin = getColumnModel().getColumnMargin();
        for (GroupColumn groupColumn : groupColumns) {
            groupColumn.setColumnMargin(columnMargin);
        }
    }
}