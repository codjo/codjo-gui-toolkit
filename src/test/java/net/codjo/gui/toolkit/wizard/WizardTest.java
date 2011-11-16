/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import junit.framework.TestCase;
/**
 * Classe de test de {@link Wizard}
 *
 * @author $Author: torrent $
 * @version $Revision: 1.6 $
 */
public class WizardTest extends TestCase {
    private MockPropertyListener listener;
    private MockStep stepA;
    private MockStep stepB;
    private MockStep stepC;
    private Wizard wizard;


    /**
     * Cas d'ajout de la meme step plusieurs fois.
     *
     * @throws Exception
     */
    public void test_addStep() throws Exception {
        wizard.addStep(stepA);
        try {
            wizard.addStep(stepA);
            fail("Deuxieme ajout de la meme step est refusé.");
        }
        catch (Exception e) {
            ; // Cas normal
        }

        assertSame(wizard, stepA.getWizard());
    }


    public void test_removeStep() throws Exception {
        wizard.addStep(stepA);
        wizard.removeStep(stepA);
        try {
            wizard.removeStep(stepA);
            fail("Step inexistante");
        }
        catch (IllegalArgumentException e) {
            ;
        }
    }


    /**
     * L'étape final ne peut être executé que lors que les autres étapes sont accompli.
     *
     * @throws Exception
     */
    public void test_executeFinalStep() throws Exception {
        wizard.addStep(stepA);
        wizard.addStep(stepB);
        wizard.setFinalStep(stepC);
        stepA.setFulfilled(true);
        stepA.setValue("stepA", "done");

        try {
            wizard.executeFinalStep();
            fail("Une etape n'est pas accompli donc on ne peux pas excuter !");
        }
        catch (Exception e) {
            ; // Cas normal
        }

        stepB.setFulfilled(true);
        stepB.setValue("stepB", "done");

        wizard.executeFinalStep();

        assertEquals(1, stepC.start_calledTime);
        assertEquals(2, stepC.start_previousStepState.size());
        assertEquals("done", stepC.start_previousStepState.get("stepA"));
        assertEquals("done", stepC.start_previousStepState.get("stepB"));
    }


    /**
     * Verifie que l'on est capable de dire lorsque toutes les etapes sont accomplis.
     *
     * @throws Exception
     */
    public void test_isAllStepFulfilled() throws Exception {
        stepA.setFulfilled(true);
        wizard.addStep(stepA);
        assertEquals(true, wizard.isAllStepFulfilled());

        wizard.addStep(stepB);
        assertEquals(false, wizard.isAllStepFulfilled());
    }


    /**
     * Test que le n'on peux passer à l'étape suivant qu'apres avoir accompli l'étape courante.
     *
     * @throws Exception
     */
    public void test_nextStep() throws Exception {
        wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY, listener);

        assertEquals(false, wizard.hasNextStep());
        assertEquals(null, wizard.getCurrentStep());

        try {
            wizard.nextStep();
            fail("Pas d'étape suivante ! ");
        }
        catch (Exception e) {
            ; // Cas normal
        }

        wizard.addStep(stepA);
        wizard.addStep(stepB);
        wizard.addStep(stepC);

        assertEquals(true, wizard.hasNextStep());
        assertEquals(null, wizard.getCurrentStep());
        assertEquals(0, listener.propertyChangeCalledTimes);

        wizard.nextStep();

        assertEquals(true, wizard.hasNextStep());
        assertEquals(stepA, wizard.getCurrentStep());
        assertEquals(1, listener.propertyChangeCalledTimes);
        assertEquals(Wizard.CURRENT_STEP_PROPERTY, listener.evt.getPropertyName());
        assertEquals(null, listener.evt.getOldValue());
        assertEquals(stepA, listener.evt.getNewValue());

        stepA.setFulfilled(true);
        wizard.nextStep();

        assertEquals(true, wizard.hasNextStep());
        assertEquals(stepB, wizard.getCurrentStep());
        assertEquals(2, listener.propertyChangeCalledTimes);
        assertEquals(Wizard.CURRENT_STEP_PROPERTY, listener.evt.getPropertyName());
        assertEquals(stepA, listener.evt.getOldValue());
        assertEquals(stepB, listener.evt.getNewValue());

