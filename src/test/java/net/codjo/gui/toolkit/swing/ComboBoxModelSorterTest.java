/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import junit.framework.TestCase;
/**
 * Classe de test de {@link ComboBoxModelSorter}.
 */
public class ComboBoxModelSorterTest extends TestCase {
    private ComboBoxModelSorter sorter;


    public void test_viewIndexToModelIndex() {
        ComboBoxModel subModel = new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        assertModelContent(new String[]{"A", "B", "C"}, sorter);

        assertEquals(0, sorter.viewIndexToModelIndex(2));

        try {
            sorter.viewIndexToModelIndex(150);
            fail("index Incorecte");
        }
        catch (RuntimeException ex) {
            ; // Ok
        }
    }


    public void test_modelIndexToViewIndex() {
        ComboBoxModel subModel = new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        assertModelContent(new String[]{"A", "B", "C"}, sorter);

        assertEquals(2, sorter.modelIndexToViewIndex(0));
        try {
            sorter.modelIndexToViewIndex(150);
            fail("index Incorecte");
        }
        catch (RuntimeException ex) {
            ; // Ok
        }
    }


    public void test_constructor() {
        ComboBoxModel subModel = new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        assertModelContent(new String[]{"A", "B", "C"}, sorter);
    }


    public void test_constructor_comparator() {
        ComboBoxModel subModel = new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter =
              new ComboBoxModelSorter(subModel,
                                      new ComboBoxModelSorter.DataComparator() {
                                          public int compare(ComboBoxModel subModel, int indexValue1,
                                                             int indexValue2) {
                                              Object obj1 = subModel.getElementAt(indexValue1);
                                              Object obj2 = subModel.getElementAt(indexValue2);
                                              return ((String)obj2).compareTo((String)obj1);
                                          }
                                      });

        assertModelContent(new String[]{"C", "B", "A"}, sorter);
    }


    public void test_constructor_valueTypeInt() {
        Integer integer1 = 1;
        Integer integer6 = 6;
        Integer integer5 = 5;
        Integer integer11 = 11;
        Integer integer2 = 2;

        ComboBoxModel subModel =
              new DefaultComboBoxModel(new Integer[]{
                    integer1, integer6, integer5, integer11, integer2
              });
        sorter = new ComboBoxModelSorter(subModel);

        assertModelContent(new Integer[]{integer1, integer11, integer2, integer5, integer6},
                           sorter);
    }


    public void test_constructor_subModelAlreadySorted() {
        String[] content = new String[]{"A", "B"};
        ComboBoxModel subModel = new DefaultComboBoxModel(content);
        sorter = new ComboBoxModelSorter(subModel);

        assertModelContent(content, sorter);
    }


    public void test_getSelectedItem() {
        String selectedItem = "A";
        ComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{selectedItem, "B"});
        subModel.setSelectedItem(selectedItem);

