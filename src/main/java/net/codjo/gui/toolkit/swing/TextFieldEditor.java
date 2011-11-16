package net.codjo.gui.toolkit.swing;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import net.codjo.gui.toolkit.text.DocumentWithMaxSize;
/**
 * Editeur pour table de type textField pour la gestion des null.
 */
public class TextFieldEditor extends AbstractCellEditor implements TableCellEditor {
    private JTextField editor = new JTextField();


    public TextFieldEditor() {
    }


    public TextFieldEditor(int sizeLimit) {
        this.editor.setDocument(new DocumentWithMaxSize(sizeLimit));
    }


    public Object getCellEditorValue() {
        String field = editor.getText();
        if ("".equals(field)) {
            return "null";
        }
        else {
            return field;
        }
    }


    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
        setText(value);
        return editor;
    }


    private void setText(Object value) {
        if ("null".equals(value)) {
            editor.setText("");
        }
        else {
            editor.setText(value.toString());
        }
    }
}