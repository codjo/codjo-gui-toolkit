/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
/**
 * Action pour passer à l'étape suivante.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class NextStepAction extends AbstractAction implements PropertyChangeListener {
    private static final Logger APP = Logger.getLogger(NextStepAction.class);
    private Wizard wizard;


    public NextStepAction(Wizard wizard) {
        putValue(NAME, "Suivant >");
        putValue(SHORT_DESCRIPTION, "Etape suivante");
        this.wizard = wizard;

        setEnabled(this.wizard.canGoNext());
        this.wizard.addPropertyChangeListener(Wizard.STEP_STATE_PROPERTY, this);
        this.wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY, this);
        this.wizard.addPropertyChangeListener(Wizard.WIZARD_FINISHING, this);
    }


    public void actionPerformed(ActionEvent evt) {
        try {
            this.wizard.nextStep();
        }
        catch (Exception e) {
            String message = "Impossible de passer à l'étape suivante : " + e;
            APP.error(message, e);
            JOptionPane.showMessageDialog(null, message);
        }
    }


    public void propertyChange(PropertyChangeEvent evt) {
        setEnabled(this.wizard.canGoNext());
    }
}
