/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.util;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
/**
 * Classe utilitaire permettant de simuler le mode Modal pour une JInternalFrame.
 */
public class Modal {
    private JPanel glassPanel = new JPanel();
    private ModalKeyFeedback keyFeedback = new ModalKeyFeedback();
    private JInternalFrame modalFrame;
    private ModalMouseFeedback mouseFeedback = new ModalMouseFeedback();
    private Component oldGlassPane = null;
    private JInternalFrame parentFrame;
    private ParentVetoListener parentVeto = new ParentVetoListener();
    private boolean parentWasClosable;


    /**
     * Constructeur de Modal.
     *
     * @param parentFrame Fenetre parente a la fenetre modal
     * @param modalFrame  La fenetre modal
     *
     * @deprecated Utiliser la methode static applyModality.<
     */
    @Deprecated
    public Modal(JInternalFrame parentFrame, JInternalFrame modalFrame) {
        this.parentFrame = parentFrame;
        this.modalFrame = modalFrame;
        modalFrame.addInternalFrameListener(new ModalStateUpdater());
        if (modalFrame.isShowing()) {
            addModalRestriction();
        }

        glassPanel.setOpaque(false);
        glassPanel.addKeyListener(keyFeedback);
        glassPanel.addMouseListener(mouseFeedback);
    }


    public static void applyModality(JInternalFrame parentFrame, JInternalFrame modalFrame) {
        new Modal(parentFrame, modalFrame);
    }


    public static void applyModality(final JFrame parentFrame, final JFrame modalFrame) {
        modalFrame.toFront();
        final Component previousGlassPane = parentFrame.getGlassPane();
        parentFrame.setGlassPane(new ModalGlassPane(parentFrame, modalFrame));
        parentFrame.getGlassPane().setVisible(true);
        parentFrame.invalidate();
        parentFrame.repaint();

        modalFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                removeModalStuff(parentFrame, previousGlassPane);
                modalFrame.removeWindowListener(this);
            }
        });
    }


    /**
     * Ajoute une restriction Modal.
     */
    private void addModalRestriction() {
        oldGlassPane = parentFrame.getGlassPane();
        parentFrame.setGlassPane(glassPanel);
        parentFrame.getGlassPane().setVisible(true);
        parentFrame.invalidate();
        parentFrame.repaint();

        parentFrame.addVetoableChangeListener(parentVeto);
        modalFrame.setLayer(JLayeredPane.MODAL_LAYER);
        parentWasClosable = parentFrame.isClosable();
        parentFrame.setClosable(false);
    }


    /**
     * Enleve la restriction modal.
     */
    private void removeModalRestriction() {
        parentFrame.getGlassPane().setVisible(false);
        parentFrame.remove(glassPanel);
        parentFrame.setGlassPane(oldGlassPane);
        parentFrame.removeVetoableChangeListener(parentVeto);
        parentFrame.setClosable(parentWasClosable);

        parentFrame.invalidate();
        parentFrame.repaint();
    }


    private static void removeModalStuff(JFrame listWindow, Component glassPane) {
        listWindow.getGlassPane().setVisible(false);
        listWindow.setGlassPane(glassPane);
        listWindow.invalidate();
        listWindow.repaint();
    }


    /**
     * Realise un retour auditif de l'echec d'un evt clavier sur la fenetre parente.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    private static class ModalKeyFeedback extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent evt) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Realise un retour auditif de l'echec d'un click sur la fenetre parente.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    private static class ModalMouseFeedback extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent evt) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Listener mettant a jours la modalite en fonction de l'etat d'ouverture de la fenetre modal.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    private class ModalStateUpdater extends InternalFrameAdapter {
        @Override
        public void internalFrameClosed(InternalFrameEvent evt) {
            removeModalRestriction();
            try {
                parentFrame.setSelected(true);
            }
            catch (PropertyVetoException veto) {
                ; // Tant pis
            }
        }


        @Override
        public void internalFrameClosing(InternalFrameEvent event) {
            internalFrameClosed(event);
        }


        @Override
        public void internalFrameOpened(InternalFrameEvent event) {
            addModalRestriction();
        }
    }

    /**
     * Empeche la fenetre parente d'etre selectionner.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    private class ParentVetoListener implements VetoableChangeListener {
        public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException {
            if ("selected".equals(evt.getPropertyName()) && Boolean.TRUE.equals(evt.getNewValue())) {
                modalFrame.setSelected(true);
                throw new PropertyVetoException("En cours d'edition", evt);
            }
            if ("icon".equals(evt.getPropertyName())) {
                modalFrame.setVisible(Boolean.FALSE.equals(evt.getNewValue()));
            }
        }
    }
}
