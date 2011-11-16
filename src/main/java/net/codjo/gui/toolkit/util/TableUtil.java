package net.codjo.gui.toolkit.util;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
/**
 *
 */
public class TableUtil {
    public static final String COLUMN_WIDTH_PROPERTY = "preferredWidth";
    public static final int MIN_COLUMN_WIDTH = 15;
    public static final int MAX_COLUMN_WIDTH = Integer.MAX_VALUE;
    public static final int CELL_MARGIN = 5;


    private TableUtil() {
    }


    public static void synchronizeTableColumns(final JTable... tables) {
        for (JTable table : tables) {
            synchronizeColumns(table, tables);
        }
    }


    public static void registerColumnSizeListener(JTable table, PropertyChangeListener listener) {
        for (Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
             columns.hasMoreElements();) {
            TableColumn column = columns.nextElement();
            column.addPropertyChangeListener(listener);
        }
    }


    public static void setColumnWidth(JTable table, int graphicalColumnIndex, int width) {
        TableColumn tableColumn = getTableColumn(table, graphicalColumnIndex);
        if (tableColumn.getPreferredWidth() != width) {
            tableColumn.setPreferredWidth(width);
        }
    }


    public static TableColumn getTableColumn(JTable table, int graphicalColumnIndex) {
        return table.getColumnModel().getColumn(graphicalColumnIndex);
    }


    public static Component getRenderedComponentAt(JTable table, int rowIndex, int columnIndex) {
        return table.
              getCellRenderer(rowIndex, columnIndex).
              getTableCellRendererComponent(table,
                                            table.getValueAt(rowIndex, columnIndex),
                                            table.isCellSelected(rowIndex, columnIndex),
                                            false,
                                            rowIndex, columnIndex);
    }


    public static void stopCellEditing(JTable table) {
        if (table.isEditing()) {
            int[] selection = table.getSelectedRows();
            table.getCellEditor().stopCellEditing();
            if (selection.length > 0) {
                selectRows(table, selection);
            }
        }
    }


    public static void cancelCellEditing(JTable table) {
        if (table.isEditing()) {
            int[] selection = table.getSelectedRows();
            table.getCellEditor().cancelCellEditing();
            if (selection.length > 0) {
                selectRows(table, selection);
            }
        }
    }


    public static void selectRows(JTable table, int[] rowIndexes) {
        table.getSelectionModel().setValueIsAdjusting(true);
        try {
            table.clearSelection();
            for (int row : rowIndexes) {
                table.addRowSelectionInterval(row, row);
            }
            if (table.getCellSelectionEnabled()) {
                table.setColumnSelectionInterval(0, table.getColumnCount() - 1);
            }
        }
        finally {
            table.getSelectionModel().setValueIsAdjusting(false);
        }
    }


    public static void configureTableCellEditing(JTable table,
                                                 AbstractButton[] buttonsForStopEditing,
                                                 AbstractButton[] buttonsForCancelEditing) {
        configureStopCellEditing(table, buttonsForStopEditing);
        configureCancelCellEditing(table, buttonsForCancelEditing);
    }


