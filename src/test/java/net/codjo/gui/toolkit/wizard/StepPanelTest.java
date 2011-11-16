/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import junit.framework.TestCase;
/**
 * Classe de test de {@link StepPanel}
 *
 * @author $Author: torrent $
 * @version $Revision: 1.6 $
 */
public class StepPanelTest extends TestCase {
    private MockPropertyListener listener;
    private StepPanel step;


    public void test_getGui() throws Exception {
        assertEquals(step, step.getGui());
    }


    public void test_getValues() throws Exception {
        assertEquals(0, step.getState().size());
        try {
            step.getState().put("test", "");
            fail("La map retourné est en lecture seul !");
        }
        catch (UnsupportedOperationException e) {
            ; // Cas normal
        }
        assertEquals(0, step.getState().size());
        step.setValue("bobo", "");
        assertEquals(1, step.getState().size());
    }


    public void test_isFulfilled() throws Exception {
        step.addPropertyChangeListener(Step.FULFILLED_PROPERTY, listener);

        assertEquals(false, step.isFulfilled());

        step.setFulfilled(true);

        assertEquals(true, step.isFulfilled());
        assertEquals(1, listener.propertyChangeCalledTimes);
        assertEquals(Step.FULFILLED_PROPERTY, listener.evt.getPropertyName());
        assertEquals(Boolean.FALSE, listener.evt.getOldValue());
        assertEquals(Boolean.TRUE, listener.evt.getNewValue());
    }


    public void test_getWizard() throws Exception {
        Wizard wizard = new Wizard();
        step.setWizard(wizard);
        assertSame(wizard, step.getWizard());
    }


    protected void setUp() throws Exception {
        step = new StepPanel();
        listener = new MockPropertyListener();
    }
}
