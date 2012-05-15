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
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.UIManager;
/**
 * Classe d'utilitaires sur les "FileChooser".
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
public final class FileChooserUtils {

    private static Locale defaultLanguage = Locale.FRANCE;


    private FileChooserUtils() {
    }


    /**
     * Retourne l'extension d'un fichier.
     *
     * @param file Le fichier.
     *
     * @return L'extension de ce fichier.
     */
    public static String getExtension(File file) {
        String ext = null;
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            ext = name.substring(dotIndex + 1).toLowerCase();
        }
        return ext;
    }


    public static void setUILanguage(Locale language) {
        defaultLanguage = language;
    }


    public static Locale getUILanguage() {
        return defaultLanguage;
    }


    public static void initUILanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("net.codjo.gui.toolkit.fileChooser.FileChooser", defaultLanguage);

        UIManager.put("FileChooser.lookInLabelText", rb.getString("lookInLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText",
                      rb.getString("filesOfTypeLabelText"));
        UIManager.put("FileChooser.upFolderToolTipText",
                      rb.getString("upFolderToolTipText"));
        UIManager.put("FileChooser.fileNameLabelText", rb.getString("fileNameLabelText"));
        UIManager.put("FileChooser.homeFolderToolTipText",
                      rb.getString("homeFolderToolTipText"));
        UIManager.put("FileChooser.newFolderToolTipText",
                      rb.getString("newFolderToolTipText"));
        UIManager.put("FileChooser.listViewButtonToolTipText",
                      rb.getString("listViewButtonToolTipText"));
        UIManager.put("FileChooser.detailsViewButtonToolTipText",
                      rb.getString("detailsViewButtonToolTipText"));
        UIManager.put("FileChooser.saveButtonText", rb.getString("saveButtonText"));
        UIManager.put("FileChooser.openButtonText", rb.getString("openButtonText"));
        UIManager.put("FileChooser.cancelButtonText", rb.getString("cancelButtonText"));
        UIManager.put("FileChooser.updateButtonText", rb.getString("updateButtonText"));
        UIManager.put("FileChooser.helpButtonText", rb.getString("helpButtonText"));
        UIManager.put("FileChooser.saveButtonToolTipText",
                      rb.getString("saveButtonToolTipText"));
        UIManager.put("FileChooser.openButtonToolTipText",
                      rb.getString("openButtonToolTipText"));
        UIManager.put("FileChooser.cancelButtonToolTipText",
                      rb.getString("cancelButtonToolTipText"));
        UIManager.put("FileChooser.updateButtonToolTipText",
                      rb.getString("updateButtonToolTipText"));
        UIManager.put("FileChooser.helpButtonToolTipText",
                      rb.getString("helpButtonToolTipText"));
        UIManager.put("FileChooser.acceptAllFileFilterText",
                      rb.getString("acceptAllFileFilterText"));
    }
}
