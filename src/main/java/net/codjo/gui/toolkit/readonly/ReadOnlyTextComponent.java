/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.readonly;
import net.codjo.gui.toolkit.util.GuiUtil;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
/**
 *
 */
public class ReadOnlyTextComponent extends AbstractReadOnlyComponent {
    private JTextComponent textComponent;


    public ReadOnlyTextComponent(JTextComponent textComponent,
                                 ReadOnlyManager readOnlyManager) {
        super(textComponent, readOnlyManager, false, null);
        this.textComponent = textComponent;
    }


    public ReadOnlyTextComponent(JTextComponent textComponent,
                                 final ReadOnlyManager readOnlyManager,
                                 ReadOnlyValueSetter setter) {
        super(textComponent, readOnlyManager, true, setter);
        this.textComponent = textComponent;
    }


    public void setReadOnly(boolean readonly, final boolean applydefaultvalue) {
        readOnly = readonly;
        if (textComponent.isEditable() == readOnly) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GuiUtil.setTextComponentEditable(textComponent, !readOnly);
                    if (applydefaultvalue) {
                        setDefaultValue(textComponent);
                    }
                }
            });
        }
    }
}
