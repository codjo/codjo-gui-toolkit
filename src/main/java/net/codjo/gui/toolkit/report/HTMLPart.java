/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.report;
/**
 * Représente un processus du report (soit 2 lignes).
 * 
 * <p>
 * Exemple: un appel VTOM (call plus processing), un import...
 * </p>
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.6 $
 */
public class HTMLPart {
    private HTMLReport report = null;
    private HTMLRow call;
    private HTMLRow processing;
    private String title;

    public HTMLPart(String title) {
        this(title, null);
    }


    public HTMLPart(String title, HTMLPart previousProcess) {
        this.title = title;
        if (previousProcess != null) {
            call = new HTMLRow("Demande", previousProcess.processing);
        }
        else {
            call = new HTMLRow("Demande", null);
        }
        processing = new HTMLRow("Traitement", call);
    }

    public boolean isProcessingFinished() {
        return processing.getState() == HTMLRow.OK;
    }


    public void startProcessing() {
        processing.start();
        updateReport();
    }


    public void startCall() {
        call.start();
        updateReport();
    }


    public void endProcessing() {
        processing.close();
        updateReport();
    }


    public void declareCallError(Throwable exception) {
        call.declareError(exception);
        updateReport();
    }


    public void declareCallError(String message) {
        call.declareError(message);
        updateReport();
    }


    public void declareProcessingError(String message) {
        processing.declareError(message);
        updateReport();
    }


    public String buildReport() {
        return "<tr>" + "<td><strong>" + title + "</strong></td>" + "</tr>"
        + call.buildReport() + processing.buildReport();
    }


    private void updateReport() {
        if (report != null) {
            report.updateGui();
        }
    }


    HTMLReport getReport() {
        return report;
    }


    void setReport(HTMLReport report) {
        this.report = report;
    }
}