        try {
            wizard.nextStep();
            fail("Existe étape suivante mais l'étape courante n'est pas accompli! ");
        }
        catch (Exception e) {
            ; // Cas normal
        }

        stepB.setFulfilled(true);
        wizard.nextStep();
        assertEquals(false, wizard.hasNextStep());
        assertEquals(stepC, wizard.getCurrentStep());

        stepC.setFulfilled(true);
        try {
            wizard.nextStep();
            fail("N'existe pas d'étape suivante!");
        }
        catch (Exception e) {
            ; // Cas normal
        }
    }


    /**
     * Test que le passage à l'étape suivante entraine l'appel de la methode start avec comme argument les
     * etats des precedente étapes.
     *
     * @throws Exception
     */
    public void test_nextStep_start() throws Exception {
        wizard.addStep(stepA);
        wizard.addStep(stepB);
        wizard.addStep(stepC);

        wizard.nextStep();

        assertEquals(stepA, wizard.getCurrentStep());
        assertEquals(1, stepA.start_calledTime);
        assertEquals(0, stepA.start_previousStepState.size());

        stepA.setFulfilled(true);
        stepA.setValue("stepA", "done");
        wizard.nextStep();

        assertEquals(stepB, wizard.getCurrentStep());
        assertEquals(1, stepB.start_calledTime);
        assertEquals("done", stepB.start_previousStepState.get("stepA"));

        stepB.setFulfilled(true);
        stepB.setValue("stepB", "done");
        wizard.nextStep();

        assertEquals(stepC, wizard.getCurrentStep());
        assertEquals(1, stepC.start_calledTime);
        assertEquals(2, stepC.start_previousStepState.size());
        assertEquals("done", stepC.start_previousStepState.get("stepA"));
        assertEquals("done", stepC.start_previousStepState.get("stepB"));
    }


    /**
     * Test que le n'on peux passer à l'étape precedente.
     *
     * @throws Exception
     */
    public void test_previousStep() throws Exception {
        wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY, listener);

        wizard.addStep(stepA);
        wizard.addStep(stepB);
        stepA.setFulfilled(true);
        stepB.setFulfilled(true);

        wizard.nextStep();

        assertEquals(false, wizard.hasPreviousStep());
        assertEquals(1, listener.propertyChangeCalledTimes);

        wizard.nextStep();

        assertEquals(true, wizard.hasPreviousStep());
        assertEquals(2, listener.propertyChangeCalledTimes);

        wizard.previousStep();

        assertEquals(false, wizard.hasPreviousStep());
        assertEquals(3, listener.propertyChangeCalledTimes);
        assertEquals(Wizard.CURRENT_STEP_PROPERTY, listener.evt.getPropertyName());
        assertEquals(stepB, listener.evt.getOldValue());
        assertEquals(stepA, listener.evt.getNewValue());
    }


    /**
     * Test que le n'on revient à l'étape precedente l'étape courante est annulée.
     *
     * @throws Exception
     */
    public void test_previousStep_cancel() throws Exception {
        wizard.addStep(stepA);
        wizard.addStep(stepB);
        stepA.setFulfilled(true);
        stepB.setFulfilled(true);

        wizard.nextStep();
        wizard.nextStep();

        wizard.previousStep();

        assertEquals(1, stepB.cancel_calledTime);
        assertEquals(0, stepA.cancel_calledTime);
    }


    public void test_previousStep_error() throws Exception {
        wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY, listener);

        assertEquals(false, wizard.hasPreviousStep());

        try {
            wizard.previousStep();
            fail("Pas d'étape precedente ! ");
        }
        catch (Exception e) {
            ; // Cas normal
        }
        assertEquals(0, listener.propertyChangeCalledTimes);
    }


    protected void setUp() throws Exception {
        wizard = new Wizard();
        stepA = new MockStep("step_A");
        stepB = new MockStep("step_B");
        stepC = new MockStep("step_C");
        listener = new MockPropertyListener();
    }
}
