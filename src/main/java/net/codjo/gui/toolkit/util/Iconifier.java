package net.codjo.gui.toolkit.util;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

class Iconifier {
    private final JFrame toIconifyWindow;
    private final JFrame mainWindow;
    private final MyWindowAdapter windowAdapter = new MyWindowAdapter();


    Iconifier(JFrame toIconifyWindow, JFrame activeWindow) {
        this.toIconifyWindow = toIconifyWindow;
        this.mainWindow = activeWindow;

        activeWindow.addWindowListener(windowAdapter);
    }


    private class MyWindowAdapter extends WindowAdapter {
        @Override
        public void windowIconified(WindowEvent event) {
            GuiUtil.iconify(toIconifyWindow);
        }


        @Override
        public void windowClosing(WindowEvent event) {
            mainWindow.removeWindowListener(this);
        }
    }
}
