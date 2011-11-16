/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.text;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class UpperCaseField extends JTextField {
    public UpperCaseField() {
    }


    public UpperCaseField(int cols) {
        super(cols);
    }


    @Override
    protected Document createDefaultModel() {
        return new UpperCaseDocument();
    }


    static class UpperCaseDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet attribute)
              throws BadLocationException {
            if (str == null) {
                return;
            }
            char[] upper = str.toCharArray();
            for (int i = 0; i < upper.length; i++) {
                upper[i] = Character.toUpperCase(upper[i]);
            }
            super.insertString(offs, new String(upper), attribute);
        }
    }
}