        sorter = new ComboBoxModelSorter(subModel);
        assertSame(selectedItem, sorter.getSelectedItem());
    }


    public void test_setSelectedItem() {
        String toBeSelected = "A";
        ComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{"first", toBeSelected, "B"});

        sorter = new ComboBoxModelSorter(subModel);
        sorter.setSelectedItem(toBeSelected);
        assertSame(toBeSelected, subModel.getSelectedItem());
    }


    public void test_addListDataListener() {
        MockListDataListener listener = new MockListDataListener();
        MockComboBoxModel subModel = new MockComboBoxModel();

        sorter = new ComboBoxModelSorter(subModel);
        sorter.addListDataListener(listener);

        assertListenerCalled(true, subModel, listener);
    }


    public void test_addListDataListener_event() {
        MockListDataListener listener = new MockListDataListener();
        DefaultComboBoxModel subModel = new DefaultComboBoxModel(new String[]{"A", "C"});

        sorter = new ComboBoxModelSorter(subModel);
        sorter.addListDataListener(listener);

        subModel.insertElementAt("B", 0);

        assertEquals(1, listener.getContentsChangedCalledNumber());

        assertEquals(ListDataEvent.CONTENTS_CHANGED, listener.getLastEvent().getType());
        assertEquals(sorter, listener.getLastEvent().getSource());
        assertEquals(0, listener.getLastEvent().getIndex0());
        assertEquals(2, listener.getLastEvent().getIndex1());

        subModel.removeElementAt(0);

        assertEquals(2, listener.getContentsChangedCalledNumber());

        assertEquals(ListDataEvent.CONTENTS_CHANGED, listener.getLastEvent().getType());
        assertEquals(sorter, listener.getLastEvent().getSource());
        assertEquals(0, listener.getLastEvent().getIndex0());
        assertEquals(1, listener.getLastEvent().getIndex1());

        subModel.setSelectedItem("");

        assertEquals(3, listener.getContentsChangedCalledNumber());

        assertEquals(ListDataEvent.CONTENTS_CHANGED, listener.getLastEvent().getType());
        assertEquals(sorter, listener.getLastEvent().getSource());
        assertEquals(0, listener.getLastEvent().getIndex0());
        assertEquals(1, listener.getLastEvent().getIndex1());
    }


    public void test_removeListDataListener() {
        MockListDataListener listener = new MockListDataListener();
        MockComboBoxModel subModel = new MockComboBoxModel();

        sorter = new ComboBoxModelSorter(subModel);
        sorter.addListDataListener(listener);
        sorter.removeListDataListener(listener);

        assertListenerCalled(false, subModel, listener);
    }


    public void test_dynamicRemoveElementAt() throws Exception {
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        subModel.removeElementAt(0);

        assertModelContent(new String[]{"A", "B"}, sorter);
    }


    public void test_setModel() {
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{"C", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);
        MockListDataListener listener = new MockListDataListener();
        sorter.addListDataListener(listener);

        DefaultComboBoxModel newSubModel =
              new DefaultComboBoxModel(new String[]{"2", "1", "3"});

        sorter.setModel(newSubModel);
        assertEquals(1, listener.getContentsChangedCalledNumber());
        assertModelContent(new String[]{"1", "2", "3"}, sorter);

        subModel.removeElement("C");
        assertEquals(1, listener.getContentsChangedCalledNumber());
    }


    public void test_setEnableSort() {
        String[] unsortedContent = new String[]{"C", "A", "B"};
        String[] sortedContent = new String[]{"A", "B", "C"};

        ComboBoxModel subModel = new DefaultComboBoxModel(unsortedContent);

        sorter = new ComboBoxModelSorter(subModel);
        assertTrue(sorter.isSortEnabled());
        assertModelContent(sortedContent, sorter);

        sorter.setSortEnabled(false);
        assertModelContent(unsortedContent, sorter);

        sorter.setSortEnabled(true);
        assertModelContent(sortedContent, sorter);
    }


    public void test_setEnableSort_event() {
        String[] unsortedContent = new String[]{"C", "A", "B"};
        sorter = new ComboBoxModelSorter(new DefaultComboBoxModel(unsortedContent));

        MockListDataListener listener = new MockListDataListener();
        sorter.addListDataListener(listener);
        assertEquals(0, listener.getContentsChangedCalledNumber());

        sorter.setSortEnabled(false);
        assertEquals(1, listener.getContentsChangedCalledNumber());
    }


    public void test_dynamicRemoveElement() throws Exception {
        String stringToRemove = "C";
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{stringToRemove, "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        subModel.removeElement(stringToRemove);

        assertModelContent(new String[]{"A", "B"}, sorter);
    }


    public void test_dynamicRemoveAllElements() throws Exception {
        String stringToRemove = "C";
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{stringToRemove, "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        subModel.removeAllElements();

        assertModelContent(new String[]{}, sorter);
    }


    public void test_dynamicAddElementAt() throws Exception {
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new String[]{"D", "A", "B"});
        sorter = new ComboBoxModelSorter(subModel);

        subModel.addElement("F");

        assertModelContent(new String[]{"A", "B", "D", "F"}, sorter);

        subModel.addElement("C");

        assertModelContent(new String[]{"A", "B", "C", "D", "F"}, sorter);
    }


    public void test_dynamicRenameElement() throws Exception {
        StringBuffer rowToRename = new StringBuffer("B");
        StringBuffer stringA = new StringBuffer("A");
        StringBuffer stringC = new StringBuffer("C");
        DefaultComboBoxModel subModel =
              new DefaultComboBoxModel(new StringBuffer[]{rowToRename, stringA, stringC});
        sorter = new ComboBoxModelSorter(subModel);

        rowToRename.setCharAt(0, 'D');
        subModel.setSelectedItem("D");

        assertModelContent(new StringBuffer[]{stringA, stringC, rowToRename}, sorter);
    }


    private void assertListenerCalled(boolean callExpected, MockComboBoxModel subModel,
                                      MockListDataListener listener) {
        subModel.mockFireContentsChanged();
        assertContentsChangedCalledNumber(callExpected, 1, listener);

        subModel.mockFireIntervalAdded();
        assertContentsChangedCalledNumber(callExpected, 2, listener);

        subModel.mockFireIntervalRemoved();
        assertContentsChangedCalledNumber(callExpected, 3, listener);

        assertEquals(false, listener.isIntervalAddedCalled());
        assertEquals(false, listener.isIntervalRemovedCalled());
    }


    private void assertContentsChangedCalledNumber(boolean callExpected, int number,
                                                   MockListDataListener listener) {
        if (callExpected) {
            assertEquals(number, listener.getContentsChangedCalledNumber());
        }
        else {
            assertEquals(0, listener.getContentsChangedCalledNumber());
        }
    }


    private void assertModelContent(Object[] expected, ComboBoxModel model) {
        assertEquals(expected.length, model.getSize());
        for (int i = 0; i < expected.length; i++) {
            Object expectedAt = expected[i];
            Object elementAt = model.getElementAt(i);
            assertEquals(expectedAt, elementAt);
        }
    }


    static class MockComboBoxModel extends DefaultComboBoxModel {
        public void mockFireContentsChanged() {
            fireContentsChanged("source", 1, 1);
        }


        public void mockFireIntervalAdded() {
            fireIntervalAdded("source", 1, 1);
        }


        public void mockFireIntervalRemoved() {
            fireIntervalRemoved("source", 1, 1);
        }
    }

    private static class MockListDataListener implements ListDataListener {
        private int contentsChangedCalledNumber = 0;
        private boolean intervalRemovedCalled = false;
        private boolean intervalAddedCalled = false;
        private ListDataEvent lastEvent;


        public void intervalAdded(ListDataEvent event) {
            lastEvent = event;
            intervalAddedCalled = true;
        }


        public void intervalRemoved(ListDataEvent event) {
            lastEvent = event;
            intervalRemovedCalled = true;
        }


        public void contentsChanged(ListDataEvent event) {
            lastEvent = event;
            contentsChangedCalledNumber++;
        }


        public int getContentsChangedCalledNumber() {
            return contentsChangedCalledNumber;
        }


        public boolean isIntervalAddedCalled() {
            return intervalAddedCalled;
        }


        public boolean isIntervalRemovedCalled() {
            return intervalRemovedCalled;
        }


        public ListDataEvent getLastEvent() {
            return lastEvent;
        }
    }
}
