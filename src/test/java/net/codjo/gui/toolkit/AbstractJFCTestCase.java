package net.codjo.gui.toolkit;

import java.awt.Component;
import junit.extensions.jfcunit.JFCTestCase;

public abstract class AbstractJFCTestCase extends JFCTestCase {
    public void setFocus(Component component) {
        long start = System.currentTimeMillis();
        do {
            component.requestFocusInWindow();
            flushAWT();
        }
        while (!component.hasFocus() && System.currentTimeMillis() - start < 500);
        assertTrue("Les tests considèrent que le champ a le focus", component.hasFocus());
    }


    public void transferFocus(Component component) {
        long start = System.currentTimeMillis();
        do {
            component.transferFocus();
            flushAWT();
        }
        while (component.hasFocus() && System.currentTimeMillis() - start < 500);
        assertTrue("Les tests considèrent que le champ n'a pas le focus", !component.hasFocus());
    }
}
