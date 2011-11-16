/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.number.NumberField;
import net.codjo.gui.toolkit.number.NumberFieldRenderer;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
/**
 * Editeur de combo/table pour des données de type numérique.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.9 $
 */
public class NumberFieldEditor extends AbstractCellEditor implements TableCellEditor {
    private static final String DIGIT = "999999999999999999999999999999";
    private NumberField editor = new NumberField();
    private boolean alreadyCustomized = false;


    public NumberFieldEditor() {
        init(editor);
    }


    public NumberFieldEditor(NumberField editor) {
        init(editor);
    }


    public NumberFieldEditor(int maxDigit, int maxFractionDigit) {
        this(maxDigit, maxFractionDigit, false);
    }


    public NumberFieldEditor(int maxDigit, int maxFractionDigit, boolean canBeNegative) {
        this(canBeNegative ?
             new BigDecimal("-" + DIGIT.substring(0, maxDigit - maxFractionDigit)) :
             new BigDecimal(0),
             new BigDecimal(DIGIT.substring(0, maxDigit - maxFractionDigit)),
             maxFractionDigit);
    }


    public NumberFieldEditor(Number minNumber, Number maxNumber, int maxFractionDigit) {
        editor.setMaximumFractionDigits(maxFractionDigit);
        editor.setMinValue(minNumber);
        editor.setMaxValue(maxNumber);
        editor.setRenderer(new NumberFieldRenderer(
              new DecimalFormat("#,###.#", new DecimalFormatSymbols(Locale.ENGLISH)),
              null,
              null,
              null,
              null));
        init(editor);
    }


    protected void customizeEditor(NumberField zeEditor) {
    }


    public Object getCellEditorValue() {
        Number nb = editor.getNumber();
        if (nb != null) {
            return nb.toString();
        }
        else {
            return "null";
        }
    }


    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (!alreadyCustomized) {
            customizeEditor(editor);
            alreadyCustomized = true;
        }
        setNumber(value);

        editor.requestFocus();

        return editor;
    }


    void setNumber(Object value) {
        if (value == null || "null".equals(value)) {
            editor.setNumber(null);
        }
        else if (value instanceof Number) {
            editor.setNumber((Number)value);
        }
        else {
            editor.setNumber(new java.math.BigDecimal(value.toString()));
        }
    }


    private void init(NumberField newEditor) {
        this.editor = newEditor;
        this.editor.forceEditionMode(true);
    }


    public NumberField getEditor() {
        return editor;
    }
}
