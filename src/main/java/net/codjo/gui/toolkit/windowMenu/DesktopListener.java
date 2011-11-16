/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.JInternalFrame;
/**
 * Ecoute les ajout des <code>JInternalFrame</code> dans le <code>JDesktopPane</code> et
 * previent le <code>WindowMenuManager</code> pour mise a jour des items.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 *
 * @see WindowMenuManager
 */
class DesktopListener extends ContainerAdapter {
    private WindowMenuManager manager = null;

    DesktopListener(WindowMenuManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException();
        }
        this.manager = manager;
    }

    public void componentAdded(ContainerEvent event) {
        if (event.getChild() instanceof JInternalFrame) {
            manager.addItem((JInternalFrame)event.getChild());
        }
    }
}
