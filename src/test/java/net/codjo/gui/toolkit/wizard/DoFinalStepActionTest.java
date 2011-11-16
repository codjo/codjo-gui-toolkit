/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.util.Map;
import javax.swing.JButton;
import junit.framework.TestCase;
/**
 * Classe de test de {@link DoFinalStepAction}
 *
 * @author $Author: torrent $
 * @version $Revision: 1.8 $
 */
public class DoFinalStepActionTest extends TestCase {
    private static final String FINAL_STEP_NAME = "FINAL";
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
        assertEquals(FINAL_STEP_NAME, button.getText());
        assertEquals("Execute l'assistant", button.getToolTipText());
    }


    /**
     * Test que le boutton n'est accessible que lorsque l'on peux passer à l'étape suivante.
     *
     * @throws Exception
     */
    public void test_isEnabled() throws Exception {
        assertEquals(false, button.isEnabled());

        stepA.setFulfilled(true);
        stepB.setFulfilled(true);

        assertEquals(true, button.isEnabled());

        button.doClick();

        assertEquals(false, button.isEnabled());
    }


    public void test_preFinalStepAction() throws Exception {
        String toTransmit = "la tete a toto";
        MockPreFinalStepAction prefinalStepAction = new MockPreFinalStepAction();
        prefinalStepAction.setValueToTransmit(toTransmit);
        wizard.setPreFinalStepAction(prefinalStepAction);

        stepA.setFulfilled(true);
        stepB.setFulfilled(true);

        assertEquals(true, button.isEnabled());

        button.doClick();
        assertEquals(toTransmit, finalStep.getStateReaded().get("key1"));
    }


    protected void setUp() throws Exception {
        wizard = new Wizard();
        stepA = new MockStep("stepA");
        stepB = new MockStep("stepB");
        finalStep = new MockStep(FINAL_STEP_NAME);
        wizard.addStep(stepA);
        wizard.addStep(stepB);
        wizard.setFinalStep(finalStep);

        DoFinalStepAction action = new DoFinalStepAction(wizard);

        button = new JButton();
        button.setAction(action);
    }


    private class MockPreFinalStepAction implements PreFinalStepAction {
        private String valueToTransmit;


        public void setValueToTransmit(String valueToTransmit) {
            this.valueToTransmit = valueToTransmit;
        }


        public void execute(Map data) throws PreFinalStepException {
            data.put("key1", valueToTransmit);
        }
    }
}
