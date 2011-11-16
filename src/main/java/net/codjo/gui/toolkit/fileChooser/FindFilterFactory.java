/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
/**
 * Implemented by each search option panel. Each panel is responsible for creating a
 * FindFilter object that implements the search criteria specified by its user
 * interface.
 *
 * @author Thierrou
 * @version $Revision: 1.4 $
 */
interface FindFilterFactory {
    public FindFilter createFindFilter();
}
