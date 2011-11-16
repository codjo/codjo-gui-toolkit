/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.text.SearchTextField;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;
import junit.framework.TestCase;
/**
 * Classe de test de {@link FilteredList}.
 */
public class FilteredListTest extends TestCase {
    private FilteredList filteredList;


    public void test_displayed() throws Exception {
        Component filterField = getTextField();
        assertNotNull(filterField);
        assertEquals(SearchTextField.class, filterField.getClass());

        Component contentList = getList();
        assertNotNull(contentList);
        assertEquals(JList.class, contentList.getClass());
    }


    public void test_name() throws Exception {
        Component filterField = getTextField();
        Component contentList = getList();

        assertEquals("FilteredList.search", filterField.getName());
        assertEquals("FilteredList.list", contentList.getName());
        assertEquals("FilteredList", filteredList.getName());

        filteredList.setName("bobo");
        assertEquals("bobo.search", filterField.getName());
        assertEquals("bobo.list", contentList.getName());
    }


    public void test_setModel() throws Exception {
        filteredList.setModel(newModel(new String[]{"element 1", "element 2"}));

        JList contentList = getList();
        assertEquals(2, contentList.getModel().getSize());
        assertEquals("element 1", contentList.getModel().getElementAt(0));
        assertEquals("element 2", contentList.getModel().getElementAt(1));
    }


    public void test_selection() throws Exception {
        String selectedItem = "element 2";
        filteredList.setModel(newModel(new String[]{"element 1", selectedItem}));

        filteredList.setSelectedValue(selectedItem);

        JList contentList = getList();
        assertSame(selectedItem, contentList.getSelectedValue());
        assertSame(selectedItem, filteredList.getSelectedValue());
    }


    public void test_filter() throws Exception {
        filteredList.setModel(newModel(
              new String[]{"toulouse", "bordeaux", "aix", "auxerre", "pau"}));

        filteredList.setFilter("au");

        JList contentList = getList();
        assertEquals("bordeaux", contentList.getModel().getElementAt(0));
        assertEquals("auxerre", contentList.getModel().getElementAt(1));
        assertEquals("pau", contentList.getModel().getElementAt(2));
    }


    public void test_filter_using_GUI() throws Exception {
        filteredList.setModel(newModel(
              new String[]{"toulouse", "bordeaux", "aix", "auxerre", "pau"}));

        getTextField().setText("au");

        JList contentList = getList();
        assertEquals("bordeaux", contentList.getModel().getElementAt(0));
        assertEquals("auxerre", contentList.getModel().getElementAt(1));
        assertEquals("pau", contentList.getModel().getElementAt(2));
    }


    public void test_navigationInList() throws Exception {
        filteredList.setModel(newModel(
              new String[]{"toulouse", "bordeaux", "aix", "auxerre", "pau"}));

        getTextField().setText("au");

        JList contentList = getList();
        assertEquals(3, contentList.getModel().getSize());
        assertEquals("bordeaux", contentList.getModel().getElementAt(0));
        assertEquals("auxerre", contentList.getModel().getElementAt(1));
        assertEquals("pau", contentList.getModel().getElementAt(2));

        assertEquals(-1, contentList.getSelectedIndex());

        TestUtil.pressKey(getTextField(), TestUtil.Key.DOWN);
        assertEquals(0, contentList.getSelectedIndex());

        TestUtil.pressKey(getTextField(), TestUtil.Key.DOWN);
        assertEquals(1, contentList.getSelectedIndex());

        TestUtil.pressKey(getTextField(), TestUtil.Key.UP);
        assertEquals(0, contentList.getSelectedIndex());

        getTextField().setText("o");
        assertEquals(2, contentList.getModel().getSize());
        assertEquals("toulouse", contentList.getModel().getElementAt(0));
        assertEquals("bordeaux", contentList.getModel().getElementAt(1));
        assertEquals(-1, contentList.getSelectedIndex());

        TestUtil.pressKey(getTextField(), TestUtil.Key.DOWN);
        TestUtil.pressKey(getTextField(), TestUtil.Key.DOWN);
        assertEquals(1, contentList.getSelectedIndex());
        TestUtil.pressKey(getTextField(), TestUtil.Key.ENTER);
        assertEquals("bordeaux", getTextField().getText());
        assertEquals(-1, contentList.getSelectedIndex());
    }


