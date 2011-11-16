package net.codjo.gui.toolkit.text;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class JTextFieldCodeLabelAutoCompleter extends AutomaticCompletion {
    protected Map<String, String> codeToLabel;
    private CodeLabel currentSelection;
    private ResetCurrentSelectionListener resetCurrentSelectionListener = new ResetCurrentSelectionListener();

    public JTextFieldCodeLabelAutoCompleter(JTextComponent comp, Map<String, String> codeToLabel) {
        super(comp);
        this.codeToLabel = codeToLabel;
        list.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list,
                                                          Object value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((CodeLabel)value).getLabel(),
                                                          index, isSelected, cellHasFocus);
            }
        });
        textComp.getDocument().addDocumentListener(resetCurrentSelectionListener);
    }


    @Override
    protected boolean updateRestrictedList() {
        String value = textComp.getText();
        if (value == null) {
            list.setListData(new String[0]);
            return true;
        }

        List<CodeLabel> listSelected = new ArrayList<CodeLabel>();
        for (Entry<String, String> entry : codeToLabel.entrySet()) {
            if (entry.getValue().toUpperCase().contains(value.toUpperCase())) {
                listSelected.add(new CodeLabel(entry.getKey(), entry.getValue()));
            }
        }

        Collections.sort(listSelected, new Comparator<CodeLabel>() {
            public int compare(CodeLabel o1, CodeLabel o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        list.setListData(listSelected.toArray());

        return true;
    }


    @Override
    protected void acceptedListItem(Object selected) {
        if (selected == null) {
            return;
        }

        this.currentSelection = (CodeLabel)selected;
        textComp.getDocument().removeDocumentListener(resetCurrentSelectionListener);
        textComp.setText(currentSelection.getLabel());
        textComp.getDocument().addDocumentListener(resetCurrentSelectionListener);
    }


    public void setCode(String code) {
        for (Entry<String, String> entry : codeToLabel.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(code)) {
                setValue(new CodeLabel(entry.getKey(), entry.getValue()));
                return;
            }
        }

        this.currentSelection = null;
        resetText();
    }


    public void updateAutoCompletedValues(Map<String, String> newValues) {
        this.codeToLabel.clear();
        this.codeToLabel.putAll(newValues);
        this.currentSelection = null;
    }


    public String getSelectedCode() {
        if (currentSelection != null) {
            return currentSelection.getCode();
        }
        String value = textComp.getText();

        for (Entry<String, String> entry : codeToLabel.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(value)) {
                return entry.getKey();
            }
        }

        return null;
    }


    public JTextComponent getTextComponent() {
        return textComp;
    }


    private class ResetCurrentSelectionListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            currentSelection = null;
        }


        public void removeUpdate(DocumentEvent e) {
            currentSelection = null;
        }


        public void changedUpdate(DocumentEvent e) {
            currentSelection = null;
        }
    }

    private class CodeLabel {
        private String code;
        private String label;


        private CodeLabel(String code, String label) {
            this.code = code;
            this.label = label;
        }


        public String getCode() {
            return code;
        }


        public String getLabel() {
            return label;
        }
    }
}
