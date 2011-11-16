/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.text;
import net.codjo.gui.toolkit.AbstractJFCTestCase;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.finder.DialogFinder;
/**
 * Test la classe TextArea.
 */
public class TextAreaTest extends AbstractJFCTestCase {
    private JFrame window = null;
    private TextArea textArea;


    public void test_max_length_keyboard() throws Exception {
        getHelper().sendString(new StringEventData(this, textArea, "012345"));

        DialogFinder dialogFinder = new DialogFinder("Message");

        List windows = dialogFinder.findAll(window);
        assertEquals("Number of dialogs showing is wrong", 1, windows.size());
        JDialog dialog = (JDialog)windows.get(0);
        assertEquals("Wrong dialog showing up", "Message", dialog.getTitle());

        assertEquals("01234", textArea.getText());
    }


    public void test_saisie() throws Exception {
        getHelper().sendString(new StringEventData(this, textArea, "01234"));

        assertEquals("01234", textArea.getText());
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        textArea = new TextArea("texte de test", 5);
        buildWindow();
        setHelper(new JFCTestHelper());
        setFocus(textArea);
        assertTrue("Les tests considèrent que le champs à le focus", textArea.hasFocus());
    }


    @Override
    protected void tearDown() throws Exception {
        TestHelper.disposeWindow(window, this);
        window = null;
        TestHelper.cleanUp(this);
        super.tearDown();
    }


    private void buildWindow() {
        window = new JFrame("Test " + getName());
        window.getContentPane().add(textArea, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
    }
}
