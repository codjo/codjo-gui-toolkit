/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.text;
import net.codjo.gui.toolkit.util.GuiUtil;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 * Composant graphique limitant la longueur de la chaîne de caractères.
 */
public class TextArea extends JTextArea {
    public TextArea(String fieldDescription, int maxLength) {
        setFont(GuiUtil.DEFAULT_FONT);
        setDocument(new TextDocument(fieldDescription, maxLength));
    }

    private final class TextDocument extends PlainDocument {
        private final String fieldDescription;
        private int maxTextLength = -1;

        TextDocument(String fieldDescription, int maxLength) {
            this.fieldDescription = fieldDescription;
            this.maxTextLength = maxLength;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet attributeSet)
                throws BadLocationException {
            if (str != null) {
                if ((maxTextLength != -1 && (getLength() + str.length()) > maxTextLength)) {
                    JOptionPane.showMessageDialog(getParent(),
                        fieldDescription + " : la longueur ne doit pas dépasser " + maxTextLength
                        + " caractères");
                    return;
                }

                super.insertString(offs, str, attributeSet);
            }
        }
    }
}
