/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.report;
import java.text.MessageFormat;
import javax.swing.JEditorPane;
/**
 * Rapport d'execution en HTML.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
public class HTMLReport extends JEditorPane {
    private HTMLPart[] parts = {};
    private String headerPattern = null;
    private Object[] headerArguments = {};

    public HTMLReport() {
        setContentType("text/html");
        setEditable(false);
    }

    public void setHTMLParts(HTMLPart[] parts) {
        this.parts = parts;
        for (HTMLPart part : parts) {
            part.setReport(this);
        }
    }


    public void setHeaderArguments(Object[] headerArguments) {
        this.headerArguments = headerArguments;
    }


    public void setHeaderPattern(String headerPattern) {
        this.headerPattern = headerPattern;
    }


    public Object[] getHeaderArguments() {
        return headerArguments;
    }


    public String getHeaderPattern() {
        return headerPattern;
    }


    public String buildReport() {
        StringBuffer buffer = new StringBuffer("<html><body bgcolor=\"F5F5F5\">");
        buffer.append(getHeaderReport());
        buffer.append("<table border=\"0\" cellspacing=\"2\" width=\"100%\">");

        for (HTMLPart part : parts) {
            buffer.append(part.buildReport());
        }

        buffer.append("</table></body></html>");
        return buffer.toString();
    }


    void updateGui() {
        setText(buildReport());
    }


    private String getHeaderReport() {
        if (getHeaderPattern() != null && getHeaderArguments() != null) {
            return MessageFormat.format(getHeaderPattern(), getHeaderArguments()) + "<p>";
        }
        else {
            return "";
        }
    }
}
