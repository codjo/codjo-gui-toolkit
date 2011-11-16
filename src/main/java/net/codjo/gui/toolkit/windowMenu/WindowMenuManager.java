/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
/**
 * Classe responsable des ajout/suppression des items dans le menu.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
class WindowMenuManager {
    private JMenu menu = null;
    private JMenuItem nextWindows = null;

    WindowMenuManager(JDesktopPane desktop, JMenu menu) {
        if (desktop == null || menu == null) {
            throw new IllegalArgumentException();
        }
        this.menu = menu;
        initMenu(desktop);
    }

    void activateFrame(JInternalFrame frame) throws PropertyVetoException {
        if (frame.isSelected()) {
            frame.getToolkit().beep();
            frame.setSelected(false);
        }
        frame.setIcon(false);
        frame.setSelected(true);
    }


    void addItem(JInternalFrame frame) {
        int idx = getIndexItem(frame);
        if (idx < 0) {
            menu.add(getNewItem(frame));
            updateStateMenu();
        }
    }


    WindowMenuItem getNewItem(JInternalFrame frame) {
        return new WindowMenuItem(this, frame, menu.getItemCount() + 1);
    }


    void removeItem(WindowMenuItem item) {
        menu.remove(nextWindows);
        menu.remove(item);
        updateStateMenu();
    }


    private int getIndexItem(JInternalFrame frame) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            if (menu.getItem(i) instanceof WindowMenuItem
                    && ((WindowMenuItem)menu.getItem(i)).getFrame().equals(frame)) {
                return i;
            }
        }
        return -1;
    }


    private void initMenu(JDesktopPane desktop) {
        for (int i = 0; i < desktop.getAllFrames().length; i++) {
            menu.add(getNewItem(desktop.getAllFrames()[i]));
        }
        this.nextWindows = new JMenuItem("Suite des fenêtres...");
        nextWindows.setMnemonic(java.awt.event.KeyEvent.VK_S);
        // init temp
        nextWindows.setEnabled(false);
        // fin temp
        updateStateMenu();

        DesktopListener desktopListener = new DesktopListener(this);
        desktop.addContainerListener(desktopListener);
    }


    private void updateStateMenu() {
        int itemCount = menu.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (menu.getItem(i) instanceof WindowMenuItem) {
                ((WindowMenuItem)menu.getItem(i)).setTextAndMnemonic(i + 1);
            }
        }
        if (itemCount > 9) {
//            menu.add(new JSeparator(SwingConstants.HORIZONTAL));
            menu.add(nextWindows);
        }
        menu.setEnabled(itemCount > 0);
    }
}
