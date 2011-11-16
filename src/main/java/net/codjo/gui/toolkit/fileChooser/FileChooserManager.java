/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
/**
 * Classe gérant l'affichage d'un JFileChooser
 */
public final class FileChooserManager {
    private static final String TXT = "txt";
    private boolean withAccessories = true;
    private String title;
    private int selectionMode = JFileChooser.FILES_AND_DIRECTORIES;
    private File defaultFile;
    private List<ExtensionFileFilter> extensionFileFilters = new ArrayList<ExtensionFileFilter>();
    private SecurityManager securityManager;


    /**
     * Classe gérant l'affichage d'un JFileChooser pour l'export de données.
     *
     * @param fileName     Le nom du fichier
     * @param chooserTitle Le titre du fileChooser
     *
     * @return Le path complet du fichier
     */
    public static String showChooserForExport(String fileName, String chooserTitle) {
        return showChooserForExport(fileName, chooserTitle, null);
    }


    public static String showChooserForExport(String fileName, String chooserTitle, Component parent) {
        File file = showChooserForSave(((fileName != null) ? new File(fileName) : null),
                                       chooserTitle, "texte", TXT, parent);
        if (file != null) {
            return file.getAbsolutePath();
        }
        else {
            return null;
        }
    }


    /**
     * Classe gérant l'affichage d'un JFileChooser pour l'export de données.
     *
     * @param defaultDestFile Le fichier
     * @param chooserTitle    Le titre du fileChooser
     * @param fileTypeLabel   Le label du type de fichier
     * @param extension       une liste d'extension possible (ex : "txt,TXT,config")
     *
     * @return Le path complet du fichier
     */
    public static File showChooserForSave(File defaultDestFile, String chooserTitle,
                                          String fileTypeLabel, String extension) {
        return showChooserForSave(defaultDestFile, chooserTitle, fileTypeLabel, extension, null);
    }


    public static File showChooserForSave(File defaultDestFile, String chooserTitle,
                                          String fileTypeLabel, String extension, Component parent) {
        FileChooserManager manager = new FileChooserManager();
        manager.setDefaultFile(defaultDestFile);
        manager.setTitle(chooserTitle);
        manager.setSelectionMode(JFileChooser.FILES_ONLY);
        manager.addExtensionFileFilter(fileTypeLabel, extension);
        return manager.showChooserForSave(parent);
    }


    /**
     * Classe gérant l'affichage d'un JFileChooser pour un log.
     *
     * @param currentLogFile Le fichier courant
     *
     * @return Le fichier choisi
     */
    public static File showChooserForLog(File currentLogFile) {
        return showChooserForSave(currentLogFile, "Selection du Log", "log", TXT);
    }


    public static File showChooserForLog(File currentLogFile, Component parent) {
        return showChooserForSave(currentLogFile, "Selection du Log", "log", TXT, parent);
    }


    /**
     * Classe gérant l'affichage d'un JFileChooser pour la selection d'un fichier sur disque.
     *
     * @param fileName     Le nom du fichier (positionne le fileChooser dessus)
     * @param chooserTitle Le titre du fileChooser
     *
     * @return Le path complet du fichier
     */
    public static String showChooserForOpen(String fileName, String chooserTitle) {
        return showChooserForOpen(fileName, chooserTitle, JFileChooser.FILES_ONLY);
    }


    public static String showChooserForOpen(String fileName, String chooserTitle, Component parent) {
        return showChooserForOpen(fileName, chooserTitle, JFileChooser.FILES_ONLY, parent);
    }


    /**
     * Classe gérant l'affichage d'un JFileChooser pour la selection d'un fichier sur disque.
     *
     * @param fileName     Le nom du fichier (positionne le fileChooser dessus)
     * @param chooserTitle Le titre du fileChooser
     * @param mode         JFileChooser.FILES_ONLY, JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_AND_DIRECTORIES
     *
     * @return Le path complet du fichier
     */
    public static String showChooserForOpen(String fileName, String chooserTitle, int mode) {
        return showChooserForOpen(fileName, chooserTitle, mode, "texte", TXT);
    }


    public static String showChooserForOpen(String fileName,
                                            String chooserTitle,
                                            int mode,
                                            Component parent) {
        return showChooserForOpen(fileName, chooserTitle, mode, "texte", TXT, parent);
    }


