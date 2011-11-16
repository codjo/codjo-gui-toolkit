/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.awt.Color;
import java.awt.Component;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
/**
 * Renderer de liste/table base sur une Map de traduction.
 *
 * @author $Author: lopezla $
 * @version $Revision: 1.7 $
 */
public class GenericRenderer implements ListCellRenderer, TableCellRenderer, Comparator {
    private DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();
    private DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
    private Map traductTable = new HashMap();

    public GenericRenderer(Map traductTable) {
        if (traductTable == null) {
            throw new IllegalArgumentException(
                "La table de traduction n'est pas initialisée !");
        }
        this.traductTable = traductTable;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
        Object translated = translateValue(value);
        listCellRenderer.getListCellRendererComponent(list, translated, index,
            isSelected, cellHasFocus);
        manageNoTranslation(listCellRenderer, value, translated, isSelected);
        return listCellRenderer;
    }


    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
        Object translated = translateValue(value);
        tableCellRenderer.getTableCellRendererComponent(table, translated, isSelected,
            hasFocus, row, column);
        manageNoTranslation(tableCellRenderer, value, translated, isSelected);
        return tableCellRenderer;
    }


    public int compare(Object o1, Object o2) {
        Comparable t1 = (Comparable)translateValue(o1);
        return t1.compareTo(translateValue(o2));
    }


    private static void manageNoTranslation(JLabel field, Object value,
        Object translated, boolean isSelected) {
        if (translated == value) {
            field.setForeground(Color.red);
        }
        else if (isSelected) {
            field.setForeground(UIManager.getColor("Table.selectionForeground"));
        }
        else {
            field.setForeground(UIManager.getColor("Table.foreground"));
        }
    }


    private Object translateValue(Object value) {
        if (isNullValue(value)) {
            return "";
        }
        if (traductTable.containsKey(value)) {
            return (String)traductTable.get(value);
        }
        return value;
    }


    private boolean isNullValue(Object value) {
        return (value == null || "null".equals(value));
    }
}
