/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import junit.framework.TestCase;
/**
 * Classe de test de {@link ExtensionFileFilter}
 *
 * @version $Revision: 1.5 $
 */
public class ExtensionFileFilterTest extends TestCase {
    private ExtensionFileFilter fileFilter = null;


    protected void tearDown() throws Exception {
        fileFilter = null;
        super.tearDown();
    }


    public void test_default() {
        fileFilter = new ExtensionFileFilter();
        assertEquals("txt", fileFilter.getFirstAllowedExtension());
        assertEquals("Fichiers texte (*.txt)", fileFilter.getDescription());
    }


    public void test_getFirstExtension() {
        fileFilter = new ExtensionFileFilter("Paramétrage Tokio", "tokio,xml");
        assertEquals("tokio", fileFilter.getFirstAllowedExtension());
        assertEquals("Fichiers Paramétrage Tokio (*.tokio,xml)",
                     fileFilter.getDescription());
    }
}
