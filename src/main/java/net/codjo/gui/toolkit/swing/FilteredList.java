/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.text.SearchTextField;
import net.codjo.gui.toolkit.util.GuiUtil;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
/**
 * Composant permettant avec filtrage sur une liste.<p>
 * <pre>
 * -------------------
 * | champ de filtre |
 * -------------------
 * | Liste d'item    |
 * |                 |
 * |                 |
 * -------------------
 * </pre></p>
 */
public class FilteredList extends JPanel {
    private final JTextField filterField = new SearchTextField();
    private final JList contentList = new JList();
    private final FilteredListModel filteredListModel;
    private final List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();


    public FilteredList() {
        super(new BorderLayout());
        filteredListModel = new FilteredListModel();
        setName("FilteredList");
        contentList.setModel(filteredListModel);
        contentList.getSelectionModel().addListSelectionListener(new ListSelectionListener());
        filterField.getDocument().addDocumentListener(new FilterFieldListener());
        filterField.addKeyListener(new ListNavigator());
        add(filterField, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(contentList);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0),
                                                                scrollPane.getBorder()));
        add(scrollPane, BorderLayout.CENTER);
    }


    public JList getList() {
        return contentList;
    }


    public int getContentListSize() {
        return filteredListModel.getSize();
    }


    public String getFirstElementInList() {
        return (String)filteredListModel.getElementAt(0);
    }


    public void addFilteredListFieldListener(KeyListener ourKeyListener) {
        filterField.addKeyListener(ourKeyListener);
    }


    public void addFilteredListListener(MouseListener ourMouseListener) {
        contentList.addMouseListener(ourMouseListener);
    }


    public void setStringifier(Stringifier stringifier) {
        filteredListModel.setStringifier(stringifier);
    }


    @Override
    public void setName(String name) {
        super.setName(name);
        filterField.setName(name + ".search");
        contentList.setName(name + ".list");
    }


    public void setModel(ListModel lm) {
        this.filteredListModel.setSubModel(lm);
    }


    public void setSelectedValue(Object newSelectedValue) {
        contentList.setSelectedValue(newSelectedValue, true);
    }


    public Object getSelectedValue() {
        return this.contentList.getSelectedValue();
    }


    public void setFilter(String filterName) {
        this.filteredListModel.setFilter(filterName);
    }


    public void addListSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }


    @Override
    public void setEnabled(boolean enabled) {
        GuiUtil.setTextComponentEditable(filterField, enabled);
        contentList.setEnabled(enabled);
    }


    public void setPopupMenu(final JPopupMenu popupMenu) {
        popupMenu.setInvoker(contentList);

        MouseAdapter popupHelper = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    int row = contentList.locationToIndex(event.getPoint());
                    if (row != -1) {
                        contentList.setSelectedIndex(row);
                        popupMenu.show(event.getComponent(), event.getX(), event.getY());
                    }
                }
                else {
                    popupMenu.setVisible(false);
                }
            }
        };
        contentList.addMouseListener(popupHelper);
    }


    public interface SelectionListener {
        void selectionChanged(int[] selection);
    }


    private void applyFilter() {
        setFilter(filterField.getText());
        contentList.clearSelection();
    }


    private class FilterFieldListener implements DocumentListener {
        public void insertUpdate(DocumentEvent event) {
            applyFilter();
        }


        public void removeUpdate(DocumentEvent event) {
            applyFilter();
        }


        public void changedUpdate(DocumentEvent event) {
            applyFilter();
        }
    }

    private class ListNavigator extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            int size = contentList.getModel().getSize();
            if (size == 0) {
                return;
            }
            int currentIndex = contentList.getSelectedIndex();
            switch (event.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    contentList.clearSelection();
                    break;
                case KeyEvent.VK_DOWN:
                    contentList.setSelectedIndex(Math.min(currentIndex + 1, size - 1));
                    break;
                case KeyEvent.VK_UP:
                    if (currentIndex == -1) {
                        break;
                    }
                    if (currentIndex == 0) {
                        contentList.clearSelection();
                    }
                    else {
                        contentList.setSelectedIndex(Math.max(currentIndex - 1, 0));
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (currentIndex != -1) {
                        filterField.setText(contentList.getSelectedValue().toString());
                    }
                default: // ignore the event
            }
        }
    }

    private class ListSelectionListener implements javax.swing.event.ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) {
                return;
            }
            int[] viewIndexes = contentList.getSelectedIndices();
            int[] modelIndexes = new int[viewIndexes.length];
            for (int i = 0; i < viewIndexes.length; i++) {
                modelIndexes[i] = filteredListModel.convertViewToModelIndex(viewIndexes[i]);
            }
            notifySelectionListeners(modelIndexes);
        }


        private void notifySelectionListeners(int[] selectedIndices) {
            for (SelectionListener listener : selectionListeners) {
                listener.selectionChanged(selectedIndices);
            }
        }
    }
}
