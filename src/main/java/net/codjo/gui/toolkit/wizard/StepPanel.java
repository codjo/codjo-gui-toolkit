/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * Implantation simple d'une etape sous forme de panel.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.9 $
 */
public class StepPanel extends JPanel implements Step {
    private boolean fulfilled = false;
    private Map<String, Object> values = new HashMap<String, Object>();
    private Wizard wizard;

    public void setFulfilled(boolean val) {
        boolean prev = this.fulfilled;
        this.fulfilled = val;
        super.firePropertyChange(FULFILLED_PROPERTY, prev, this.fulfilled);
    }


    public void setValue(String key, Object value) {
        values.put(key, value);
    }


    public boolean isFulfilled() {
        return fulfilled;
    }


    public JComponent getGui() {
        return this;
    }


    public Map<String, Object> getState() {
        return Collections.unmodifiableMap(values);
    }


    public Object getValue(String key) {
        return values.get(key);
    }


    public void cancel() {}


    public void start(Map previousStepState) {}


    void setWizard(Wizard wizardOwner) {
        wizard = wizardOwner;
    }


    protected Wizard getWizard() {
        return wizard;
    }
}
