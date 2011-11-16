/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
/**
 * Action pour passer à l'étape précédente.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class PreviousStepAction extends AbstractAction implements PropertyChangeListener {
    private static final Logger APP = Logger.getLogger(PreviousStepAction.class);
    private Wizard wizard;

    public PreviousStepAction(Wizard wizard) {
        putValue(NAME, "< Précédent");
        putValue(SHORT_DESCRIPTION, "Etape précédente");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
        this.wizard = wizard;

        setEnabled(this.wizard.canGoPrevious());
        this.wizard.addPropertyChangeListener(Wizard.STEP_STATE_PROPERTY, this);
        this.wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY, this);
        this.wizard.addPropertyChangeListener(Wizard.WIZARD_FINISHING, this);
    }

    public void actionPerformed(ActionEvent evt) {
        try {
            this.wizard.previousStep();
        }
        catch (Exception e1) {
            String message = "Impossible de passer à l'étape précédente : " + e1;
            APP.error(message, e1);
            JOptionPane.showMessageDialog(null, message);
        }
    }


    public void propertyChange(PropertyChangeEvent evt) {
        setEnabled(this.wizard.canGoPrevious());
    }
}
