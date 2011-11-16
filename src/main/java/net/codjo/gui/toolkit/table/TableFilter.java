/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.table;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
/**
 * Model pour filtrer un tableau.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 */
public class TableFilter extends TableMap {
    private static final Logger APP = Logger.getLogger(TableFilter.class);
    private int rowCount = 0;
    private transient PropertyChangeSupport propertyChangeListeners = new PropertyChangeSupport(this);
    private int[] indexes;
    private Object[][] filters;
    private boolean[] partialMatchAllowed;


    /**
     * Constructor.
     *
     * @param model Model original filtrer
     */
    public TableFilter(TableModel model) {
        setModel(model);
    }


    /**
     * Positionne un filtre sur une colonne.
     *
     * @param column La colonne filtree
     * @param value  La valeur du filtre (null = pas de filtre)
     */
    public void setFilter(int column, Object value) {
        setFilter(column, value, false);
    }

    public void setFilter(int column, Object value, boolean partialMatchAllowed) {
        Object oldValue = filters[column];
        filters[column] = new Object[]{value};
        this.partialMatchAllowed[column] = partialMatchAllowed;
        applyFilter();
        propertyChangeListeners.firePropertyChange(Integer.toString(column), oldValue, filters[column]);
    }


    /**
     * Positionne le model a filtrer.
     *
     * @param model Un nouveau Model
     */
    public void setModel(TableModel model) {
        super.setModel(model);
        reallocateInternalData();
    }


    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        if (aRow > rowCount) {
            throw new IllegalArgumentException();
        }
        getModel().setValueAt(aValue, indexes[aRow], aColumn);
        applyFilter();
    }


    /**
     * Retourne la premiere valeur du filtre de la colonne.
     *
     * @param column La colonne
     *
     * @return Le premier filtre (ou null)
     */
    public Object getFilterValue(int column) {
        Object[] columnFilter = getFilterValueList(column);
        if (columnFilter == null || columnFilter.length == 0) {
            return null;
        }
        return columnFilter[0];
    }


    /**
     * Retourne les valeurs de tous les filtres de la colonne.
     *
     * @param column La colonne
     *
     * @return Tableau de filtre (ou null)
     */
    public Object[] getFilterValueList(int column) {
        return filters[column];
    }


    public int getRowCount() {
        return rowCount;
    }


    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        if (aRow > rowCount) {
            return null;
        }
        else {
            return getModel().getValueAt(indexes[aRow], aColumn);
        }
    }


    /**
     * Indique si la colonne est filtrée.
     *
     * @param column La colonne
     *
     * @return 'true' si la colonne est filtrée
     */
    public boolean hasFilter(int column) {
        return filters[column] != null;
    }


    public void addFilter(int column, Object value) {
        Object oldValue = filters[column];
        Object[] array = (filters[column] != null) ? filters[column] : new Object[0];
        List columnFilters = new java.util.ArrayList(Arrays.asList(array));
        columnFilters.add(value);
        filters[column] = columnFilters.toArray();
        applyFilter();
        propertyChangeListeners.firePropertyChange(Integer.toString(column), oldValue, filters[column]);
    }


    /**
     * Enleve le filtre de la colonne.
     *
     * @param column La colonne
     */
    public void clearFilter(int column) {
        Object oldValue = filters[column];
        filters[column] = null;
        applyFilter();
        propertyChangeListeners.firePropertyChange(Integer.toString(column), oldValue, filters[column]);
    }


    public void clearAllColumnFilter() {
        for (int i = 0; i < getColumnCount(); i++) {
            clearFilter(i);
        }
    }


    public void removeFilter(int column, Object value) {
        Object[] oldValue = filters[column];
        if (oldValue == null) {
            return;
        }
        List columnFilters = new java.util.ArrayList(Arrays.asList(oldValue));
        if (columnFilters.contains(value)) {
            if (columnFilters.size() == 1) {
                clearFilter(column);
            }
            else {
                columnFilters.remove(value);
                filters[column] = columnFilters.toArray();
                applyFilter();
                propertyChangeListeners.firePropertyChange(Integer.toString(column),
                                                           oldValue, filters[column]);
            }
        }
    }


    public boolean containsFilterValue(int column, Object value) {
        Object[] columnFilters = filters[column];
        if (columnFilters == null) {
            return false;
        }

        for (int i = 0; i < filters[column].length; i++) {
            if (filters[column][i] == value) {
                return true;
            }
            else if (filters[column][i] != null) {
                if (filters[column][i].equals(value)) {
                    return true;
                }
                else if (partialMatchAllowed[column]) {
                    if (value != null && value.toString().toLowerCase()
                          .contains(filters[column][i].toString().toLowerCase())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Modification du model sous jacent.
     *
     * @param event
     */
    public void tableChanged(TableModelEvent event) {
        reallocateInternalData();
        applyFilter();
    }


    public synchronized void removePropertyChangeListener(int column,
                                                          PropertyChangeListener listener) {
        propertyChangeListeners.removePropertyChangeListener(Integer.toString(column),
                                                             listener);
    }


    /**
     * Ajoute un PropertyChangeListener pour les Filtres.
     *
     * @param column
     * @param listener
     */
    public synchronized void addPropertyChangeListener(int column, PropertyChangeListener listener) {
        propertyChangeListeners.addPropertyChangeListener(Integer.toString(column), listener);
    }


    /**
     * Indique si la ligne verifie les filtres.
     *
     * @param aRow La ligne du model a verifier.
     *
     * @return <code>true</code> la ligne respecte les filtre (sera affichee)
     */
    private boolean isDisplayed(int aRow) {
        for (int ci = 0; ci < getColumnCount(); ci++) {
            if (!checkColumnFilter(ci, getModel().getValueAt(aRow, ci))) {
                return false;
            }
        }

        return true;
    }


    /**
     * Verifie que la valeur "value" correspond au moins a un des filtres.
     *
     * @param column La colonne
     * @param value  La valeur
     *
     * @return 'true' si la valeur est contenu dans les filtres, 'false' sinon.
     */
    private boolean checkColumnFilter(int column, Object value) {
        if (filters[column] == null) {
            return true;
        }
        return containsFilterValue(column, value);
    }


    /**
     * Applique les filtre. Cette methode lance un evenenment de modification.
     */
    private void applyFilter() {
        rowCount = 0;

        for (int i = 0; i < getModel().getRowCount(); i++) {
            if (isDisplayed(i)) {
                indexes[rowCount] = i;
                rowCount++;
            }
        }

        super.tableChanged(new TableModelEvent(this));
    }


    /**
     * Reallocation des donnees interne.
     */
    private void reallocateInternalData() {
        Object[][] oldFilters = filters;
        filters = new Object[getModel().getColumnCount()][];
        partialMatchAllowed = new boolean[filters.length];
        if (oldFilters != null) {
            for (int i = 0; i < oldFilters.length && i < filters.length; i++) {
                filters[i] = oldFilters[i];
            }
        }

        rowCount = getModel().getRowCount();
        indexes = new int[rowCount];
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }

        checkModel();
    }


    /**
     * Verification d'un changement.
     */
    private void checkModel() {
        if (indexes.length != getModel().getRowCount()) {
            APP.error("Le filtre n'a pas été informé d'un changement du modèle.");
        }
    }
}
