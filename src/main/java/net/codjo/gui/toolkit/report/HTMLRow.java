/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.report;
/**
 * Represente une ligne du rapport.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
class HTMLRow {
    static final int OK = 0;
    static final int NOT_STARTED = 1;
    static final int STARTED = 2;
    static final int ERROR = 3;
    private int state = NOT_STARTED;
    private String title;
    private String errorMessage;
    private HTMLRow prevStep;

    HTMLRow(String title, HTMLRow prevStep) {
        this.title = title;
        this.prevStep = prevStep;
    }

    public int getState() {
        return state;
    }


    public void start() {
        state = STARTED;
        if (prevStep != null) {
            prevStep.close();
        }
    }


    public void close() {
        state = OK;
        if (prevStep != null) {
            prevStep.close();
        }
    }


    public void declareError(Throwable exception) {
        declareError(exception.getLocalizedMessage());
    }


    public void declareError(String message) {
        state = HTMLRow.ERROR;
        errorMessage = message;
    }


    public String buildReport() {
        return "<tr>" + "  <th align=\"left\" valign=\"top\" bgcolor=\"#EEEEFF\" >"
        + title + "</th>" + "  <td align=\"right\" valign=\"top\" bgcolor=\""
        + getStateColor() + "\">" + getStateMessage() + "</td>" + "</tr>";
    }


    private String getStateColor() {
        switch (state) {
            case OK:
                return "#AAFFAA";
            case NOT_STARTED:
                return "#EEEEEE";
            case STARTED:
                return "#EEFFEE";
            default:
                return "#FF0000";
        }
    }


    private String getStateMessage() {
        switch (state) {
            case OK:
                return "OK";
            case NOT_STARTED:
                return "...";
            case STARTED:
                return "En cours...";
            default:
                if (errorMessage.startsWith("EJB Exception")) {
                    return "Erreur Interne !";
                }
                else {
                    return errorMessage;
                }
        }
    }
}
