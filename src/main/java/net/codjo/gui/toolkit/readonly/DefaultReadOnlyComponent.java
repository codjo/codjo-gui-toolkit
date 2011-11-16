/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.readonly;
import net.codjo.gui.toolkit.util.GuiUtil;
import javax.swing.JComponent;
/**
 *
 */
public class DefaultReadOnlyComponent extends AbstractReadOnlyComponent {
    private JComponent component;


    public DefaultReadOnlyComponent(JComponent component, final ReadOnlyManager readOnlyManager) {
        super(component, readOnlyManager, false, null);
        this.component = component;
    }


    public DefaultReadOnlyComponent(JComponent component,
                                    final ReadOnlyManager readOnlyManager,
                                    ReadOnlyValueSetter setter) {
        super(component, readOnlyManager, true, setter);
        this.component = component;
    }


    public void setReadOnly(boolean readonly, final boolean applydefaultvalue) {
        readOnly = readonly;
        GuiUtil.runInSwingThread(new Runnable() {
            public void run() {
                if (component.isEnabled() == readOnly) {
                    if (applydefaultvalue) {
                        setDefaultValue(component);
                    }
                    component.setEnabled(!readOnly);
                }
            }
        });
    }
}
