/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.progressbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import org.apache.log4j.Logger;
/**
 * Barre de progression pour des taches longues. Affiche des balles.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.10 $
 */
public class ProgressBarLabel extends JLabel {
    private static final String IMAGES_FOLDER = "";
    private static final int MIN_ICON_INDEX = 0;
    private static final int MAX_ICON_INDEX = 23;
    private static final String PREFIX_ICON = "ProgressBar16_";
    private static final String SUFFIX_ICON = ".png";
    private static final Logger APP = Logger.getLogger(ProgressBarLabel.class);
    private int delay = 400;
    private int currentIconIdx = MIN_ICON_INDEX;
    private Timer timer = null;
    private String oldLabel = null;
    private Icon[] icons = new Icon[24];

    public ProgressBarLabel() {
        setBorder(new EmptyBorder(0, 6, 0, 0));
        initIcons();
        setHorizontalTextPosition(SwingConstants.LEFT);
    }

    public void setDelay(int delay) {
        this.delay = delay;
        getTimer().setDelay(this.delay);
    }


    public void setText(String text) {
        super.setText(((text == null) ? "" : text.trim()));
    }


    public void start(String label) {
        oldLabel = getText();
        setText(label);
        getTimer().start();
    }


    public void stop() {
        stop(null);
    }


    public void stop(String label) {
        if (label != null) {
            setText(label);
        }
        else {
            setText(oldLabel);
        }

        if (isInitialized()) {
            getTimer().stop();
            currentIconIdx = MIN_ICON_INDEX;
            setIcon(getIcon(MIN_ICON_INDEX));
        }
    }


    private void setIconNotFunText(int idx) {
        setText("Missing resource: '" + IMAGES_FOLDER + PREFIX_ICON + idx + SUFFIX_ICON
            + "'");
    }


    private Icon getIcon(int idx) {
        Icon icon = icons[idx];
        if (icon == null) {
            setIconNotFunText(idx);
        }
        return icons[idx];
    }


    private boolean isInitialized() {
        return timer != null;
    }


    private int getNextIdx() {
        if (currentIconIdx > MAX_ICON_INDEX) {
            currentIconIdx = MIN_ICON_INDEX;
        }
        return currentIconIdx++;
    }


    private Timer getTimer() {
        if (!isInitialized()) {
            timer =
                new javax.swing.Timer(delay,
                    new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            setIcon(getIcon(getNextIdx()));
                        }
                    });
        }
        return timer;
    }


    private void initIcons() {
        for (int i = MIN_ICON_INDEX; i < MAX_ICON_INDEX + 1; i++) {
            try {
                icons[i] =
                    new ImageIcon(ProgressBarLabel.class.getResource(IMAGES_FOLDER
                            + PREFIX_ICON + i + SUFFIX_ICON));
            }
            catch (Exception ex) {
                APP.error("Erreur lors du chargement d'une image.", ex);
                setIconNotFunText(i);
                break;
            }
        }
    }
}
