/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.date.DateField;
import net.codjo.gui.toolkit.swing.callback.CellEditorCallBack;
import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
/**
 * Editeur de combo/table pour des données de type date.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class DateFieldEditor extends AbstractCellEditor implements TableCellEditor {
    private DateField editor;


    public DateFieldEditor() {
        this(true);
    }


    public DateFieldEditor(boolean showCalendar) {
        this(showCalendar, null);
    }


    public DateFieldEditor(boolean showCalendar, String dateFieldName) {
        this(showCalendar, dateFieldName, new JButton());
    }


    public DateFieldEditor(boolean showCalendar, String dateFieldName, JButton button) {
        editor = new DateField(showCalendar, new CellEditorCallBack(this), button);
        editor.setName(dateFieldName);
    }


    public void setEditor(DateField editor) {
        this.editor = editor;
    }


    public Object getCellEditorValue() {
        Date date = editor.getDate();
        if (date != null) {
            return new java.sql.Date(date.getTime()).toString();
        }
        else {
            return "null";
        }
    }


    public Component getTableCellEditorComponent(JTable table, final Object value,
                                                 boolean isSelected, int row, int column) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setDate(value);
            }
        });

        return editor;
    }


    private void setDate(Object value) {
        if ("null".equals(value)) {
            editor.setDate(null);
        }
        else {
            editor.setDate(java.sql.Date.valueOf((String)value));
        }
    }
}