    public void test_listeningSelection() throws Exception {
        filteredList.setModel(newModel(
              new String[]{"toulouse", "bordeaux", "aix", "auxerre", "pau"}));

        final StringBuffer logString = new StringBuffer();
        filteredList.addListSelectionListener(new SelectionListenerLogger(logString));

        JList list = getList();
        list.getSelectionModel().setSelectionInterval(0, 0);
        assertSelection("selectionChanged(0)", logString);

        list.getSelectionModel().setSelectionInterval(1, 4);
        assertSelection("selectionChanged(1, 2, 3, 4)", logString);

        list.clearSelection();
        assertSelection("selectionChanged()", logString);
    }


    public void test_listeningSelection_withFilter() {
        filteredList.setModel(newModel(
              new String[]{"toulouse", "bordeaux", "aix", "auxerre", "pau"}));

        final StringBuffer logString = new StringBuffer();
        filteredList.addListSelectionListener(new SelectionListenerLogger(logString));

        getTextField().setText("pa");

        JList list = getList();
        list.getSelectionModel().setSelectionInterval(0, 0);
        assertSelection("selectionChanged(4)", logString);
    }


    private void assertSelection(String message, StringBuffer logString) {
        assertEquals(message, logString.toString());
        logString.setLength(0);
    }


    @Override
    protected void setUp() throws Exception {
        filteredList = new FilteredList();
    }


    private JList getList() {
        return (JList)TestUtil.findComponentWithName("FilteredList.list", filteredList);
    }


    private JTextField getTextField() {
        return (JTextField)TestUtil.findComponentWithName("FilteredList.search",
                                                          filteredList);
    }


    private DefaultListModel newModel(String[] content) {
        final DefaultListModel defaultListModel = new DefaultListModel();
        for (String aContent : content) {
            defaultListModel.addElement(aContent);
        }
        return defaultListModel;
    }


    public static void main(String[] args) {
        final DefaultListModel defaultListModel = new DefaultListModel();
        defaultListModel.addElement("aix");
        defaultListModel.addElement("amiens");
        defaultListModel.addElement("angers");
        defaultListModel.addElement("angoulème");
        defaultListModel.addElement("annecy");
        defaultListModel.addElement("auxerre");
        defaultListModel.addElement("avignon");
        defaultListModel.addElement("pau");
        defaultListModel.addElement("bordeaux");
        defaultListModel.addElement("chateautierry");

        JFrame frame = new JFrame("Test FilteredList");

        final FilteredList filteredList = new FilteredList();
        filteredList.setModel(defaultListModel);
        filteredList.setSelectedValue("element 2");

/*
        filteredList.remove(filteredList.filterField);
        filteredList.contentList.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) { }


                public void keyPressed(KeyEvent e) {
                    popupQuickSearch(filteredList, 100, 10);
                }


                public void keyReleased(KeyEvent e) { }
            });
*/
        frame.setContentPane(filteredList);
        frame.pack();
        frame.setSize(300, 500);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }


    /*
        private static void popupQuickSearch(FilteredList filteredList, final int x,
            final int y) {
            final JPopupMenu popup = new JPopupMenu("QuickSearch");
            popup.setBorder(null);

    //        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().
            JTextField field = filteredList.filterField;
            field.setColumns(15);
    //        field.setFont(field.getFont().deriveFont(Font.ITALIC));
            field.setFont(new Font("Verdana Negreta cursiva", Font.PLAIN, 12));
            field.setBorder(BorderFactory.createEmptyBorder());
            field.setOpaque(false);

            field.setBorder(null);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(null);
            panel.setBackground(new Color(250, 250, 200));
            JLabel label = new JLabel("Filtrage : ");
            label.setFont(label.getFont().deriveFont(Font.ITALIC));

            panel.add(label, BorderLayout.WEST);
            panel.add(field, BorderLayout.CENTER);
            popup.add(panel).setBackground(new Color(250, 250, 200));
    //        popup.setBorder(new ShadowBorder());
            popup.show(filteredList, x, y);

            field.requestFocus();
        }
    */
    private static class SelectionListenerLogger implements FilteredList.SelectionListener {
        private final StringBuffer logString;


        SelectionListenerLogger(StringBuffer logString) {
            this.logString = logString;
        }


        public void selectionChanged(int[] selection) {
            logString.setLength(0);
            logString.append("selectionChanged(");
            for (int i = 0; i < selection.length; i++) {
                int index = selection[i];
                logString.append(index);
                if (i < selection.length - 1) {
                    logString.append(", ");
                }
            }
            logString.append(")");
        }
    }
}
