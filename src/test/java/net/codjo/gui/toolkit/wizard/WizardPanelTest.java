/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import junit.framework.TestCase;
/**
 * Classe de test de {@link WizardPanel}
 *
 * @author $Author: blazart $
 * @version $Revision: 1.5 $
 */
public class WizardPanelTest extends TestCase {
    //    private WizardPanel panel;
    public static void main(String[] array) throws Exception {
//      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame window = new JFrame();
        WizardPanel contentPane = new WizardPanel();
        window.setContentPane(contentPane);

        Wizard wizard = new Wizard();

        MockStep stepA = new MockStep("Step A");
        stepA.add(new JLabel("Content de A"));
        stepA.add(new FulfilledButton(stepA));
        wizard.addStep(stepA);

        MockStep stepB = new MockStep("Step B");
        stepB.add(new JLabel("Content de B"));
        stepB.add(new FulfilledButton(stepB));
        wizard.addStep(stepB);

        MockStep stepF = new MockStep("Import");
        stepF.add(new JLabel("Import en cours"));
        stepF.add(new FulfilledButton(stepF));
        wizard.setFinalStep(stepF);

        contentPane.setWizard(wizard);
        contentPane.setWizardIcon(new ImageIcon("src/images/wizard.import.gif"));

        window.pack();
        window.setVisible(true);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
        contentPane.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
    }


    public void test_empty() throws Exception {
    }


    protected void setUp() throws Exception {
//        panel = new WizardPanel();
    }


    private static class FulfilledButton extends JButton {
        StepPanel step;


        FulfilledButton(StepPanel stepO) {
            this.step = stepO;
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    step.setFulfilled(!step.isFulfilled());
                    setText((step.isFulfilled() ? "Annuler " + step.getName()
                             : "Accomplir " + step.getName()));
                }
            });
            setText("Accomplir " + step.getName());
        }
    }
}
