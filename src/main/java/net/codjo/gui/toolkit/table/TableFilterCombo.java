/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.table;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
/**
 * ComboBox permettant a un utilisateur de selectionner un filtre sur un tableau. Ce JComboBox est connecte a
 * un <code>TableFilter</code> (responsable du filtre). Le ComboBox se rempli avec les informations contenu
 * dans le model.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.11 $
 * @see TableFilter
 */
public class TableFilterCombo extends JComboBox {
    public static final Object NO_FILTER = makeEnum("Tout");
    public static final Object NULL_FILTER = makeEnum("Vide");
    private ComboBoxListener comboListener;
    private TableFilterListener tableListener = new TableFilterListener();
    private java.util.Comparator<Object> comparator = null;
    private List<Integer> columnList;
    private List<TableFilter> tableFilterList;


    /**
     * Constructor.
     */
    public TableFilterCombo() {
        this(true);
    }


    public TableFilterCombo(boolean autoApplyFilter) {
        comboListener = new ComboBoxListener(autoApplyFilter);
        addActionListener(comboListener);
        tableFilterList = new ArrayList<TableFilter>();
        columnList = new ArrayList<Integer>();
    }


    public TableFilterCombo(TableFilter filterModel, int column) {
        this(true);
        setTableFilter(filterModel, column);
    }


    public void setAutoApplyFilter(boolean autoApplyFilter) {
        comboListener.setAutoApplyFilter(autoApplyFilter);
    }


    public void applyFilter() {
        Object selectedItem = getSelectedItem();

        if (selectedItem != NO_FILTER) {
            for (int i = 0; i < tableFilterList.size(); i++) {
                TableFilter filter = tableFilterList.get(i);
                filter.setFilter(columnList.get(i), selectedItem);
            }
        }
        else {
            for (int i = 0; i < tableFilterList.size(); i++) {
                TableFilter filter = tableFilterList.get(i);
                filter.clearFilter(columnList.get(i));
            }
        }
    }


    List<Integer> getColumnList() {
        return columnList;
    }


    List getTableFilterList() {
        return tableFilterList;
    }


    public void setComparator(java.util.Comparator<Object> comparator) {
        this.comparator = comparator;
        reloadComboBox();
    }


    public void setTableFilter(TableFilter filterModel, int column) {
        addTableFilter(filterModel, column);
    }


    public void addTableFilter(TableFilter filterModel, int column) {
        columnList.add(column);
        tableFilterList.add(filterModel);
        fillComboBox(filterModel, column);
        filterModel.addPropertyChangeListener(column, tableListener);
        filterModel.getModel().addTableModelListener(tableListener);
    }


    public java.util.Comparator<Object> getComparator() {
        return comparator;
    }


    /**
     * Construction d'un objet pour faire Enum.
     *
     * @param item
     *
     * @return Un objet Enum
     */
    private static Object makeEnum(final String item) {
        return new Object() {
            @Override
            public String toString() {
                return item;
            }
        };
    }


    private boolean buildModel(Set<Object> set, TableModel model, int column) {
        boolean modelContainsNullValue = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object obj = model.getValueAt(i, column);
            if (obj == null) {
                modelContainsNullValue = true;
            }
            else {
                set.add(obj);
            }
        }
        return modelContainsNullValue;
    }


    private void fillComboBox(TableFilter tableFilter, int column) {
        if (tableFilter == null) {
            return;
        }
        removeActionListener(comboListener);
        Set<Object> set = new HashSet<Object>();

        for (int i = 0; i < getModel().getSize(); i++) {
            if (!getModel().getElementAt(i).equals(NO_FILTER)
                && !getModel().getElementAt(i).equals(NULL_FILTER)) {
                set.add(getModel().getElementAt(i));
            }
        }
        boolean modelContainsNullValue;

        TableModel model = tableFilter.getModel();
        modelContainsNullValue = buildModel(set, model, column);

        Set sortedSet;
        try {
            sortedSet = sortSet(set);
        }
        catch (RuntimeException ex) {
            sortedSet = set;
        }

        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(sortedSet.toArray());
        if (modelContainsNullValue) {
            comboModel.insertElementAt(NULL_FILTER, 0);
        }

        setModel(comboModel);

        insertItemAt(NO_FILTER, 0);

        Object selectedItem;

        selectedItem = tableFilter.getFilterValue(column);

        if (selectedItem == null) {
            setSelectedIndex(0);
        }
        else {
            setSelectedItem(selectedItem);
        }

        addActionListener(comboListener);
    }


    /**
     * Rempli le contenu du ComboBox avec les elements distincts de la colonne a trier. Par défaut le contenu
     * est triée, si il y a echec aucun trie n'est fait.
     */
    private void reloadComboBox() {
        if (tableFilterList.size() == 0) {
            return;
        }
        removeActionListener(comboListener);
        Set<Object> set = new HashSet<Object>();
        boolean modelContainsNullValue = false;
        for (int i = 0; i < tableFilterList.size(); i++) {
            TableFilter tableFilter = tableFilterList.get(i);
            if (tableFilter == null) {
                return;
            }
            TableModel model = tableFilter.getModel();
            modelContainsNullValue = buildModel(set, model, columnList.get(i));
        }

        Set sortedSet;
        try {
            sortedSet = sortSet(set);
        }
        catch (RuntimeException ex) {
            sortedSet = set;
        }

        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(sortedSet.toArray());
        if (modelContainsNullValue) {
            comboModel.insertElementAt(NULL_FILTER, 0);
        }

        setModel(comboModel);

        insertItemAt(NO_FILTER, 0);

        Object selectedItem = null;
        int valuesCount = 0;
        for (int i = 0; i < tableFilterList.size(); i++) {
            TableFilter tableFilter = tableFilterList.get(i);
            Object tempSelectedItem = tableFilter.getFilterValue(columnList.get(i));

            if (tempSelectedItem != null && !tempSelectedItem.equals(selectedItem)) {
                valuesCount++;
                selectedItem = tempSelectedItem;
            }
        }
        if (selectedItem == null || valuesCount > 1) {
            setSelectedIndex(0);
        }
        else {
            setSelectedItem(selectedItem);
        }

        addActionListener(comboListener);
    }


    private Set sortSet(Set<Object> set) {
        Set<Object> sortedSet = new TreeSet<Object>(getComparator());
        sortedSet.addAll(set);
        return sortedSet;
    }


    /**
     * Ecoute les changements du ComboBox (action utilisateur).
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.11 $
     */
    private class ComboBoxListener implements ActionListener {
        private boolean autoApplyFilter;


        private ComboBoxListener(boolean autoApplyFilter) {
            this.autoApplyFilter = autoApplyFilter;
        }


        public void actionPerformed(ActionEvent parm1) {
            if (!autoApplyFilter) {
                return;
            }
            applyFilter();
        }


        public void setAutoApplyFilter(boolean autoApplyFilter) {
            this.autoApplyFilter = autoApplyFilter;
        }
    }

    /**
     * Ecoute les changements de Filtre, et de contenu.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.11 $
     */
    private class TableFilterListener implements PropertyChangeListener,
                                                 TableModelListener {
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() != null) {
                setSelectedItem(tableFilterList.get(0).getFilterValue(columnList.get(0)));
            }
            else {
                setSelectedItem(NO_FILTER);
            }
        }


        public void tableChanged(TableModelEvent event) {
            reloadComboBox();
        }
    }
}
