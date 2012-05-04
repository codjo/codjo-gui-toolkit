/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.wizard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.codjo.gui.toolkit.i18n.InternationalizationUtil;
import net.codjo.gui.toolkit.text.AntialiasedJLabel;
import net.codjo.gui.toolkit.util.GuiUtil;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.InternationalizableContainer;
import net.codjo.i18n.gui.TranslationNotifier;
/**
 * Panneau d'affichage d'un wizard.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 */
public class WizardPanel extends JPanel implements InternationalizableContainer {
    private JButton applyButton = new JButton();
    private ButtonEffect buttonEffect = new ButtonEffect();
    private JPanel buttonPanel = new JPanel();
    private FlowLayout buttonPanelLayout = new FlowLayout();
    private JButton cancelButton = new JButton();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JButton nextButton = new JButton();
    private JButton previousButton = new JButton();
    private JLabel stepLabel = new AntialiasedJLabel();
    private JPanel stepPanel = new JPanel(new BorderLayout());
    private StepPanelUpdater stepPanelUpdater = new StepPanelUpdater();
    private Icon wizardIcon = null;
    private JLabel wizardIconLabel = new JLabel();
    private Wizard wizard;
    private TranslationManager translationManager;
    private TranslationNotifier translationNotifier;


    public WizardPanel() {
        jbInit();
        cancelButton.addMouseListener(buttonEffect);
        cancelButton.setName("WizardPanel.cancelButton");

        applyButton.addMouseListener(buttonEffect);
        applyButton.setName("WizardPanel.applyButton");

        nextButton.addMouseListener(buttonEffect);
        nextButton.setName("WizardPanel.nextButton");

        previousButton.addMouseListener(buttonEffect);
        previousButton.setName("WizardPanel.previousButton");

        previousButton.setBorder(ButtonEffect.newBorder(5, 0, 5, 5));
        nextButton.setBorder(ButtonEffect.newBorder(5, 5, 5, 15));

        applyButton.setBorder(ButtonEffect.newBorder(5, 15, 5, 5));
        cancelButton.setBorder(ButtonEffect.newBorder(5, 5, 5, 5));
    }


    public void addInternationalizableComponents(TranslationNotifier notifier) {
        translationNotifier.addInternationalizableComponent(cancelButton, "WizardPanel.cancelButton", null);
        translationNotifier.addInternationalizableComponent(applyButton,
                                                            wizard.getFinalStep().getName(),
                                                            "WizardPanel.applyButton.tooltip");
        translationNotifier.addInternationalizableComponent(nextButton,
                                                            "WizardPanel.nextButton",
                                                            "WizardPanel.nextButton.tooltip");
        translationNotifier.addInternationalizableComponent(previousButton,
                                                            "WizardPanel.previousButton",
                                                            "WizardPanel.previousButton.tooltip");
    }


    public void setTranslationBackpack(TranslationManager manager, TranslationNotifier notifier) {
        this.translationManager = manager;
        this.translationNotifier = notifier;
    }


    private void translateComponent(JLabel label, String key) {
        if (translationManager != null && translationNotifier != null &&
            translationManager.hasKey(key, translationNotifier.getLanguage())) {
            label.setText(translationManager.translate(key, translationNotifier.getLanguage()));
        }
        else {
            label.setText(key);
        }
    }


    public void setWizard(Wizard wizard) {
        if (this.wizard != null) {
            throw new IllegalStateException("Deja défini");
        }
        this.wizard = wizard;
        previousButton.setAction(new PreviousStepAction(wizard));
        nextButton.setAction(new NextStepAction(wizard));
        applyButton.setAction(new DoFinalStepAction(wizard));
        this.wizard.addPropertyChangeListener(Wizard.CURRENT_STEP_PROPERTY,
                                              stepPanelUpdater);
        this.wizard.addPropertyChangeListener(Wizard.WIZARD_FINISHING, stepPanelUpdater);
        if (wizard.getCurrentStep() == null) {
            wizard.nextStep();
        }

        if (translationManager != null && translationNotifier != null) {
            InternationalizationUtil.registerBundlesIfNeeded(translationManager);
            translationNotifier.addInternationalizableContainer(this);
        }
    }


