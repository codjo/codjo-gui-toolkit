/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 * Classe permettant de créer un filtre sur les fichiers dans un JFileChooser.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
class ExtensionFileFilter extends FileFilter {
    private String label = "texte";
    private String extension = "txt";

    ExtensionFileFilter() {}


    /**
     * Construit un filtre pour un type de fichier
     *
     * @param label Le label du type de fichier
     * @param extension une liste d'extension possible (ex : "txt,TXT,config")
     */
    ExtensionFileFilter(String label, String extension) {
        this.label = label;
        this.extension = extension;
    }

    /**
     * Retourne la description du filtre (l'affichage dans le combo des filtres).
     *
     * @return La description du filtre.
     */
    public String getDescription() {
        return "Fichiers " + label + " (*." + extension + ")";
    }


    public String getFirstAllowedExtension() {
        if (this.extension.indexOf(',') != -1) {
            return this.extension.substring(0, this.extension.indexOf(','));
        }
        else {
            return this.extension;
        }
    }


    /**
     * Détermine si le fichier sélectionné est acceptable par le filtre.
     *
     * @param file Le fichier.
     *
     * @return True si acceptable, False sinon.
     */
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String currentFileExtension = FileChooserUtils.getExtension(file);

        if (currentFileExtension != null) {
            return this.extension.indexOf(currentFileExtension) != -1;
        }

        return false;
    }
}
