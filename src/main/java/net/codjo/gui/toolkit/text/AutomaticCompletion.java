package net.codjo.gui.toolkit.text;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.JTextComponent;

public abstract class AutomaticCompletion {
    private static final String AUTOMATIC_COMPLETION = "AUTOMATIC";
    private int maxRowDisplay = 10;
    protected JList list = new JList();
    protected JPopupMenu popup = new JPopupMenu();
    protected JTextComponent textComp;
    private DocumentListener listener;
    private static final String ENTER_ACTION = "enter";
    private static final String DOWN_ACTION = "down";
    private static final String UP_ACTION = "up";
    private static final String ESCAPE_ACTION = "escape";


    protected AutomaticCompletion(JTextComponent component, int maxRowDisplay) {
        this(component);
        this.maxRowDisplay = maxRowDisplay;
    }


    protected AutomaticCompletion(JTextComponent component) {
        initCompletionPopup();
        initCompletionTextField(component);
        initCompletionList();
    }


    private void initCompletionList() {
        list.setName("completionList");
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                textComp.getDocument().removeDocumentListener(listener);
                acceptedListItem(list.getSelectedValue());
                textComp.getDocument().addDocumentListener(listener);
            }
        });
        list.setFocusable(false);
        list.setRequestFocusEnabled(false);
    }


    private void initCompletionTextField(JTextComponent component) {
        textComp = component;
        textComp.putClientProperty(AUTOMATIC_COMPLETION, this);
        textComp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN_ACTION);
        textComp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), UP_ACTION);
        textComp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESCAPE_ACTION);
        textComp.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER_ACTION);

        listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                showPopup();
            }


            public void removeUpdate(DocumentEvent e) {
                showPopup();
            }


            public void changedUpdate(DocumentEvent e) {
            }
        };

        textComp.getDocument().addDocumentListener(listener);

        textComp.getActionMap().put(DOWN_ACTION, showAction);
        textComp.getActionMap().put(UP_ACTION, upAction);
        textComp.getActionMap().put(ESCAPE_ACTION, hidePopupAction);
    }


    private void initCompletionPopup() {
        popup.setName("restrictedList");
        popup.setBorder(BorderFactory.createLineBorder(Color.black));
        popup.add(initScroll());
        popup.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }


            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                textComp.getActionMap().remove(ENTER_ACTION);
            }


            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }


    private JScrollPane initScroll() {
        JScrollPane scroll;
        scroll = new JScrollPane(list);
        scroll.setName("scrollPanel");
        scroll.setBorder(null);

        scroll.getVerticalScrollBar().setFocusable(false);
        scroll.getHorizontalScrollBar().setFocusable(false);
        return scroll;
    }


    protected void showPopup() {
        popup.setVisible(false);
        if (textComp.isEnabled() && updateRestrictedList() && list.getModel().getSize() != 0) {
            textComp.getActionMap().put(ENTER_ACTION, acceptAction);
            int size = list.getModel().getSize();

            list.setVisibleRowCount(size < maxRowDisplay ? size : maxRowDisplay);

            int position = Math.min(textComp.getCaret().getDot(), textComp.getCaret().getMark());
            popup.show(textComp, position, textComp.getHeight());
        }
        else {
            popup.setVisible(false);
        }
        textComp.requestFocus();
    }


    static final Action showAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JComponent component = (JComponent)e.getSource();
            AutomaticCompletion completer = (AutomaticCompletion)component.getClientProperty(AUTOMATIC_COMPLETION);
            if (component.isEnabled()) {
                if (completer.popup.isVisible()) {
                    completer.selectNextPossibleValue();
                }
                else {
                    completer.showPopup();
                }
            }
        }
    };

    static final Action upAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JComponent component = (JComponent)e.getSource();
            AutomaticCompletion completer = (AutomaticCompletion)component.getClientProperty(AUTOMATIC_COMPLETION);
            if (component.isEnabled()) {
                if (completer.popup.isVisible()) {
                    completer.selectPreviousPossibleValue();
                }
            }
        }
    };

    static final Action hidePopupAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JComponent component = (JComponent)e.getSource();
            AutomaticCompletion completer = (AutomaticCompletion)component.getClientProperty(AUTOMATIC_COMPLETION);
            if (component.isEnabled()) {
                completer.popup.setVisible(false);
            }
        }
    };

    static final Action acceptAction = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            JComponent component = (JComponent)e.getSource();
            AutomaticCompletion completer = (AutomaticCompletion)component.getClientProperty(AUTOMATIC_COMPLETION);
            completer.popup.setVisible(false);
            completer.setValue(completer.list.getSelectedValue());
        }
    };


    public void setValue(Object value) {
        textComp.getDocument().removeDocumentListener(listener);
        acceptedListItem(value);
        textComp.getDocument().addDocumentListener(listener);
    }


    protected void resetText() {
        textComp.getDocument().removeDocumentListener(listener);
        textComp.setText("");
        textComp.getDocument().addDocumentListener(listener);
    }


    protected void selectNextPossibleValue() {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < list.getModel().getSize() - 1) {
            list.setSelectedIndex(selectedIndex + 1);
            list.ensureIndexIsVisible(selectedIndex + 1);
        }
    }


    protected void selectPreviousPossibleValue() {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex > 0) {
            list.setSelectedIndex(selectedIndex - 1);
            list.ensureIndexIsVisible(selectedIndex - 1);
        }
    }


    protected abstract boolean updateRestrictedList();


    protected abstract void acceptedListItem(Object selected);
}
