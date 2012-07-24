/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.readonly;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
/**
 *
 */
public abstract class AbstractReadOnlyComponent implements ReadOnlyComponent {
    private static final String JDK_16 = "1.6";

    protected boolean readOnly;
    protected boolean applyDefaultValue;
    protected ReadOnlyValueSetter setter;


    protected AbstractReadOnlyComponent(JComponent component,
                                        final ReadOnlyManager readOnlyManager,
                                        boolean applyDefaultValue,
                                        ReadOnlyValueSetter setter) {
        this.applyDefaultValue = applyDefaultValue;
        this.setter = setter;

        if (!System.getProperty("java.version").startsWith(JDK_16)) {
            initForceReadOnlyListener(component, readOnlyManager);
        }
    }


    protected void initForceReadOnlyListener(JComponent component, final ReadOnlyManager readOnlyManager) {
        component.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (readOnlyManager.isReadOnly() && readOnlyManager.isApplyDefaultValue()) {
                    setReadOnly(readOnly, applyDefaultValue);
                }
            }
        });
    }


    protected void setDefaultValue(JComponent component) {
        if (applyDefaultValue && readOnly) {
            setter.setValue(component);
        }
    }
}
