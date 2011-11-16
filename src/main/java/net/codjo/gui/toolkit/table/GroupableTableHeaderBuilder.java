package net.codjo.gui.toolkit.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public class GroupableTableHeaderBuilder {
    private JTable table;
    private GroupableTableHeader header;
    private List<GroupColumn> groups = new ArrayList<GroupColumn>();
    private Map<Integer, GroupColumn> linkIdToGroup = new HashMap<Integer, GroupColumn>();
    private Map<Integer, Integer> linkIds = new HashMap<Integer, Integer>();


    private GroupableTableHeaderBuilder() {
    }


    public static GroupableTableHeaderBuilder install(JTable table) {
        GroupableTableHeaderBuilder builder = new GroupableTableHeaderBuilder();
        builder.setTable(table).attachHeader();
        return builder;
    }


    private GroupableTableHeaderBuilder setTable(JTable aTable) {
        this.table = aTable;
        return this;
    }


    private GroupableTableHeaderBuilder attachHeader() {
        header = new GroupableTableHeader(table.getColumnModel());
        table.setTableHeader(header);
        return this;
    }


    public GroupableTableHeaderBuilder createGroupColumn(String labelValue, int... columnIndexes) {
        makeGroupColumn(labelValue, columnIndexes);
        return this;
    }


    public GroupableTableHeaderBuilder createGroupColumn(int linkId,
                                                         String labelValue,
                                                         int... columnIndexes) {
        linkIdToGroup.put(linkId, makeGroupColumn(labelValue, columnIndexes));
        return this;
    }


    private GroupColumn makeGroupColumn(String labelValue, int[] columnIndexes) {
        GroupColumn groupColumn = new GroupColumn(labelValue);
        TableColumnModel tableColumnModel = table.getColumnModel();
        for (int columnIndex : columnIndexes) {
            groupColumn.add(tableColumnModel.getColumn(columnIndex));
        }
        groups.add(groupColumn);
        return groupColumn;
    }


    public GroupableTableHeaderBuilder linkGroupColumns(int parentLinkId, int... childrenLinkId) {
        for (int childLinkId : childrenLinkId) {
            linkIds.put(parentLinkId, childLinkId);
        }
        return this;
    }


    public void build() {
        for (Map.Entry<Integer, Integer> entry : linkIds.entrySet()) {
            Integer parentId = entry.getKey();
            GroupColumn parentGroup = linkIdToGroup.get(parentId);
            if (parentGroup == null) {
                throw new IllegalArgumentException("Illegal linkId '" + parentId + "'. " +
                                                   "This linkId must be declared as parameter of the method that builds the group of columns.");
            }

            Integer childId = entry.getValue();
            GroupColumn childGroup = linkIdToGroup.get(childId);
            if (childGroup == null) {
                throw new IllegalArgumentException("Illegal linkId '" + childId + "'. " +
                                                   "This linkId must be declared as parameter of the method that builds the group of columns.");
            }

            parentGroup.add(childGroup);
            groups.remove(childGroup);
        }

        for (GroupColumn groupColumn : groups) {
            header.addGroupColumn(groupColumn);
        }
    }
}