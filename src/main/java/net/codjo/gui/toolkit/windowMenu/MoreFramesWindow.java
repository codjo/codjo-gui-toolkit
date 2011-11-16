/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.windowMenu;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
/**
 * Suite des fenêtres.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.9 $
 */
public class MoreFramesWindow extends JInternalFrame {
    private JLabel activateLabel = new JLabel();
    private JList activateList = null;
    private Border border1;
    private JButton cancelButton = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton okButton = new JButton();

    public MoreFramesWindow() {
        jbInit();
    }

    private void jbInit() {
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(178, 178, 178));
        this.getContentPane().setLayout(gridBagLayout1);
        DefaultListModel model = new DefaultListModel();
        model.addElement("one");
        activateList = new JList(model);
        activateList.setBorder(border1);
        activateLabel.setText("Activer:");
        this.setNormalBounds(new Rectangle(10, 10, 520, 300));
        this.setTitle("Suite des fenêtres...");
        this.setPreferredSize(new Dimension(520, 300));
        okButton.setMaximumSize(new Dimension(80, 27));
        okButton.setMinimumSize(new Dimension(80, 27));
        okButton.setPreferredSize(new Dimension(80, 27));
        okButton.setMnemonic('O');
        okButton.setText("Ok");
        cancelButton.setMaximumSize(new Dimension(80, 27));
        cancelButton.setMinimumSize(new Dimension(80, 27));
        cancelButton.setPreferredSize(new Dimension(80, 27));
        cancelButton.setMnemonic('A');
        cancelButton.setText("Annuler");
        this.getContentPane().add(activateLabel,
            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(10, 12, 0, 0), 0, 0));
        this.getContentPane().add(activateList,
            new GridBagConstraints(0, 1, 1, 2, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 10, 10, 0), 0, 0));
        this.getContentPane().add(okButton,
            new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        this.getContentPane().add(cancelButton,
            new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                GridBagConstraints.NONE, new Insets(10, 10, 0, 10), 0, 0));
    }
}
