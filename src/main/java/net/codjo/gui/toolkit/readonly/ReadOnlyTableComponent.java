package net.codjo.gui.toolkit.readonly;

import net.codjo.gui.toolkit.util.GuiUtil;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 */
public class ReadOnlyTableComponent extends AbstractReadOnlyComponent {
    private JTable tableComponent;

    private Map<TableColumn, TableCellRenderer> initialRenderers
          = new HashMap<TableColumn, TableCellRenderer>();


    public ReadOnlyTableComponent(JTable table, ReadOnlyManager readOnlyManager) {
        super(table, readOnlyManager, false, null);
        this.tableComponent = table;
    }


    public ReadOnlyTableComponent(JTable table,
                                  ReadOnlyManager readOnlyManager,
                                  ReadOnlyValueSetter setter) {
        super(table, readOnlyManager, true, setter);
        this.tableComponent = table;
    }


    public void setReadOnly(boolean readonly, final boolean applydefaultvalue) {
        readOnly = readonly;
        if (readonly) {
            initReadOnlyTableCellRenderer();
        }
        else {
            removeReadOnlyTableCellRenderer();
        }
        if (tableComponent.isEnabled() == readOnly) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tableComponent.setEnabled(!readOnly);
                    if (applydefaultvalue) {
                        setDefaultValue(tableComponent);
                    }
                }
            });
        }
    }


    private void initReadOnlyTableCellRenderer() {
        for (int i = 0; i < tableComponent.getColumnCount(); i++) {
            TableColumn column = tableComponent.getColumnModel().getColumn(i);
            TableCellRenderer cellRenderer = column.getCellRenderer();
            if (cellRenderer == null) {
                cellRenderer = tableComponent.getDefaultRenderer(tableComponent.getColumnClass(i));
            }
            if (!ReadOnlyTableCellRenderer.class.isInstance(cellRenderer)) {
                initialRenderers.put(column, cellRenderer);
            }
            column.setCellRenderer(new ReadOnlyTableCellRenderer(cellRenderer));
        }
    }


    private void removeReadOnlyTableCellRenderer() {
        for (Map.Entry<TableColumn, TableCellRenderer> entry : initialRenderers.entrySet()) {
            TableColumn column = entry.getKey();
            TableCellRenderer renderer = entry.getValue();
            if (DefaultTableCellRenderer.class.isInstance(renderer)) {
                ((DefaultTableCellRenderer)renderer).updateUI();
            }
            column.setCellRenderer(renderer);
        }
    }


    private class ReadOnlyTableCellRenderer extends DefaultTableCellRenderer {
        private TableCellRenderer cellRenderer;


        private ReadOnlyTableCellRenderer(TableCellRenderer cellRenderer) {
            this.cellRenderer = cellRenderer;
        }


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component cellRendererComponent;
            if (cellRenderer == null) {
                cellRendererComponent =
                      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            else {
                cellRendererComponent =
                      cellRenderer
                            .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }

            if (!JLabel.class.isInstance(cellRendererComponent)) {
                cellRendererComponent.setEnabled(false);
            }
            cellRendererComponent.setBackground(GuiUtil.DISABLED_BACKGROUND_COLOR);
            cellRendererComponent.setForeground(GuiUtil.DISABLED_FOREGROUND_COLOR);
            return cellRendererComponent;
        }
    }
}
