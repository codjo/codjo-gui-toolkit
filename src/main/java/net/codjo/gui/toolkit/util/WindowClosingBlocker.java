package net.codjo.gui.toolkit.util;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

class WindowClosingBlocker {
    private final JFrame blockedWindow;
    private final JFrame window;
    private final MyWindowAdapter windowAdapter = new MyWindowAdapter();
    private final int oldCloseOperation;


    WindowClosingBlocker(JFrame blockedWindow, JFrame activeWindow) {
        this.blockedWindow = blockedWindow;
        this.window = activeWindow;
        this.oldCloseOperation = blockedWindow.getDefaultCloseOperation();

        activeWindow.addWindowListener(windowAdapter);
        blockedWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }


    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent event) {
            blockedWindow.setDefaultCloseOperation(oldCloseOperation);
            window.removeWindowListener(this);
        }
    }
}
