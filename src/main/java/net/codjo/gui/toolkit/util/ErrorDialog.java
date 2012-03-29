/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import net.codjo.gui.toolkit.i18n.InternationalizationUtil;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.TranslationNotifier;
/**
 * Classe permettant d'afficher un message d'erreur à l'utilisateur.
 *
 * <p> Le fonctionnement est proche de celui décrit par JOptionPane. </p>
 *
 * <p> Exemple d'utilisation :
 * <pre>
 *  ErrorDialog.show(aframe, "Erreur de connexion", "le serveur est mort");</pre>
 * ou
 * <pre>
 *  ErrorDialog.show(aframe, "Erreur de connexion", monException);</pre>
 * </p>
 *
 * @author $Author: gaudefr $
 * @see javax.swing.JOptionPane
 */
public final class ErrorDialog {
    private static TranslationManager translationManager;
    private static TranslationNotifier translationNotifier;


    /**
     * Constructeur.
     *
     * @param aFrame               Composant GUI parent
     * @param message              Message titre
     * @param exceptionMsg         Message de l'exception
     * @param exceptionDescription Message decrivant l'exception
     */
    private ErrorDialog(Component aFrame, String message, String exceptionMsg, String exceptionDescription) {
        checkTranslationBackpack();
        JTextArea textArea = buildTextArea(exceptionMsg);
        JTextArea detailArea = buildErrorDetail(exceptionDescription);
        detailArea.setLineWrap(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(0, 0, 0, 20));
        JTabbedPane tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
        tabPane.addTab("Message", new JScrollPane(textArea));
        tabPane.addTab("Détail", new JScrollPane(detailArea));
        panel.add(tabPane, BorderLayout.CENTER);
        Object[] array = {message, panel};
        Object[] options = {"OK"};

        JOptionPane optionPane = new JOptionPane(array, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION,
                                                 null, options, options[0]);

        JDialog dialog = optionPane.createDialog(aFrame, "Erreur");
        dialog.setResizable(true);

        InternationalizationUtil.registerBundlesIfNeeded(translationManager);
        translationNotifier.addInternationalizableComponent(dialog, "ErrorDialog.title");
        translationNotifier.addInternationalizableComponent(tabPane, null, new String[]{
              "ErrorDialog.messageTab.label", "ErrorDialog.detailTab.label"
        });

        dialog.setVisible(true);
    }


    private void checkTranslationBackpack() {
        if ((translationManager == null) || (translationNotifier == null)) {
            throw new IllegalArgumentException(
                  "Cannot launch internationalization. "
                  + "TranslationNotifier and TranslationManager "
                  + " must be set upon ErrorDialog class.");
        }
    }


    private JTextArea buildErrorDetail(String exceptionDescription) {
        JTextArea detailArea;
        if (exceptionDescription != null) {
            detailArea = buildTextArea(exceptionDescription);
        }
        else {
            detailArea = buildTextArea("");
        }
        return detailArea;
    }


    private static String buildStackTrace(Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }


    /**
     * Création d'un dialogue ErrorDialog.
     *
     * <p> Le dialogue crée est affiché. Le thread courant est bloqué tant que l'utilisateur ne clique pas sur OK. <br>
     * La methode peutê être appelée par le thread évènement. </p>
     *
     * @param aFrame       Composant GUI parent
     * @param message      Message titre
     * @param exceptionMsg Message decrivant l'exception
     */
    public static void show(Component aFrame, String message, String exceptionMsg) {
        new ErrorDialog(aFrame, message, exceptionMsg, null);
    }


    /**
     * Création d'un dialogue ErrorDialog à partir d'une exception.
     *
     * <p> Le dialogue crée est affiché. Le thread courant est bloqué tant que l'utilisateur ne clique pas sur OK. <br>
     * La methode peut être appelée par le thread évènement. </p>
     *
     * @param aFrame  Composant GUI parent
     * @param message Message titre
     * @param error   L'exception de l'erreur
     */
    public static void show(Component aFrame, String message, Throwable error) {
        String msg = "";
        if (error.getLocalizedMessage() != null) {
            msg = error.getLocalizedMessage();
        }
        new ErrorDialog(aFrame, message, msg, buildStackTrace(error));
    }


    /**
     * Création d'un dialogue ErrorDialog à partir d'une exception.
     *
     * <p> Le dialogue crée est affiché. Le thread courant est bloqué tant que l'utilisateur ne clique pas sur OK. <br>
     * La methode peut être appelée par le thread évènement. </p>
     *
     * @param aFrame           Composant GUI parent
     * @param message          Message titre
     * @param errorMessage     Message de l'erreur
     * @param errorDescription Description de l'erreur
     */
    public static void show(Component aFrame, String message, String errorMessage, String errorDescription) {
        new ErrorDialog(aFrame, message, errorMessage, errorDescription);
    }


    /**
     * Création d'un dialogue ErrorDialog à partir d'une exception.
     *
     * <p> Le dialogue crée est affiché. Le thread courant est bloqué tant que l'utilisateur ne clique pas sur OK. <br>
     * La methode peut être appelé par le thread évênement. </p>
     *
     * @param aFrame       Composant GUI parent
     * @param message      Message titre
     * @param errorMessage Message de l'erreur
     * @param error        L'exception de l'erreur
     */
    public static void show(Component aFrame, String message, String errorMessage, Throwable error) {
        new ErrorDialog(aFrame, message, errorMessage, buildStackTrace(error));
    }


    public static void setTranslationBackpack(TranslationManager translationManager,
                                              TranslationNotifier translationNotifier) {
        ErrorDialog.translationManager = translationManager;
        ErrorDialog.translationNotifier = translationNotifier;
    }


    private JTextArea buildTextArea(String exceptionMsg) {
        JTextArea textArea = new JTextArea(exceptionMsg, 20, 75);
        textArea.setName("errorMessage");
        textArea.setEnabled(true);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(new Color(205, 205, 205));
        textArea.setDisabledTextColor(Color.black);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return textArea;
    }
}
