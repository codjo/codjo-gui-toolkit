/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
/*
 * Project: PENELOPE
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.fileChooser;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * Cette classe construit un panel de visualisation de fichiers texte.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.7 $
 */
class TextFilePreviewAccessory extends JPanel implements PropertyChangeListener {
    private File textfile;
    private JTextArea ta;

    /**
     * Constructeur.
     *
     * @param fc Le fileChooser
     */
    TextFilePreviewAccessory(JFileChooser fc) {
        setName(" Visualisation ");
        fc.addPropertyChangeListener(this);
        ta = new JTextArea();
        ta.setFont(new Font("Helvetica", Font.PLAIN, 9));
        JScrollPane sp = new JScrollPane(ta);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, sp);
        setPreferredSize(new Dimension(150, 200));
    }

    /**
     * Charge le fichier pour le visualiser.
     */
    public void loadTextfile() {
        if (textfile == null || FileChooserUtils.getExtension(textfile) == null) {
            return;
        }

        if (!FileChooserUtils.getExtension(textfile).equals("txt")) {
            ta.setText("Pas de visualisation possible !");
        }
        else {
            try {
                ta.setText("");
                BufferedReader br = new BufferedReader(new FileReader(textfile));
                String line;
                while ((line = br.readLine()) != null) {
                    ta.append(line + "\n");
                }
                br.close();
            }
            catch (IOException ex) {
                ErrorDialog.show(this, "Impossible d'afficher la prévisualisation !", ex);
            }
        }
    }


    /**
     * Listener sur le fichier sélectionné. Lorsque ce fichier change, on le visualise.
     *
     * @param event événement de changement de propriété
     */
    public void propertyChange(PropertyChangeEvent event) {
        String prop = event.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            textfile = (File)event.getNewValue();
            if (isShowing()) {
                loadTextfile();
                repaint();
            }
        }
    }
}