    /**
     * Classe gérant l'affichage d'un JFileChooser pour la selection d'un fichier sur disque.
     *
     * @param fileName      Le nom du fichier (positionne le fileChooser dessus)
     * @param chooserTitle  Le titre du fileChooser
     * @param mode          JFileChooser.FILES_ONLY, JFileChooser.DIRECTORIES_ONLY,
     *                      JFileChooser.FILES_AND_DIRECTORIES
     * @param fileTypeLabel Le label du type de fichier
     * @param extension     une liste d'extension possible (ex : "txt,TXT,config")
     *
     * @return Le path complet du fichier
     */
    public static String showChooserForOpen(String fileName, String chooserTitle,
                                            int mode, String fileTypeLabel, String extension) {
        return showChooserForOpen(fileName, chooserTitle, mode, fileTypeLabel, extension, null);
    }


    public static String showChooserForOpen(String fileName, String chooserTitle,
                                            int mode,
                                            String fileTypeLabel,
                                            String extension,
                                            Component parent) {
        FileChooserManager manager = new FileChooserManager();
        manager.setDefaultFile(new File(fileName));
        manager.setTitle(chooserTitle);
        manager.setSelectionMode(mode);
        manager.addExtensionFileFilter(fileTypeLabel, extension);
        File file = manager.showChooserForOpen(parent);
        return file == null ? null : file.getAbsolutePath();
    }


    public void setWithAccessories(boolean withAccessories) {
        this.withAccessories = withAccessories;
    }


    public boolean isWithAccessories() {
        return withAccessories;
    }


    public void setDefaultFile(File defaultFile) {
        this.defaultFile = defaultFile;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setSelectionMode(int selectionMode) {
        this.selectionMode = selectionMode;
    }


    public void addExtensionFileFilter(String label, String extension) {
        extensionFileFilters.add(new ExtensionFileFilter(label, extension));
    }


    public File showChooserForSave(Component parent) {
        removeSecurityManager();
        JFileChooser chooser = createFileChooser();
        chooser.setSelectedFile(defaultFile);
        try {
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (FileChooserUtils.getExtension(file) == null
                    && chooser.getFileFilter() instanceof ExtensionFileFilter) {
                    ExtensionFileFilter fileFilter = (ExtensionFileFilter)chooser.getFileFilter();
                    file = new File(file.getAbsolutePath() + "." + fileFilter.getFirstAllowedExtension());
                }
                return file;
            }
            else {
                return null;
            }
        }
        finally {
            restoreSecurityManager();
        }
    }


    public File showChooserForSave() {
        return showChooserForSave(null);
    }


    public File showChooserForOpen(Component parent) {
        removeSecurityManager();

        JFileChooser chooser = createFileChooser();

        if (defaultFile != null) {
            if (defaultFile.isFile()) {
                chooser.setSelectedFile(defaultFile);
            }
            else {
                chooser.setCurrentDirectory(defaultFile);
            }
        }

        try {
            if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                if (file != null && file.exists()) {
                    return file;
                }
            }
            return null;
        }
        finally {
            restoreSecurityManager();
        }
    }


    public File showChooserForOpen() {
        return showChooserForOpen(null);
    }


    private JFileChooser createFileChooser() {
        // Traduction du fileChooser en français
        FileChooserUtils.setUILanguage();

        // On pointe par defaut sur le lecteur C
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(selectionMode);

        // Init des outils du fileChooser
        if (selectionMode != JFileChooser.DIRECTORIES_ONLY) {
            javax.swing.filechooser.FileFilter allFilter = chooser.getFileFilter();
            for (ExtensionFileFilter fileFilter : extensionFileFilters) {
                chooser.addChoosableFileFilter(fileFilter);
            }
            chooser.setFileFilter(allFilter);
            chooser.addPropertyChangeListener(new FileChooserListener(chooser));
            if (isWithAccessories()) {
                chooser.setAccessory(buildAccessory(chooser));
            }
        }

        chooser.setDialogTitle(title);

        return chooser;
    }


    private void restoreSecurityManager() {
        System.setSecurityManager(securityManager);
    }


    /**
     * Suppression du securityManager pour éviter le bug de la disquette par JavaWebStart
     */
    private void removeSecurityManager() {
        securityManager = System.getSecurityManager();
        System.setSecurityManager(null);
    }


    private static FileChooserAccessory buildAccessory(JFileChooser chooser) {
        FileChooserAccessory fca = new FileChooserAccessory(chooser);
        fca.initUtils(new JPanel[]{
              new ShortcutsAccessory(chooser), new FindFileAccessory(chooser),
              new TextFilePreviewAccessory(chooser)
        });
        return fca;
    }
}
