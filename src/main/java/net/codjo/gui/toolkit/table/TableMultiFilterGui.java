/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.table;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
/**
 * Liste permettant a un utilisateur de selectionner des filtres sur un tableau. Cette
 * JListe est connecte a un <code>TableFilter</code> (responsable du filtre). La liste
 * se rempli avec les informations contenu dans le model.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 *
 * @see TableFilter
 */
public class TableMultiFilterGui extends JTable {
    private InnerModel model = new InnerModel();
    private TableFilterListener tableListener = new TableFilterListener();

    public TableMultiFilterGui() {
        setModel(model);
        getColumn(getModel().getColumnName(0)).setMinWidth(25);
        getColumn(getModel().getColumnName(0)).setMaxWidth(25);
    }

    public void setTableFilter(TableFilter filterModel, int column) {
        if (model.tableFilterModel != null) {
            model.tableFilterModel.removePropertyChangeListener(model.filteredColumn,
                tableListener);
            model.tableFilterModel.getModel().removeTableModelListener(tableListener);
        }
        model.init(filterModel, column);
        filterModel.addPropertyChangeListener(column, tableListener);
        filterModel.getModel().addTableModelListener(tableListener);
    }


    /**
     * Retourne la colonne filtrée.
     *
     * @return The FilteredColumn value
     */
    public int getFilteredColumn() {
        return model.filteredColumn;
    }

    private static class InnerModel extends AbstractTableModel {
        private static final String[] COLUMN_NAMES = {"Sel", "Valeur"};
        private static final Class[] COLUMN_CLASS = {Boolean.class, Object.class};
        private TableFilter tableFilterModel = null;
        private int filteredColumn = 0;
        private java.util.List valueList = new java.util.ArrayList();

        InnerModel() {}

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                if (tableFilterModel.containsFilterValue(filteredColumn,
                            valueList.get(rowIndex))) {
                    tableFilterModel.removeFilter(filteredColumn, valueList.get(rowIndex));
                }
                else {
                    tableFilterModel.addFilter(filteredColumn, valueList.get(rowIndex));
                }
                this.fireTableCellUpdated(rowIndex, columnIndex);
            }
        }


        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }


        public Class getColumnClass(int columnIndex) {
            return COLUMN_CLASS[columnIndex];
        }


        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }


        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }


        public int getRowCount() {
            return valueList.size();
        }


        public Object getValueAt(int row, int column) {
            Object value = valueList.get(row);
            switch (column) {
                case 0:
                    if (tableFilterModel.containsFilterValue(filteredColumn, value)) {
                        return Boolean.TRUE;
                    }
                    else {
                        return Boolean.FALSE;
                    }
                case 1:
                    return value;
                default:
                    return null;
            }
        }


        public void init(TableFilter tfm, int filteredCol) {
            this.filteredColumn = filteredCol;
            this.tableFilterModel = tfm;
            fillTable();
        }


        public void fillTable() {
            boolean hasNullValue = false;
            Set set = new TreeSet();
            TableModel filterModel = tableFilterModel.getModel();
            for (int i = 0; i < filterModel.getRowCount(); i++) {
                if (filterModel.getValueAt(i, filteredColumn) != null) {
                    set.add(filterModel.getValueAt(i, filteredColumn));
                }
                else {
                    hasNullValue = true;
                }
            }
            valueList.clear();
            if (hasNullValue) {
                valueList.add(null);
            }
            valueList.addAll(set);
            fireTableDataChanged();
        }
    }


    /**
     * Ecoute les changements de Filtre, et de contenu.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    private class TableFilterListener implements PropertyChangeListener,
        TableModelListener {
        public void tableChanged(TableModelEvent event) {
            model.fillTable();
        }


        public void propertyChange(PropertyChangeEvent evt) {
            repaint();
        }
    }
}
