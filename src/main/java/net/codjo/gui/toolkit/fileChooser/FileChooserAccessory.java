/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
/*
 * Project: PENELOPE
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.Dimension;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
/**
 * Cette classe construit un panel en lui incorporant le ou les accessoires désirés
 * (Recherche de fichiers et/ou Prévisualisation de fichiers texte).
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
class FileChooserAccessory extends JTabbedPane {
    private JFileChooser chooser;

    /**
     * Constructeur.
     *
     * @param fc Le fileChooser
     *
     * @throws IllegalArgumentException lorsque JFileChooser est null
     */
    FileChooserAccessory(JFileChooser fc) {
        if (fc == null) {
            throw new IllegalArgumentException();
        }
        chooser = fc;
    }

    /**
     * Construction du panel contenant les accessoires désirés.
     *
     * @param subAccessories les accessoires
     */
    public void initUtils(JPanel[] subAccessories) {
        super.setBorder(new TitledBorder("Outils"));

        for (JPanel subAccessory : subAccessories) {
            addTab(subAccessory.getName(), subAccessory);
        }

        chooser.setPreferredSize(new Dimension(600, 350));
    }
}
