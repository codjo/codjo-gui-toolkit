/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.path;
import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 * Filtre les fichiers en fonction d'un libellé.
 *
 * @author $Author: gaudefr $
 * @version 2.05-SNAPSHOT
 */
class LabelFilter extends FileFilter {
    private String fileType = "texte";
    private String extension = "txt";

    LabelFilter() {}


    LabelFilter(String fileTypeLabel, String extension) {
        this.fileType = fileTypeLabel;
        this.extension = extension;
    }

    public String getDescription() {
        return fileType + " (*." + extension + ")";
    }


    public String getFirstAllowedExtension() {
        if (this.extension.indexOf(',') != -1) {
            return this.extension.substring(0, this.extension.indexOf(','));
        }
        else {
            return this.extension;
        }
    }


    public boolean accept(File file) {
        return file.getName() != null && file.getName().indexOf(fileType) > -1;
    }
}
