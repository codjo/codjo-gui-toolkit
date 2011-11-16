package net.codjo.gui.toolkit.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class GroupableTableHeaderUI extends BasicTableHeaderUI {

    @Override
    public void paint(Graphics graphics, JComponent component) {
        Rectangle clipBounds = graphics.getClipBounds();
        if (header.getColumnModel() == null) {
            return;
        }
        ((GroupableTableHeader)header).setColumnMargin();
        int column = 0;
        Dimension size = header.getSize();
        Rectangle cellRect = new Rectangle(0, 0, size.width, size.height);
        Map<GroupColumn, Rectangle> columnGroupToRect = new HashMap<GroupColumn, Rectangle>();
        int columnMargin = header.getColumnModel().getColumnMargin();

        Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            cellRect.height = size.height;
            cellRect.y = 0;
            TableColumn tableColumn = enumeration.nextElement();
            Iterator columnGroups = ((GroupableTableHeader)header).getColumnGroups(tableColumn);
            if (columnGroups != null) {
                int groupHeight = 0;
                while (columnGroups.hasNext()) {
                    GroupColumn groupColumn = (GroupColumn)columnGroups.next();
                    Rectangle groupRect = columnGroupToRect.get(groupColumn);
                    if (groupRect == null) {
                        groupRect = new Rectangle(cellRect);
                        Dimension groupColumnSize = groupColumn.getSize(header.getTable());
                        groupRect.width = groupColumnSize.width - groupColumn.getTotalTableColumns();
                        groupRect.height = groupColumnSize.height;
                        columnGroupToRect.put(groupColumn, groupRect);
                    }
                    paintCell(graphics, groupRect, groupColumn);
                    groupHeight += groupRect.height;
                    cellRect.height = size.height - groupHeight;
                    cellRect.y = groupHeight;
                }
            }
            cellRect.width = tableColumn.getWidth() + columnMargin - 1;
            if (cellRect.intersects(clipBounds)) {
                paintGroupCell(graphics, cellRect, column);
            }
            cellRect.x += cellRect.width;
            column++;
        }
    }


    private void paintGroupCell(Graphics graphics, Rectangle cellRect, int columnIndex) {
        TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
        TableCellRenderer renderer = getGroupCellRenderer(columnIndex);
        Component cellRendererComponent = renderer.getTableCellRendererComponent(
              header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);

        cellRendererComponent.setBackground(UIManager.getColor("control"));

        rendererPane.add(cellRendererComponent);
        rendererPane.paintComponent(graphics, cellRendererComponent, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }


    private TableCellRenderer getGroupCellRenderer(int columnIndex) {
        TableCellRenderer headerRenderer = header.getColumnModel().getColumn(columnIndex).getHeaderRenderer();
        if (headerRenderer == null) {
            return new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    JLabel header = new JLabel();
                    header.setForeground(table.getTableHeader().getForeground());
                    header.setBackground(table.getTableHeader().getBackground());
                    header.setFont(table.getTableHeader().getFont());

                    header.setHorizontalAlignment(JLabel.CENTER);
                    header.setText(value.toString());
                    header.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                    return header;
                }
            };
        }
        return headerRenderer;
    }


    private void paintCell(Graphics graphics, Rectangle cellRect, GroupColumn groupColumn) {
        TableCellRenderer renderer = groupColumn.getHeaderRenderer();

        Component component = renderer.getTableCellRendererComponent(
              header.getTable(), groupColumn.getHeaderValue(), false, false, -1, -1);
        rendererPane.add(component);
        rendererPane.paintComponent(graphics, component, header, cellRect.x, cellRect.y,
                                    cellRect.width, cellRect.height, true);
    }


    private int getGroupHeaderHeight() {
        int height = 0;
        TableColumnModel columnModel = header.getColumnModel();
        for (int column = 0; column < columnModel.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            TableCellRenderer renderer = tableColumn.getHeaderRenderer();
            int cHeight = 20;
            if (renderer != null) {
                Component component = renderer.getTableCellRendererComponent(
                      header.getTable(), tableColumn.getHeaderValue(), false, false, -1, column);
                cHeight = component.getPreferredSize().height;
            }

            Iterator<GroupColumn> columnGroupsIt
                  = ((GroupableTableHeader)header).getColumnGroups(tableColumn);
            if (columnGroupsIt != null) {
                while (columnGroupsIt.hasNext()) {
                    GroupColumn groupColumn = columnGroupsIt.next();
                    cHeight += groupColumn.getSize(header.getTable()).height;
                }
            }
            height = Math.max(height, cHeight);
        }
        return height;
    }


    private Dimension createGroupHeaderSize(long width) {
        TableColumnModel columnModel = header.getColumnModel();
        width += columnModel.getColumnMargin() * columnModel.getColumnCount();
        if (width > Integer.MAX_VALUE) {
            width = Integer.MAX_VALUE;
        }
        return new Dimension((int)width, getGroupHeaderHeight());
    }


    @Override
    public Dimension getPreferredSize(JComponent component) {
        long width = 0;
        Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn aColumn = enumeration.nextElement();
            width = width + aColumn.getPreferredWidth();
        }
        return createGroupHeaderSize(width);
    }
}