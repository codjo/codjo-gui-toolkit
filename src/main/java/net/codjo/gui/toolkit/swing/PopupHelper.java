/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
/**
 *
 */
public abstract class PopupHelper extends MouseAdapter {
    private JPopupMenu popup;
    private JTable table;

    protected PopupHelper(JPopupMenu popup, JTable table) {
        this.popup = popup;
        this.table = table;
    }

    public void mouseClicked(MouseEvent event) {
        tableMouseClicked(event);
    }


    public void mousePressed(MouseEvent event) {
        tableMousePressed(event);
    }


    public void mouseReleased(MouseEvent event) {
        maybeShowPopup(event);
    }


    void tableMouseClicked(MouseEvent event) {
        Object source = event.getSource();
        if (event.getClickCount() == 2 && source instanceof JMenuItem) {
            ((JMenuItem)source).getAction().actionPerformed(new ActionEvent(this, 0,
                    "Modification"));
        }
    }


    void tableMousePressed(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            int row = table.rowAtPoint(event.getPoint());
            if (row != -1) {
                table.setRowSelectionInterval(row, row);
            }
        }
        maybeShowPopup(event);
    }


    private void maybeShowPopup(MouseEvent event) {
        if (event.isPopupTrigger() && mayShowPopup()) {
            popup.show(event.getComponent(), event.getX(), event.getY());
        }
        else {
            popup.setVisible(false);
        }
    }


    public abstract boolean mayShowPopup();
}
