/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableCellRenderer;
/**
 * Renderer pour les dates. Conversion a partir de String au format SQL.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
public class DateRenderer extends DefaultTableCellRenderer {
    private DateFormat formatter;

    public DateRenderer() {
        this("dd/MM/yyyy");
    }


    public DateRenderer(String pattern) {
        this(new SimpleDateFormat(pattern));
    }


    public DateRenderer(DateFormat formatter) {
        this.formatter = formatter;
    }

    public void setValue(Object value) {
        if ("null".equals(value)) {
            setText("");
        }
        else {
            setText(formatter.format(java.sql.Date.valueOf((String)value)));
        }
    }
}
