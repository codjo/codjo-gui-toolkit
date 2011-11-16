/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.JComponent;
/**
 * Definition d'une étape.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public interface Step {
    // -----------------------------------------------------------------------------------------------------------------
    // Event
    // -----------------------------------------------------------------------------------------------------------------
    public static final String FULFILLED_PROPERTY = "fulfilled";

    // -----------------------------------------------------------------------------------------------------------------
    // State
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Indque si l'étape est accompli.
     *
     * @return true si accompli.
     */
    public boolean isFulfilled();


    /**
     * Retourne le nom de l'étape.
     *
     * @return Nom de l'étape.
     */
    public String getName();


    /**
     * Le composant graphique de cette étape.
     *
     * @return Le composant d'édition de cette étape.
     */
    public JComponent getGui();


    /**
     * Démarre cette étape.
     *
     * @param previousStepState Etat des précédentes étape.
     */
    public void start(Map previousStepState);


    /**
     * Annule et re-initialise cette étape. Cette methode est appelé lors d'un retour en
     * arrière.
     */
    public void cancel();


    /**
     * Retourne l'état de l'étape.
     *
     * @return une map.
     */
    public Map<String, Object> getState();


    public void addPropertyChangeListener(String propertyName,
        PropertyChangeListener listener);


    public void removePropertyChangeListener(String propertyName,
        PropertyChangeListener listener);
}
