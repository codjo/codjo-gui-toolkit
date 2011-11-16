package net.codjo.gui.toolkit.table;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class GroupColumn implements Column {
    protected TableCellRenderer renderer;
    protected List<Column> columnGroups = new ArrayList<Column>();
    protected String text;
    protected int margin = 0;


    public GroupColumn(String text) {
        this(null, text);
    }


    public GroupColumn(TableCellRenderer renderer, String text) {
        if (renderer == null) {
            this.renderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table,
                                                               Object value,
                                                               boolean isSelected,
                                                               boolean hasFocus,
                                                               int row,
                                                               int column) {
                    JTableHeader header = table.getTableHeader();
                    if (header != null) {
                        setForeground(header.getForeground());
                        setBackground(header.getBackground());
                        setFont(header.getFont());
                    }
                    setHorizontalAlignment(JLabel.CENTER);
                    setText((value == null) ? "" : value.toString());
                    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return this;
                }
            };
        }
        else {
            this.renderer = renderer;
        }
        this.text = text;
    }


    public void add(Column obj) {
        if (obj == null) {
            return;
        }
        columnGroups.add(obj);
    }


    public void add(TableColumn tableColumn) {
        add(new GroupableTableColumn(tableColumn));
    }


    public List<GroupColumn> getColumnGroups(GroupableTableColumn groupableTableColumn,
                                             List<GroupColumn> group) {
        group.add(this);
        if (columnGroups.contains(groupableTableColumn)) {
            return group;
        }
        for (Column obj : columnGroups) {
            if (obj instanceof GroupColumn) {
                List<GroupColumn> cloned = new ArrayList<GroupColumn>(group);
                List<GroupColumn> groups = ((GroupColumn)obj).getColumnGroups(groupableTableColumn, cloned);
                if (groups != null) {
                    return groups;
                }
            }
        }
        return null;
    }


    public TableCellRenderer getHeaderRenderer() {
        return renderer;
    }


    public void setHeaderRenderer(TableCellRenderer renderer) {
        if (renderer != null) {
            this.renderer = renderer;
        }
    }


    public Object getHeaderValue() {
        return text;
    }


    public Dimension getSize(JTable table) {
        Component comp = renderer
              .getTableCellRendererComponent(table, getHeaderValue(), false, false, -1, -1);
        int height = comp.getPreferredSize().height;
        int width = 0;
        for (Column column : columnGroups) {
            if (column instanceof GroupableTableColumn) {
                TableColumn aColumn = ((GroupableTableColumn)column).getTableColumn();
                width += aColumn.getWidth();
                width += margin;
            }
            else {
                width += ((GroupColumn)column).getSize(table).width;
            }
        }
        return new Dimension(width, height);
    }


    public void setColumnMargin(int margin) {
        this.margin = margin;
        for (Column column : columnGroups) {
            if (column instanceof GroupColumn) {
                ((GroupColumn)column).setColumnMargin(margin);
            }
        }
    }


    public int getTotalTableColumns() {
        int totalTableColumns = 0;
        for (Column column : columnGroups) {
            if (column instanceof GroupColumn) {
                totalTableColumns += ((GroupColumn)column).getTotalTableColumns();
            }
            else if (column instanceof GroupableTableColumn) {
                totalTableColumns++;
            }
        }
        return totalTableColumns;
    }
}