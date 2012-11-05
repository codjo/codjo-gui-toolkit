/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.readonly;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

/**
 *
 */
public class ReadOnlyManager {
    private List<ReadOnlyComponent> readOnlyComponents = new ArrayList<ReadOnlyComponent>();
    private List<ReadOnlyManager> subReadOnlyManagers = new ArrayList<ReadOnlyManager>();
    private boolean readOnly;
    private boolean applyDefaultValue;


    public ReadOnlyManager(boolean readOnly) {
        this(readOnly, true);
    }


    public ReadOnlyManager(boolean readOnly, boolean applyDefaultValue) {
        this.readOnly = readOnly;
        this.applyDefaultValue = applyDefaultValue;
    }


    public void setReadOnly(boolean read) {
        setReadOnly(read, true);
    }


    public void setReadOnly(boolean read, boolean applyDefaultValue) {
        this.readOnly = read;
        this.applyDefaultValue = applyDefaultValue;

        for (ReadOnlyComponent readOnlyComponent : readOnlyComponents) {
            readOnlyComponent.setReadOnly(readOnly, applyDefaultValue);
        }

        for (ReadOnlyManager subReadOnlyManager : subReadOnlyManagers) {
            subReadOnlyManager.setReadOnly(read, applyDefaultValue);
        }
    }


    public boolean isReadOnly() {
        return readOnly;
    }


    public boolean isApplyDefaultValue() {
        return applyDefaultValue;
    }


    public void addReadOnlyContainer(ReadOnlyComponentsContainer rootContainer) {
        rootContainer.addReadOnlyComponents(this);
    }


    public void addReadOnlyComponent(JComponent component) {
        if (component instanceof JTextComponent) {
            add(new ReadOnlyTextComponent((JTextComponent)component, this));
        }
        else if (component instanceof JTable) {
            add(new ReadOnlyTableComponent((JTable)component, this));
        }
        else {
            add(new DefaultReadOnlyComponent(component, this));
        }
    }


    public void addReadOnlyComponent(JComponent component, ReadOnlyValueSetter setter) {
        if (component instanceof JTextComponent) {
            add(new ReadOnlyTextComponent((JTextComponent)component, this, setter));
        }
        else if (component instanceof JTable) {
            add(new ReadOnlyTableComponent((JTable)component, this, setter));
        }
        else {
            add(new DefaultReadOnlyComponent(component, this, setter));
        }
    }


    public void addSubReadOnlyManager(ReadOnlyManager subManager) {
        subReadOnlyManagers.add(subManager);
    }


    public void add(ReadOnlyComponent readOnlyComponent) {
        readOnlyComponents.add(readOnlyComponent);
        if (readOnly) {
            readOnlyComponent.setReadOnly(readOnly, applyDefaultValue);
        }
    }
}
