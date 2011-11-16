/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.calendar;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
/**
 * Handler d'une date.
 * 
 * <p>
 * Fonctionne avec le pattern 'chain of responsability'.Le premier handler de la chaîne
 * capable dont la méthode <code>handle</code> retourne <code>true </code>, gére la date
 * en question.
 * </p>
 * 
 * <p>
 * <b>NB:</b> Il est préférable de modifier le comportement par le biai des méthodes
 * <code>handle</code>.
 * </p>
 *
 * @version $Revision: 1.5 $
 */
public abstract class DateHandler {
    private DateHandler successor;

    /**
     * Positionne le handler suivant dans la chaîne.
     *
     * @param successor le nouveau successeur
     */
    public void setSuccessor(DateHandler successor) {
        this.successor = successor;
    }


    /**
     * Retourne le handler suivant dans la chaîne.
     *
     * @return le successeur
     */
    public DateHandler getSuccessor() {
        return successor;
    }


    /**
     * Determine le renderer.
     *
     * @param input La date dont il faut faire le rendu.
     * @param renderer Le renderer de la date
     * @param table sur laquelle il faut appliquer le renderer
     * @param isSelected true si la ligne est sélectionnée
     * @param hasFocus true si la ligne sélectionnée a le focus
     * @param row ligne sur laquelle le renderer va s'appliquer
     * @param column sur laquelle le renderer va s'appliquer
     *
     * @return le nouveau renderer.
     */
    final JLabel renderer(Date input, JLabel renderer, JTable table, boolean isSelected,
        boolean hasFocus, int row, int column) {
        if (handle(input)) {
            return handleRenderer(input, renderer, table, isSelected, hasFocus, row,
                column);
        }
        else if (getSuccessor() != null) {
            return getSuccessor().renderer(input, renderer, table, isSelected, hasFocus,
                row, column);
        }
        else {
            return renderer;
        }
    }


    /**
     * Determine le message associé.
     *
     * @param input nouvelle date
     *
     * @return le message explicatif ou <code>null</code>.
     */
    final String message(Date input) {
        if (handle(input)) {
            return handleMessage(input);
        }
        else if (getSuccessor() != null) {
            return getSuccessor().message(input);
        }
        else {
            return null;
        }
    }


    /**
     * Determine si la date est sélectionnable.
     *
     * @param input nouvelle date
     *
     * @return <code>true</code> si la date peut-être choisie.
     */
    final boolean selectable(Date input) {
        if (handle(input)) {
            return handleSelectable(input);
        }
        else if (getSuccessor() != null) {
            return getSuccessor().selectable(input);
        }
        else {
            return true;
        }
    }


    /**
     * Indique si ce handler prend en charge la date.
     *
     * @param input nouvelle date.
     *
     * @return <code>true</code> la date est gérée.
     */
    protected abstract boolean handle(Date input);


    /**
     * Gère le renderer.
     *
     * @param input La date dont il faut faire le rendu.
     * @param renderer Le renderer de la date
     * @param table sur laquelle il faut appliquer le renderer
     * @param isSelected true si la ligne est sélectionnée
     * @param hasFocus true si la ligne sélectionnée a le focus
     * @param row ligne sur laquelle le renderer va s'appliquer
     * @param column sur laquelle le renderer va s'appliquer
     *
     * @return le nouveau renderer.
     */
    protected JLabel handleRenderer(Date input, JLabel renderer, JTable table,
        boolean isSelected, boolean hasFocus, int row, int column) {
        return handleRenderer(input, renderer);
    }


    /**
     * Gère le renderer.
     *
     * @param input La date dont il faut faire le rendu.
     * @param renderer Le renderer de la date
     *
     * @return le nouveau renderer.
     */
    protected JLabel handleRenderer(Date input, JLabel renderer) {
        return renderer;
    }


    /**
     * Reoturne le message associé.
     *
     * @param input nouvelle date
     *
     * @return le message explicatif.
     */
    protected String handleMessage(Date input) {
        return null;
    }


    /**
     * Indique si la date est sélectionnable.
     *
     * @param input nouvelle date
     *
     * @return <code>true</code> si la date peut-être choisie.
     */
    protected boolean handleSelectable(Date input) {
        return true;
    }


    /**
     * Methode utilitaire pour savoir si la date est un week-end.
     *
     * @param input la date
     *
     * @return <code>true</code> si c'est un week-end.
     */
    protected boolean isWeekEnd(Date input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(input);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }


    /**
     * Methode utilitaire pour savoir si 2 dates sont identique (au jours près).
     * 
     * <p>
     * <b>NB</b> : Les heures, minutes et secondes ne sont pas testées.
     * </p>
     *
     * @param dateA la premiere date
     * @param dateB la deuxieme date
     *
     * @return <code>true</code> si elles sont identique.
     */
    protected boolean isSameDate(Date dateA, Date dateB) {
        Calendar calA = Calendar.getInstance();
        calA.setTime(dateA);

        Calendar calB = Calendar.getInstance();
        calB.setTime(dateB);

        return ((calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR))
        && (calA.get(Calendar.MONTH) == calB.get(Calendar.MONTH))
        && (calA.get(Calendar.DAY_OF_MONTH) == calB.get(Calendar.DAY_OF_MONTH)));
    }
}
