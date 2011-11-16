/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Gestionnaire de l'assistant.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class Wizard {
    public static final String CURRENT_STEP_PROPERTY = "currentStep";
    public static final String FINAL_STEP_PROPERTY = "finalStep";
    static final String STEP_STATE_PROPERTY = "StepState";
    static final String WIZARD_FINISHING = "wizardFinished";
    private StepListener stepListener = new StepListener();
    private List<Step> steps = new ArrayList<Step>();
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private boolean wizardFinished = false;
    private Step currentStep;
    private Step finalStep;
    private PreFinalStepAction preFinalStepAction;
    private Map<String, Object> overridingValues;


    public void setFinalStep(Step finalStep) {
        Step oldFinalStep = this.finalStep;
        this.finalStep = finalStep;
        support.firePropertyChange(FINAL_STEP_PROPERTY, oldFinalStep, finalStep);
    }


    public void setWizardFinished(boolean wizardFinished) {
        boolean old = this.wizardFinished;
        this.wizardFinished = wizardFinished;
        support.firePropertyChange(WIZARD_FINISHING, old, this.wizardFinished);
    }


    public boolean isAllStepFulfilled() {
        for (Step step : steps) {
            if (!step.isFulfilled()) {
                return false;
            }
        }
        return true;
    }


    public Step getCurrentStep() {
        return currentStep;
    }


    public Step getFinalStep() {
        return finalStep;
    }


    public boolean isWizardFinished() {
        return wizardFinished;
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }


    public void addPropertyChangeListener(String propertyName,
                                          PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }


    public void addStep(Step step) {
        if (steps.contains(step)) {
            throw new IllegalArgumentException("Already defined");
        }
        steps.add(step);
        if (step instanceof StepPanel) {
            ((StepPanel)step).setWizard(this);
        }
        step.addPropertyChangeListener(Step.FULFILLED_PROPERTY, stepListener);
    }


    public void removeStep(Step step) {
        if (!steps.contains(step)) {
            throw new IllegalArgumentException("Not defined");
        }
        steps.remove(step);
        if (step instanceof StepPanel) {
            ((StepPanel)step).setWizard(null);
        }
        step.removePropertyChangeListener(Step.FULFILLED_PROPERTY, stepListener);
    }


    public void executeFinalStep() {
        if (!isAllStepFulfilled()) {
            throw new IllegalStateException("Des étapes ne sont pas terminé !");
        }

        getFinalStep().start(collectStateFromStep(steps.size() - 1));
        setWizardFinished(true);
    }


    public boolean hasNextStep() {
        int currentStepIdx = steps.indexOf(currentStep);

        if (currentStepIdx == -1) {
            return steps.size() > 0;
        }
        else {
            return steps.indexOf(currentStep) + 1 != steps.size();
        }
    }


    public boolean hasPreviousStep() {
        int currentStepIdx = steps.indexOf(currentStep);
        return currentStepIdx > 0;
    }


    public void nextStep() {
        if (!canGoNext()) {
            throw new IllegalStateException("Etape courante non accompli");
        }

        Step nextStep = steps.get(steps.indexOf(currentStep) + 1);

        Map prevState = collectStateFromPreviousStep();
        nextStep.start(prevState);

        setCurrentStep(nextStep);
    }


    public void previousStep() {
        if (!canGoPrevious()) {
            throw new IllegalStateException("Impossible de revenir en arrière");
        }

        Step prevStep = steps.get(steps.indexOf(currentStep) - 1);

        getCurrentStep().cancel();

        setCurrentStep(prevStep);
    }


    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }


    public void removePropertyChangeListener(String propertyName,
                                             PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }


    protected void setCurrentStep(Step currentStep) {
        Step previous = this.currentStep;
        this.currentStep = currentStep;
        support.firePropertyChange(CURRENT_STEP_PROPERTY, previous, this.currentStep);
    }


    protected boolean canGoNext() {
        return isCurrentStepFulfilled() && hasNextStep() && !isWizardFinished();
    }


    protected boolean canGoPrevious() {
        return hasPreviousStep() && !isWizardFinished();
    }


    private boolean isCurrentStepFulfilled() {
        return currentStep == null || currentStep.isFulfilled();
    }


    private Map collectStateFromPreviousStep() {
        if (getCurrentStep() == null) {
            return new HashMap();
        }

        int idx = steps.indexOf(getCurrentStep());
        return collectStateFromStep(idx);
    }


    private Map<String, Object> collectStateFromStep(int idx) {
        Map<String, Object> prevState = new HashMap<String, Object>();
        for (int i = 0; i <= idx; i++) {
            Step step = steps.get(i);
            Map<String, Object> state = step.getState();
            prevState.putAll(state);
        }
        if (overridingValues != null) {
            prevState.putAll(overridingValues);
        }
        return prevState;
    }


    public void executePreFinalStepAction() throws PreFinalStepException {
        if (preFinalStepAction != null) {
            overridingValues = collectStateFromStep(steps.size() - 1);
            preFinalStepAction.execute(overridingValues);
        }
    }


    public void setPreFinalStepAction(PreFinalStepAction action) {
        preFinalStepAction = action;
    }


    private class StepListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            support.firePropertyChange(STEP_STATE_PROPERTY, null, evt);
        }
    }


    protected void fireStepStatePropertyChange(Object newValue) {
        support.firePropertyChange(STEP_STATE_PROPERTY, null, newValue);
    }
}
