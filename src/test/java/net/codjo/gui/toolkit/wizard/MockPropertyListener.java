/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 * Mock de {@link PropertyChangeListener}
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
class MockPropertyListener implements PropertyChangeListener {
    public int propertyChangeCalledTimes = 0;
    public PropertyChangeEvent evt;


    public void propertyChange(PropertyChangeEvent evt) {
        this.evt = evt;
        propertyChangeCalledTimes++;
    }
}