    public void setWizardIcon(Icon wizardIcon) {
        this.wizardIcon = wizardIcon;
        if (wizardIcon != null) {
            this.setPreferredSize(new Dimension(500, wizardIcon.getIconHeight() + 30));
        }
        wizardIconLabel.setIcon(wizardIcon);
    }


    public JButton getCancelButton() {
        return cancelButton;
    }


    public Wizard getWizard() {
        return wizard;
    }


    public Icon getWizardIcon() {
        return wizardIcon;
    }


    void cancelButtonActionPerformed() {
        if (wizard == null) {
            return;
        }
        if (wizard.isWizardFinished() && !wizard.getFinalStep().isFulfilled()) {
            wizard.getFinalStep().cancel();
        }
    }


    private void jbInit() {
        wizardIconLabel.setRequestFocusEnabled(true);
        this.setLayout(gridBagLayout1);
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setRolloverEnabled(true);
        cancelButton.setText("Fermer");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed();
            }
        });
        applyButton.setEnabled(false);
        applyButton.setBorderPainted(false);
        applyButton.setFocusPainted(false);
        applyButton.setRolloverEnabled(true);
        nextButton.setBorderPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setRolloverEnabled(true);
        previousButton.setEnabled(false);
        previousButton.setBorderPainted(false);
        previousButton.setFocusPainted(false);
        previousButton.setRolloverEnabled(true);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setAlignment(FlowLayout.RIGHT);
        buttonPanelLayout.setHgap(0);
        buttonPanelLayout.setVgap(0);
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        stepLabel.setFont(new java.awt.Font("Serif", 3, 22));
        buttonPanel.add(previousButton, null);
        buttonPanel.add(nextButton, null);
        buttonPanel.add(applyButton, null);
        buttonPanel.add(cancelButton, null);
        this.add(stepLabel,
                 new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 0), 0, 0));
        this.add(stepPanel,
                 new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                                        GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
        this.add(buttonPanel,
                 new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(wizardIconLabel,
                 new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }


    private static class ButtonEffect extends MouseAdapter {
        public static Border newBorder(int top, int left, int bottom, int right) {
            return BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(
                  top,
                  left,
                  bottom,
                  right), BorderFactory.createEtchedBorder());
        }


        @Override
        public void mouseEntered(MouseEvent event) {
            if (!((JButton)event.getSource()).isEnabled()) {
                return;
            }
            ((JButton)event.getSource()).setBorderPainted(true);
            ((JButton)event.getSource()).setForeground(Color.blue);
        }


        @Override
        public void mouseExited(MouseEvent mouse) {
            ((JButton)mouse.getSource()).setBorderPainted(false);
            ((JButton)mouse.getSource()).setForeground(Color.black);
        }
    }

    private class StepPanelUpdater implements PropertyChangeListener {
        public void propertyChange(final PropertyChangeEvent evt) {
            GuiUtil.runInSwingThread(new Runnable() {
                public void run() {
                    final Step step;
                    if (Wizard.WIZARD_FINISHING.equals(evt.getPropertyName())) {
                        step = wizard.getFinalStep();
                    }
                    else {
                        step = wizard.getCurrentStep();
                    }

                    repaintStep(step);
                }
            });
        }
    }


    public void repaintStep(Step step) {
        stepPanel.removeAll();
        if (step != null) {
            translateComponent(stepLabel, step.getName());
            stepPanel.add(step.getGui(), BorderLayout.CENTER);
        }
        stepPanel.validate();
        stepPanel.repaint();
    }


    public void addStepStatePropertyListenerToApplyButton() {
        ((DoFinalStepAction)applyButton.getAction()).addStepStatePropertyListener();
    }
}
