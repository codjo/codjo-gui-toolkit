/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.apache.log4j.Logger;
/**
 * Execution de la final.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.12 $
 */
public class DoFinalStepAction extends AbstractAction implements PropertyChangeListener {
    private static final Logger APP = Logger.getLogger(DoFinalStepAction.class);
    private Wizard wizard;

    public DoFinalStepAction(Wizard wizard) {
        putValue(NAME, wizard.getFinalStep().getName());
        putValue(SHORT_DESCRIPTION, "Execute l'assistant");
        this.wizard = wizard;

        setEnabled(this.wizard.isAllStepFulfilled());
        addStepStatePropertyListener();
    }


    public void addStepStatePropertyListener() {
        this.wizard.addPropertyChangeListener(Wizard.STEP_STATE_PROPERTY, this);
    }


    public void actionPerformed(ActionEvent event) {
        try {
            this.wizard.executePreFinalStepAction();
            this.wizard.executeFinalStep();
            this.wizard.removePropertyChangeListener(Wizard.STEP_STATE_PROPERTY, this);
            setEnabled(false);
        }
        catch (Exception e) {
            String message = "Impossible d'executer : " + e;
            APP.error(message, e);
            ErrorDialog.show(null, message, e);
        }
    }


    public void propertyChange(PropertyChangeEvent evt) {
        setEnabled(this.wizard.isAllStepFulfilled());
    }
}
