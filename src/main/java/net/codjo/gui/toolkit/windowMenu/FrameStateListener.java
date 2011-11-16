/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
/**
 * Ecoute l'activation d'un <code>JInternalFrame</code> et selectionne le
 * <code>WindowMenuItem</code>.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 *
 * @see WindowMenuItem
 */
class FrameStateListener extends InternalFrameAdapter {
    private WindowMenuItem item = null;
    private WindowMenuManager manager = null;

    FrameStateListener(WindowMenuManager manager, WindowMenuItem item) {
        if (manager == null || item == null) {
            throw new IllegalArgumentException();
        }
        this.manager = manager;
        this.item = item;
    }

    public void internalFrameActivated(InternalFrameEvent event) {
        item.setState(true);
    }


    public void internalFrameClosed(InternalFrameEvent event) {
        manager.removeItem(item);
    }


    public void internalFrameDeactivated(InternalFrameEvent event) {
        item.setState(false);
    }
}
