/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.progressbar;
import junit.framework.TestCase;
/**
 * Classe de test de {@link net.codjo.gui.toolkit.progressbar.ProgressBarLabel}.
 */
public class ProgressBarLabelTest extends TestCase {
    private ProgressBarLabel progressBarLabel;


    public void test_setText() {
        String text = "contenu";
        progressBarLabel.setText(text);
        assertEquals(text, progressBarLabel.getText());
    }


    protected void setUp() throws Exception {
        progressBarLabel = new ProgressBarLabel();
    }


    protected void tearDown() {
        progressBarLabel = null;
    }
}
