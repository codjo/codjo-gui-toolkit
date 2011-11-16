/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.log4j.Logger;
/**
 * Renderer table pour des données String a afficher comme un numérique.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class NumberRenderer extends DefaultTableCellRenderer {
    private static final Logger APP = Logger.getLogger(NumberRenderer.class);
    private NumberFormat format = null;


    public NumberRenderer() {
        setHorizontalAlignment(JTextField.RIGHT);
    }


    public NumberRenderer(NumberFormat format) {
        this.format = format;
        setHorizontalAlignment(JTextField.RIGHT);
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        return super.getTableCellRendererComponent(table, format(value), isSelected,
                                                   hasFocus, row, column);
    }


    @Override
    protected void setValue(Object value) {
        if (value != null && "null".equals(value.toString())) {
            super.setValue("");
        }
        else {
            super.setValue(value);
        }
    }


    private Object format(Object value) {
        if (value != null) {
            String strValue = value.toString();
            if ("null".equals(strValue) || "".equals(strValue)) {
                return "";
            }

            if (format != null) {
                try {
                    return format.format(new BigDecimal(strValue));
                }
                catch (Exception ex) {
                    APP.error("Erreur lors du formatage de " + value);
                }
            }
        }
        return value;
    }
}
