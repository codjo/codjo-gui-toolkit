/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import org.apache.log4j.Logger;
/**
 * Affiche une fenêtre "A propos" contenant les evolutions de l'application.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class AboutWindow extends JDialog implements ActionListener {
    private static final Logger APP = Logger.getLogger(AboutWindow.class);
    private JLabel applicationLabel = new JLabel("Application : ");
    private JLabel versionLabel = new JLabel("Version actuelle : ");
    private JEditorPane historicText = new JEditorPane();
    private URL historicHTMLFile;
    private JButton quitButton = new JButton("Fermer");


    /**
     * Constructeur de AboutWindow.
     *
     * @param parent           Fenêtre parente.
     * @param applicationName  Le nom de l'application
     * @param version          La version de l'application
     * @param historicHTMLFile L'URL du fichier HTML contenant l'historique des évolutions
     */
    public AboutWindow(Frame parent, String applicationName, String version,
                       URL historicHTMLFile) {
        super(parent);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
            applicationLabel.setText("<html>" + applicationLabel.getText() + "<b>"
                                     + applicationName + "</b>");
            versionLabel.setText("<html>" + versionLabel.getText() + "<b>" + version
                                 + "</b>");
            historicText.setPage(historicHTMLFile);
        }
        catch (Exception ex) {
            APP.error("URL invalide : " + historicHTMLFile.toString(), ex);
        }
        this.historicHTMLFile = historicHTMLFile;
    }


    /**
     * Fermer le dialogue sur un événement bouton.
     *
     * @param event Evenement.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == quitButton) {
            dispose();
        }
    }


    /**
     * Permet de sortir quand la fenêtre est fermée.
     *
     * @param event Evenement.
     */
    @Override
    protected void processWindowEvent(WindowEvent event) {
        if (event.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose();
        }
        super.processWindowEvent(event);
    }


    void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
            return;
        }
        if (!historicSetPage(event.getURL())) {
            historicSetPage(historicHTMLFile);
        }
    }


    private boolean historicSetPage(URL url) {
        try {
            historicText.setPage(url);
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }


    private void jbInit() {
        setSize(600, 500);
        setTitle("A propos");

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        rootPanel.add(createTopPanel(), BorderLayout.NORTH);
        rootPanel.add(createCenterPanel(), BorderLayout.CENTER);
        rootPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        getContentPane().add(rootPanel, null);
    }


    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        bottomPanel.setLayout(flowLayout);
        quitButton.setActionCommand("Fermer");
        quitButton.addActionListener(this);
        bottomPanel.add(quitButton, null);

        return bottomPanel;
    }


    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setMaximumSize(new Dimension(400, 110));
        topPanel.setMinimumSize(new Dimension(400, 110));
        topPanel.setPreferredSize(new Dimension(400, 110));

        topPanel.add(applicationLabel,
                     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                                            GridBagConstraints.HORIZONTAL, new Insets(5, 5, 2, 0), 0, 0));

        topPanel.add(versionLabel,
                     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                                            GridBagConstraints.HORIZONTAL, new Insets(0, 5, 2, 0), 0, 0));

        Icon icon = UIManager.getIcon("help.logo_help");
        JLabel labelImage = new JLabel(icon);
        labelImage.setMaximumSize(new Dimension(100, 100));
        labelImage.setMinimumSize(new Dimension(100, 100));
        labelImage.setPreferredSize(new Dimension(100, 100));
        labelImage.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(labelImage,
                     new GridBagConstraints(1, 0, 1, 2, 0.0, 1.0, GridBagConstraints.WEST,
                                            GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));

        return topPanel;
    }


    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JScrollPane jScrollPane1 = new JScrollPane();
        Border border =
              BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134));
        jScrollPane1.setBorder(new TitledBorder(border, "Historique des versions"));

        historicText.setCaretColor(Color.black);
        historicText.setBackground(SystemColor.info);
        historicText.setEditable(false);
        historicText.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                hyperlinkUpdate(event);
            }
        });
        jScrollPane1.getViewport().add(historicText, null);
        centerPanel.add(jScrollPane1, BorderLayout.CENTER);

        return centerPanel;
    }
}
