package net.codjo.gui.toolkit.util;
import java.awt.Component;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class FocusChangeBlocker {
    private final JFrame blockedWindow;
    private final JFrame activeWindow;
    private final MyWindowAdapter windowAdapter = new MyWindowAdapter();
    private final MyVetoableChangeListener vetoableChangeListener = new MyVetoableChangeListener();


    FocusChangeBlocker(JFrame blockedWindow, JFrame activeWindow) {
        this.blockedWindow = blockedWindow;
        this.activeWindow = activeWindow;

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
              .addVetoableChangeListener(vetoableChangeListener);
        activeWindow.addWindowListener(windowAdapter);
    }


    private void manageIconification() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (activeWindow.getState() == Frame.ICONIFIED) {
                    GuiUtil.deiconify(activeWindow);
                }
            }
        });
    }


    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            activeWindow.removeWindowListener(this);
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                  .removeVetoableChangeListener(vetoableChangeListener);
        }
    }

    private class MyVetoableChangeListener implements VetoableChangeListener {
        public void vetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
            Component nextFocusedComponent = (Component)event.getNewValue();
            if (nextFocusedComponent == blockedWindow
                || nextFocusedComponent != null &&
                   SwingUtilities.windowForComponent(nextFocusedComponent) == blockedWindow) {
                manageIconification();
                throw new PropertyVetoException("Non, ecran detail gagne", event);
            }

            JFrame oldWindow = getFrame(event.getOldValue());
            JFrame activeWindow = getFrame(
                  KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow());

            if ("activeWindow".equals(event.getPropertyName())
                && oldWindow == blockedWindow
                && activeWindow == null) {
                FocusChangeBlocker.this.activeWindow.toFront();
                throw new PropertyVetoException("Non, ecran detail gagne", event);
            }
        }


        private JFrame getFrame(Object component) {
            JFrame newWindow = null;
            if (component instanceof JFrame) {
                newWindow = (JFrame)component;
            }
            else if (component != null) {
                newWindow = (JFrame)SwingUtilities.windowForComponent((Component)component);
            }
            return newWindow;
        }
    }
}
