/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
/*
 * Project: ALIS
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.table;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
public class TableMap extends AbstractTableModel implements TableModelListener {
    private TableModel model;


    public void setModel(TableModel model) {
        if (this.model != null) {
            this.model.removeTableModelListener(this);
        }
        this.model = model;
        if (model == null) {
            return;
        }
        model.addTableModelListener(this);
    }


    @Override
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }


    public TableModel getModel() {
        return model;
    }


    // By default, Implement TableModel by forwarding all messages
    // to the model.
    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }


    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }


    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }


    @Override
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }


    @Override
    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn);
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return model.isCellEditable(row, column);
    }


    //
    // Implementation of the TableModelListener interface,
    //
    // By default forward all events to all the listeners.
    public void tableChanged(TableModelEvent event) {
        fireTableChanged(event);
    }
}
