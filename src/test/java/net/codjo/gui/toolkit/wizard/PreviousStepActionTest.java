/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import javax.swing.JButton;
import junit.framework.TestCase;
/**
 * Classe de test de {@link PreviousStepAction}
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
public class PreviousStepActionTest extends TestCase {
    private PreviousStepAction action;
    private JButton button;
    private MockStep finalStep;
    private MockStep stepA;
    private MockStep stepB;
    private Wizard wizard;


    /**
     * Test l'affichage de l'action.
     *
     * @throws Exception
     */
    public void test_display() throws Exception {
        assertEquals("< Précédent", button.getText());
        assertEquals("Etape précédente", button.getToolTipText());
//        assertEquals(KeyEvent.VK_P, button.getMnemonic());
    }


    /**
     * Test que le boutton n'est accessible que lorsque l'on peux passer à l'étape suivante.
     *
     * @throws Exception
     */
    public void test_isEnabled() throws Exception {
        assertEquals(false, button.isEnabled());

        wizard.nextStep();
        assertEquals(stepA, wizard.getCurrentStep());
        assertEquals(false, button.isEnabled());

        stepA.setFulfilled(true);
        wizard.nextStep();
        assertEquals(stepB, wizard.getCurrentStep());
        assertEquals(true, button.isEnabled());

        button.doClick();
        assertEquals(stepA, wizard.getCurrentStep());
        assertEquals(false, button.isEnabled());
    }


    /**
     * Test que le boutton n'est plus accessible des que l'on execute la cible final.
     *
     * @throws Exception
     */
    public void test_isEnabled_lastStep() throws Exception {
        assertEquals(false, button.isEnabled());

        wizard.nextStep();
        stepA.setFulfilled(true);
        stepB.setFulfilled(true);
        wizard.nextStep();
        assertEquals(true, button.isEnabled());

        wizard.executeFinalStep();
        assertEquals(false, button.isEnabled());
    }


    protected void setUp() throws Exception {
        wizard = new Wizard();
        stepA = new MockStep("stepA");
        stepB = new MockStep("stepB");
        finalStep = new MockStep("FINAL_STEP_NAME");
        wizard.addStep(stepA);
        wizard.addStep(stepB);
        wizard.setFinalStep(finalStep);

        action = new PreviousStepAction(wizard);

        button = new JButton();
        button.setAction(action);
    }
}
