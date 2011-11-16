/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.swing;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
/**
 * Editeur pour table de type checkBox pour les données booléennes.
 */
public class CheckBoxEditor extends DefaultCellEditor {
    public CheckBoxEditor() {
        super(new JCheckBox());
        JCheckBox checkBox = (JCheckBox)editorComponent;
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }
}
