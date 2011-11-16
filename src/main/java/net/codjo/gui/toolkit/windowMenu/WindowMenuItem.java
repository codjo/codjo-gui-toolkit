/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
/**
 * Item representant une fenêtre.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 *
 * @see WindowMenuManager
 */
class WindowMenuItem extends JCheckBoxMenuItem implements ActionListener {
    private JInternalFrame frame = null;
    private WindowMenuManager manager = null;

    WindowMenuItem(WindowMenuManager manager, JInternalFrame frame, int mnemonic) {
        if (manager == null || frame == null) {
            throw new IllegalArgumentException();
        }

        this.manager = manager;
        this.frame = frame;
        this.frame.addInternalFrameListener(new FrameStateListener(this.manager, this));
        this.addActionListener(this);

        setTextAndMnemonic(mnemonic);
    }

    public void actionPerformed(ActionEvent parm1) {
        try {
            manager.activateFrame(frame);
        }
        catch (PropertyVetoException veto) {
            getToolkit().beep();
            setState(frame.isSelected());
        }
    }


    JInternalFrame getFrame() {
        return frame;
    }


    void setTextAndMnemonic(int mnemonic) {
        if (mnemonic < 10) {
            this.setText(mnemonic + " " + frame.getTitle());
            setMnemonic(Integer.toString(mnemonic).charAt(0));
            this.setVisible(true);
        }
        else {
            this.setText(frame.getTitle());
            this.setVisible(false);
        }
    }
}
