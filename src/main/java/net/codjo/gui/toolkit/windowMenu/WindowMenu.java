/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
/**
 * Extension de la classe <code>JMenu</code>.
 * 
 * <p>
 * Ajouté a une <code>JMenuBar</code> cette classe permet d'afficher la liste des
 * <code>JInternalFrame</code>s comme des <code>JCheckBoxMenuItem</code>s. L'action
 * associe a chaque item a pour effet de donner le focus a son JInternalFrame.
 * </p>
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 *
 * @see WindowMenuManager
 * @see WindowMenuItem
 */
public class WindowMenu extends JMenu {
    /**
     * Construit le menu et ajoute les items <code>WindowMenuItem</code>s.
     *
     * @param desktop Le conteneur des JInternalFrame
     */
    public WindowMenu(JDesktopPane desktop) {
        super("Fenêtre");
        setMnemonic(KeyEvent.VK_F);
        new WindowMenuManager(desktop, this);
    }
}
