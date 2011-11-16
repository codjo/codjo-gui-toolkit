/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.waiting;
import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
/**
 * Fenetre à afficher lors de l'execution de process long.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 */
public class WaitingWindow extends JInternalFrame {
    public static final ImageIcon WAIT_ICON =
        new ImageIcon(WaitingWindow.class.getResource("waitingIcon.gif"));
    private JLabel iconLabel = new JLabel();
    private JLabel messageLabel = new JLabel();

    public WaitingWindow(String label) {
        jbInit();
        if (label != null) {
            messageLabel.setText(label);
        }
    }

    public static JInternalFrame displayWaintingWindow(JDesktopPane dp) {
        return displayWaintingWindow(null, dp);
    }


    public static JInternalFrame displayWaintingWindow(String label, JDesktopPane dp) {
        WaitingWindow window = new WaitingWindow(label);
        dp.add(window);
        window.pack();
        window.setVisible(true);
        try {
            window.setSelected(true);
        }
        catch (PropertyVetoException veto) {
            ; // Tant pis
        }
        return window;
    }


    private void jbInit() {
        this.setSize(260, 100);
        messageLabel.setFont(new java.awt.Font("SansSerif", Font.BOLD, 12));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        messageLabel.setText("Chargement en cours...");
        iconLabel.setIcon(WAIT_ICON);
        this.setTitle("Patientez");
        this.getContentPane().setBackground(UIManager.getColor("Panel.background"));
        this.getContentPane().add(iconLabel, BorderLayout.WEST);
        this.getContentPane().add(messageLabel, BorderLayout.CENTER);
    }
}
