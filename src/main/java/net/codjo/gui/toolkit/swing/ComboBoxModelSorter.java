/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
/**
 * Model de ComboBox permettant de trier dynamiquement un sous-model.
 */
public class ComboBoxModelSorter extends AbstractListModel implements ComboBoxModel {
    private ComboBoxModel subModel;
    private List<Integer> indexes = new ArrayList<Integer>();
    private DataComparator comparator;
    private ComboBoxModelSorter.SorterListDataListener listener =
          new SorterListDataListener();
    private boolean sortEnabled = true;


    public ComboBoxModelSorter(ComboBoxModel subModel) {
        this(subModel, new DefaultDataComparator());
    }


    public ComboBoxModelSorter(ComboBoxModel subModel, DataComparator comparator) {
        this.comparator = comparator;
        setModel(subModel);
    }


    public void setModel(ComboBoxModel subModel) {
        if (this.subModel != null) {
            this.subModel.removeListDataListener(listener);
        }

        this.subModel = subModel;
        sort();

        subModel.addListDataListener(listener);
    }


    public void setSelectedItem(Object anItem) {
        subModel.setSelectedItem(anItem);
    }


    public Object getSelectedItem() {
        return subModel.getSelectedItem();
    }


    public int getSize() {
        return indexes.size();
    }


    public Object getElementAt(int index) {
        return subModel.getElementAt(viewIndexToModelIndex(index));
    }


    public boolean isSortEnabled() {
        return sortEnabled;
    }


    public void setSortEnabled(boolean sortEnabled) {
        this.sortEnabled = sortEnabled;
        sort();
    }


    public int viewIndexToModelIndex(int viewIndex) {
        return indexes.get(viewIndex);
    }


    public int modelIndexToViewIndex(int modelIndex) {
        for (int i = 0; i < indexes.size(); i++) {
            if (indexes.get(i) == modelIndex) {
                return i;
            }
        }
        throw new IllegalArgumentException("Index incorrecte : " + modelIndex);
    }


    private void sort() {
        initializeIndexes();
        if (isSortEnabled()) {
            Collections.sort(indexes, new DefaultComboBoxModelComparator(subModel, comparator));
        }
        fireAllContentsHasChanged();
    }


    private void initializeIndexes() {
        indexes = new ArrayList<Integer>();
        for (int i = 0; i < subModel.getSize(); i++) {
            indexes.add(i);
        }
    }


    private void fireAllContentsHasChanged() {
        fireContentsChanged(ComboBoxModelSorter.this, 0, getSize() - 1);
    }


    /**
     * Interface de comparaison
     */
    public interface DataComparator {
        public int compare(ComboBoxModel subModel, int indexValue1, int indexValue2);
    }

    private static class DefaultComboBoxModelComparator implements Comparator<Integer> {
        private ComboBoxModel subModel;
        DataComparator dataComparator;


        private DefaultComboBoxModelComparator(ComboBoxModel subModel, DataComparator dataComparator) {
            this.subModel = subModel;
            this.dataComparator = dataComparator;
        }


        public int compare(Integer index1, Integer index2) {
            return dataComparator.compare(subModel, index1, index2);
        }
    }

    private static class DefaultDataComparator implements DataComparator {
        public int compare(ComboBoxModel subModel, int indexValue1, int indexValue2) {
            String value1 = subModel.getElementAt(indexValue1).toString();
            String value2 = subModel.getElementAt(indexValue2).toString();

            return value1.compareTo(value2);
        }
    }

    private class SorterListDataListener implements ListDataListener {
        public void intervalAdded(ListDataEvent event) {
            sort();
        }


        public void intervalRemoved(ListDataEvent event) {
            sort();
        }


        public void contentsChanged(ListDataEvent event) {
            sort();
        }
    }
}
