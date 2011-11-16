/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.path;
import net.codjo.gui.toolkit.fileChooser.FileChooserManager;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DirectoryPathField extends JPanel {
    public static final String DIRECTORY_PROPERTY = "directoryName";
    private static final ImageIcon SEARCH_ICON = new ImageIcon(DirectoryPathField.class.getResource("search.png"));

    private JLabel directoryLabel = new JLabel();
    private JTextField directoryNameField = new JTextField();
    private JButton selectButton = new JButton();

    private String defaultFolder = "";

    public DirectoryPathField() {
        jbInit();
    }

    @Override
    public void setBackground(Color fg) {
        if (directoryNameField == null) {
            super.setBackground(fg);
            return;
        }
        directoryNameField.setBackground(fg);
    }


    public void setDefaultFolder(String defaultFolder) {
        this.defaultFolder = defaultFolder;
        this.directoryNameField.setText(defaultFolder);
    }


    @Override
    public void setForeground(Color fg) {
        if (directoryNameField == null) {
            super.setForeground(fg);
            return;
        }
        directoryNameField.setForeground(fg);
    }


    public void setLabel(String label) {
        directoryLabel.setText(label);
    }


    public void setLabelSize(int labelSize) {
        directoryLabel.setPreferredSize(new Dimension(labelSize,
                directoryLabel.getPreferredSize().height));
        this.invalidate();
    }


    public String getDefaultFolder() {
        return defaultFolder;
    }


    public File getDirectory() {
        if ("".equals(directoryNameField.getText().trim())) {
            return null;
        }
        return new File(directoryNameField.getText().trim());
    }


    public JTextField getDirectoryNameField() {
        return directoryNameField;
    }


    public String getLabel() {
        return directoryLabel.getText();
    }


    public int getLabelSize() {
        return directoryLabel.getPreferredSize().width;
    }


    void selectPath() {
        String oldDirectory = directoryNameField.getText();

        String newDirectory =
            FileChooserManager.showChooserForOpen(determineRootFolder(),
                "Selection du répertoire", JFileChooser.DIRECTORIES_ONLY);

        if (newDirectory == null) {
            newDirectory = "";
        }

        try {
            fireVetoableChange(DIRECTORY_PROPERTY, oldDirectory, newDirectory);
            directoryNameField.setText(newDirectory);
            firePropertyChange(DIRECTORY_PROPERTY, oldDirectory, newDirectory);
        }
        catch (PropertyVetoException ex) {
            Toolkit.getDefaultToolkit().beep();
            ErrorDialog.show(this, "Répertoire non conforme ", ex.getMessage());
        }
    }


    private String determineRootFolder() {
        String rootFolder = defaultFolder;
        if (directoryNameField.getText().length() > 0) {
            rootFolder = directoryNameField.getText();
        }
        return rootFolder;
    }


    private void jbInit() {
        selectButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    selectPath();
                }
            });

        selectButton.setBorder(BorderFactory.createEmptyBorder());
        selectButton.setIcon(SEARCH_ICON);
        selectButton.setPreferredSize(new Dimension(40, 21));
        selectButton.setMinimumSize(new Dimension(40, 21));
        selectButton.setName("DirectoryPathField.selectButton");
        selectButton.setToolTipText("Rechercher le répertoire");
        this.setLayout(new GridBagLayout());
        directoryLabel.setLabelFor(directoryNameField);
        directoryNameField.setEditable(false);
        directoryNameField.setName("DirectoryPathField.directoryNameField");
        this.add(directoryLabel,
            new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, -5, 0), 0, 0));
        this.add(directoryNameField,
            new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
        this.add(selectButton,
            new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    }
}
