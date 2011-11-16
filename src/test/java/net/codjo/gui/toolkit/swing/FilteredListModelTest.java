/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import junit.framework.TestCase;
/**
 * Classe de test de {@link FilteredListModel}.
 */
public class FilteredListModelTest extends TestCase {
    private FilteredListModel model;


    public void test_setSubModel_noSubModel() throws Exception {
        assertEquals(0, model.getSize());
    }


    public void test_setFilter_static() throws Exception {
        DefaultListModel subModel = new DefaultListModel();
        subModel.addElement("bb");
        subModel.addElement("aa");
        this.model.setSubModel(subModel);
        this.model.setFilter("a");

        assertEquals(1, this.model.getSize());
        assertEquals("aa", this.model.getElementAt(0));
    }


    public void test_setFilter_dynamic() throws Exception {
        DefaultListModel subModel = new DefaultListModel();
        this.model.setSubModel(subModel);
        this.model.setFilter("a");

        subModel.addElement("bb");
        subModel.addElement("aa");
        subModel.addElement("AA");

        assertEquals(2, this.model.getSize());
        assertEquals("aa", this.model.getElementAt(0));
        assertEquals("AA", this.model.getElementAt(1));
    }


    public void test_setSubModel_simple() throws Exception {
        ListModelMock mock = new ListModelMock();
        mock.mockGetSize(4);
        mock.mockGetElementAt(0, "val0");
        mock.mockGetElementAt(1, "val1");
        mock.mockGetElementAt(2, "val2");
        mock.mockGetElementAt(3, "val3");

        model.setSubModel(mock);

        assertEquals(4, model.getSize());
        assertEquals("val0", model.getElementAt(0));
    }


    public void test_setSubModel_dynamic() throws Exception {
        ListDataListenerMock mockListener = new ListDataListenerMock();
        DefaultListModel subModel = new DefaultListModel();
        model.setSubModel(subModel);

        model.addListDataListener(mockListener);

        subModel.addElement("val1");

        assertEquals(1, model.getSize());
        assertEquals(" intervalAdded(FilteredListModel, 0, 0)", mockListener.calls());
    }


    public void test_event_dynamic() throws Exception {
        ListDataListenerMock mockListener = new ListDataListenerMock();
        DefaultListModel subModel = new DefaultListModel();
        model.addListDataListener(mockListener);

        model.setSubModel(subModel);
        assertEquals(" contentsChanged(FilteredListModel, 0, 0)", mockListener.calls());

        mockListener.resetCalls();
        model.setFilter("a");
        assertEquals(" contentsChanged(FilteredListModel, 0, 0)", mockListener.calls());

        mockListener.resetCalls();
        subModel.addElement("aa");
        subModel.addElement("bb");
        assertEquals(" intervalAdded(FilteredListModel, 0, 0)", mockListener.calls());

        mockListener.resetCalls();
        subModel.addElement("ab");
        assertEquals(2, model.getSize());
        assertEquals(" intervalAdded(FilteredListModel, 1, 1)", mockListener.calls());

        mockListener.resetCalls();
        subModel.addElement("cc");
        assertEquals(2, model.getSize());
        assertEquals("", mockListener.calls());
    }


    public void test_event_dynamic_remove() throws Exception {
        ListDataListenerMock mockListener = new ListDataListenerMock();
        DefaultListModel subModel = new DefaultListModel();
        subModel.addElement("aa");

        model.setSubModel(subModel);
        model.addListDataListener(mockListener);

        mockListener.resetCalls();
        subModel.removeElement("aa");
        assertEquals(" intervalRemoved(FilteredListModel, 0, 0)", mockListener.calls());
    }


    public void test_event_dynamic_changed_simple()
          throws Exception {
        ListDataListenerMock mockListener = new ListDataListenerMock();
        ListModelMock subModel = new ListModelMock();
        model.setFilter("a");
        model.setSubModel(subModel);
        model.addListDataListener(mockListener);

        subModel.mockGetSize(3);
        subModel.mockGetElementAt(0, "aa");
        subModel.mockGetElementAt(1, "bb");
        subModel.mockGetElementAt(2, "ac");

        mockListener.resetCalls();
        subModel.mockGetElementAt(0, "aa");
        subModel.mockGetElementAt(1, "bba");
        subModel.mockGetElementAt(2, "c");
        subModel.fireEventChanged(this, 0, 2);

        assertEquals(" contentsChanged(FilteredListModel, 0, 1)", mockListener.calls());
    }


    public void test_event_dynamic_largeRange() throws Exception {
        ListDataListenerMock mockListener = new ListDataListenerMock();
        ListModelMock subModel = new ListModelMock();
        subModel.mockGetSize(1);
        subModel.mockGetElementAt(0, "aa");
        model.setSubModel(subModel);
        model.setFilter("a");
        model.addListDataListener(mockListener);

        subModel.mockGetSize(4);
        subModel.mockGetElementAt(1, "bb");
        subModel.mockGetElementAt(2, "ba");
        subModel.mockGetElementAt(3, "bab");
        subModel.fireEventAdded(this, 1, 3);

        assertEquals(3, model.getSize());
        assertEquals(" intervalAdded(FilteredListModel, 1, 1)"
                     + " intervalAdded(FilteredListModel, 2, 2)", mockListener.calls());
    }


    protected void setUp() throws Exception {
        model = new FilteredListModel();
    }


    static class ListModelMock implements ListModel {
        private int size;
        private Map getElementAtMocked = new HashMap();
        private ListDataListener listener;


        public int getSize() {
            return size;
        }


        public Object getElementAt(int index) {
            return getElementAtMocked.get(new Integer(index));
        }


        public void addListDataListener(ListDataListener newListener) {
            this.listener = newListener;
        }


        public void removeListDataListener(ListDataListener newListener) {
        }


        public void mockGetSize(int newSize) {
            size = newSize;
        }


        public void mockGetElementAt(int arg, String mockedElement) {
            getElementAtMocked.put(new Integer(arg), mockedElement);
        }


        public void fireEventAdded(Object source, int index0, int index1) {
            listener.intervalAdded(new ListDataEvent(source,
                                                     ListDataEvent.INTERVAL_ADDED, index0, index1));
        }


        public void fireEventChanged(Object source, int index0, int index1) {
            listener.contentsChanged(new ListDataEvent(source,
                                                       ListDataEvent.CONTENTS_CHANGED, index0, index1));
        }
    }

    static class ListDataListenerMock implements ListDataListener {
        private String calls = "";


        public void intervalAdded(ListDataEvent event) {
            calls += " intervalAdded(" + toString(event) + ")";
        }


        public void intervalRemoved(ListDataEvent event) {
            calls += " intervalRemoved(" + toString(event) + ")";
        }


        public void contentsChanged(ListDataEvent event) {
            calls += " contentsChanged(" + toString(event) + ")";
        }


        public String calls() {
            return this.calls;
        }


        private String toString(ListDataEvent event) {
            String name = event.getSource().getClass().getName();
            return name.substring(name.lastIndexOf(".") + 1) + ", " + event.getIndex0()
                   + ", " + event.getIndex1();
        }


        public void resetCalls() {
            this.calls = "";
        }
    }
}
