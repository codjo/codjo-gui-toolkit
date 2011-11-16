/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
/**
 * Model de liste utilisé par le composant {@link FilteredList}.
 */
class FilteredListModel extends AbstractListModel {
    private ListModel subModel = new DefaultListModel();
    private String filter = "";
    private Integer[] viewToModelIndex = new Integer[] {};
    private final SubModelListener subModelListener = new SubModelListener();
    private Stringifier stringifier = Stringifier.TO_STRING;

    public void setStringifier(Stringifier stringifier) {
        this.stringifier = stringifier;
    }


    public int getSize() {
        return viewToModelIndex.length;
    }


    public Object getElementAt(int index) {
        return this.subModel.getElementAt(convertViewToModelIndex(index));
    }


    public void setSubModel(ListModel listModel) {
        this.subModel = listModel;
        this.subModel.addListDataListener(subModelListener);
        applyFilter();
        fireAllContentChanged();
    }


    public void setFilter(String filter) {
        this.filter = filter;
        applyFilter();
        fireAllContentChanged();
    }


    int convertViewToModelIndex(int viewIndex) {
        return viewToModelIndex[viewIndex];
    }


    int convertModelToViewIndex(int modelIndex) {
        for (int i = 0; i < viewToModelIndex.length; i++) {
            Integer integer = viewToModelIndex[i];
            if (integer == modelIndex) {
                return i;
            }
        }
        return -1;
    }


    private void fireAllContentChanged() {
        fireContentsChanged(this, 0, Math.max(0, getSize() - 1));
    }


    private void applyFilter() {
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < subModel.getSize(); i++) {
            Object value = subModel.getElementAt(i);
            if (value != null && stringifier.toString(value).toUpperCase().contains(filter.toUpperCase())) {
                list.add(i);
            }
        }

        viewToModelIndex = list.toArray(new Integer[list.size()]);
    }

    private class SubModelListener implements ListDataListener {
        public void intervalAdded(ListDataEvent event) {
            applyFilter();

            for (int modelIndex = event.getIndex0(); modelIndex <= event.getIndex1();
                    modelIndex++) {
                int viewIndex = convertModelToViewIndex(modelIndex);
                if (viewIndex != -1) {
                    fireIntervalAdded(FilteredListModel.this, viewIndex, viewIndex);
                }
            }
        }


        public void intervalRemoved(ListDataEvent event) {
            List<Integer> indexList = new ArrayList<Integer>();
            for (int modelIndex = event.getIndex0(); modelIndex <= event.getIndex1();
                    modelIndex++) {
                int viewIndex = convertModelToViewIndex(modelIndex);
                if (viewIndex != -1) {
                    indexList.add(viewIndex);
                }
            }
            applyFilter();
            for (Integer anIndexList : indexList) {
                fireIntervalRemoved(FilteredListModel.this, anIndexList, anIndexList);
            }
        }


        public void contentsChanged(ListDataEvent event) {
            applyFilter();
            fireAllContentChanged();
        }
    }
}
