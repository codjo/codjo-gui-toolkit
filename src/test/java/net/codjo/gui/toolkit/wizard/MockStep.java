/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.util.Map;
/**
 * Mock de {@link StepPanel}
 *
 * @author $Author: torrent $
 * @version $Revision: 1.6 $
 */
public class MockStep extends StepPanel {
    public int cancel_calledTime = 0;
    public int start_calledTime = 0;
    public Map start_previousStepState;
    private Map stateReaded;


    public MockStep() {
    }


    public MockStep(String name) {
        setName(name);
    }


    public Map getStateReaded() {
        return stateReaded;
    }


    public void cancel() {
        super.cancel();
        cancel_calledTime++;
    }


    public void start(Map previousStepState) {
        stateReaded = previousStepState;
        start_calledTime++;
        start_previousStepState = previousStepState;
        super.start(previousStepState);
    }
}
