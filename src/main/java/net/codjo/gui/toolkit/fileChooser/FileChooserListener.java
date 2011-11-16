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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JFileChooser;
/**
 * Cette classe construit un listener sur un JFileChooser. Ce listener permet lorsqu'on
 * clique sur un dossier, de ne pas afficher son nom dans la zone de saisie du fichier.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
class FileChooserListener implements PropertyChangeListener {
    private File file = null;
    private JFileChooser fileChooser = null;

    /**
     * Constructeur.
     *
     * @param fc Le fileChooser
     */
    FileChooserListener(JFileChooser fc) {
        fileChooser = fc;
    }

    public void propertyChange(PropertyChangeEvent event) {
        String prop = event.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            file = (File)event.getNewValue();
            if (file != null && file.isDirectory()) {
                fileChooser.setSelectedFile(new File(""));
            }
        }
    }
}
