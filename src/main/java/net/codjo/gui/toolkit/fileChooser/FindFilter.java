/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import java.io.File;
/**
 * Each search option tab that implements FindFilterFactory defines an inner class that
 * implements FindFilter. When a search is started the search panel invokes
 * createFindFilter() on each panel that implements FindFilterFactory, thus causing the
 * panel to create a FindFilter object that implements its search settings.
 *
 * @author Thierrou
 * @version $Revision: 1.4 $
 */
interface FindFilter {
    public boolean accept(File file, FindProgressCallback monitor);
}