    public static void configureStopCellEditing(final JTable table, AbstractButton... buttons) {
        for (AbstractButton button : buttons) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    stopCellEditing(table);
                }
            });
        }
    }


    public static void configureCancelCellEditing(final JTable table, AbstractButton... buttons) {
        for (AbstractButton button : buttons) {
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    cancelCellEditing(table);
                }
            });
        }
    }


    public static void synchronizeHorizontalScrollbars(final JViewport viewportA, final JViewport viewportB) {
        synchronizeHorizontalScrollbar(viewportA, viewportB);
        synchronizeHorizontalScrollbar(viewportB, viewportA);
    }


    private static void synchronizeColumns(final JTable masterTable, final JTable... slaveTables) {
        registerColumnSizeListener(masterTable, new ResizeColumnListener(masterTable, slaveTables));

        masterTable.getColumnModel()
              .addColumnModelListener(new MoveColumnModelListener(masterTable, slaveTables));
    }


    private static void synchronizeHorizontalScrollbar(final JViewport source, final JViewport target) {
        source.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                double viewportSourcePosX = source.getViewPosition().getX();
                Point viewportTargetPos = target.getViewPosition();
                double viewportTargetPosX = viewportTargetPos.getX();
                if (viewportSourcePosX == viewportTargetPosX) {
                    return;
                }
                target.setViewPosition(new Point(((int)viewportSourcePosX),
                                                 ((int)viewportTargetPos.getY())));
                target.repaint();
            }
        });
    }


    public static void synchronizeTableSelection(int selectionMode, final JTable... tables) {
        for (JTable table : tables) {
            table.getSelectionModel().setSelectionMode(selectionMode);
            synchronizeTableSelections(table, tables);
        }
    }


    private static void synchronizeTableSelections(final JTable masterTable, final JTable... slaveTables) {
        SyncSelectionListener selectionListener = new SyncSelectionListener(masterTable, slaveTables);
        masterTable.getSelectionModel().addListSelectionListener(selectionListener);
    }


    private static class MoveColumnModelListener implements TableColumnModelListener {
        private final JTable[] slaveTables;
        private final JTable masterTable;


        private MoveColumnModelListener(JTable masterTable, JTable... slaveTables) {
            this.slaveTables = slaveTables;
            this.masterTable = masterTable;
        }


        public void columnMoved(TableColumnModelEvent evt) {
            int fromIndex = evt.getFromIndex();
            int toIndex = evt.getToIndex();
            if (fromIndex != toIndex) {
                for (JTable slaveTable : slaveTables) {
                    if (masterTable != slaveTable) {
                        TableColumnModel columnModel = slaveTable.getColumnModel();
                        if (columnModel instanceof DefaultTableColumnModel) {
                            TableColumnModelListener moveListener = getMoveColumnListener(
                                  (DefaultTableColumnModel)columnModel);
                            columnModel.removeColumnModelListener(moveListener);

                            slaveTable.getColumnModel().moveColumn(fromIndex, toIndex);

                            if (moveListener != null) {
                                columnModel.addColumnModelListener(moveListener);
                            }
                        }
                    }
                }
            }
        }


        private TableColumnModelListener getMoveColumnListener(DefaultTableColumnModel columnModel) {
            for (TableColumnModelListener listener : columnModel.getColumnModelListeners()) {
                if (listener instanceof MoveColumnModelListener) {
                    return listener;
                }
            }
            return null;
        }


        public void columnMarginChanged(ChangeEvent evt) {
        }


        public void columnSelectionChanged(ListSelectionEvent evt) {
        }


        public void columnAdded(TableColumnModelEvent evt) {
        }


        public void columnRemoved(TableColumnModelEvent evt) {
        }
    }
    private static class ResizeColumnListener implements PropertyChangeListener {
        private final JTable masterTable;
        private final JTable[] slaveTables;


        private ResizeColumnListener(JTable masterTable, JTable... slaveTables) {
            this.masterTable = masterTable;
            this.slaveTables = slaveTables;
        }


        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(COLUMN_WIDTH_PROPERTY)) {
                TableColumn column = (TableColumn)evt.getSource();
                int width = (Integer)evt.getNewValue();
                int columnIndex = column.getModelIndex();
                for (JTable slaveTable : slaveTables) {
                    if (masterTable != slaveTable) {
                        setColumnWidth(
                              slaveTable,
                              columnIndex, width);
                    }
                }
            }
        }
    }

    private static class SyncSelectionListener implements ListSelectionListener {
        private final JTable masterTable;
        private final JTable[] slaveTables;


        private SyncSelectionListener(JTable masterTable, JTable... slaveTables) {
            this.masterTable = masterTable;
            this.slaveTables = slaveTables;
        }


        public void valueChanged(ListSelectionEvent evt) {
            ListSelectionModel listSelectionModel = (ListSelectionModel)evt.getSource();
            int index0 = listSelectionModel.getAnchorSelectionIndex();
            int index1 = listSelectionModel.getLeadSelectionIndex();
            for (JTable slaveTable : slaveTables) {
                if (masterTable != slaveTable) {
                    slaveTable.getSelectionModel().setSelectionInterval(index0, index1);
                }
            }
        }
    }
}
