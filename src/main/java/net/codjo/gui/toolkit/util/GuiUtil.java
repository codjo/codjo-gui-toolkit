/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.util;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
/**
 * Ensemble de méthodes utilitaires pour l'IHM.
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.14 $
 */
public final class GuiUtil {
    public static final Font DEFAULT_FONT = new JLabel().getFont();
    public static final Font BOLD_FONT = DEFAULT_FONT.deriveFont(Font.BOLD);

    public static final Color DISABLED_BACKGROUND_COLOR = new JPanel().getBackground();
    public static final Color DISABLED_FOREGROUND_COLOR = new Color(110, 110, 110);
    public static final Color DEFAULT_BLACK_COLOR = new JLabel().getForeground();


    /**
     * Bloque la creation d'instances de GuiUtil.
     */
    private GuiUtil() {
    }


    /**
     * Centre une fenetre dans son <code>Container</code> .
     *
     * <p> ATTENTION: Pour les fenetres {@link JInternalFrame} , cette methode doit etre appelee apres l'ajout dans le
     * desktop. (en general dans l'action qui fabrique le {@link JInternalFrame} ). </p>
     *
     * @param cp La fenetre a centrer
     *
     * @throws IllegalArgumentException si <code>cp</code> est null
     * @throws IllegalStateException    si <code>cp</code> est une {@link JInternalFrame} et que cette méthode est
     *                                  appelée avant l'ajout au {@link javax.swing.JDesktopPane}
     */
    public static void centerWindow(Component cp) {
        if (cp == null) {
            throw new IllegalArgumentException();
        }

        Dimension containerSize;

        if (cp instanceof JInternalFrame) {
            if (cp.getParent() == null) {
                throw new IllegalStateException("L'appel a la methode 'centerWindow'"
                                                + " doit s'effectuer apres l'ajout au desktop");
            }
            containerSize = cp.getParent().getSize();
        }
        else {
            containerSize = Toolkit.getDefaultToolkit().getScreenSize();
        }

        Dimension frameSize = cp.getSize();

        if (frameSize.height > containerSize.height) {
            frameSize.height = containerSize.height;
            cp.setSize(frameSize);
        }
        if (frameSize.width > containerSize.width) {
            frameSize.width = containerSize.width;
            cp.setSize(frameSize);
        }

        cp.setLocation((containerSize.width - frameSize.width) / 2,
                       (containerSize.height - frameSize.height) / 2);
    }


    public static JPanel createHorizontalButtonPanel(JButton[] buttons) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        for (JButton button : buttons) {
            buttonPanel.add(button);
            buttonPanel.add(Box.createHorizontalStrut(4));
        }
        return buttonPanel;
    }


    public static void showDialog(JDialog dialog) {
        showDialog(dialog, true);
    }


    public static void showDialog(JDialog dialog, boolean pack) {
        if (pack) {
            dialog.pack();
        }
        Window owner = dialog.getOwner();
        int xPosition = (owner.getWidth() / 2) - dialog.getWidth() / 2;
        int yPosition = (owner.getHeight() / 2) - dialog.getHeight() / 2;
        dialog.setLocation(xPosition, yPosition);
        dialog.setVisible(true);
    }


    public static void setTextComponentEditable(JTextComponent textComponent, boolean editable) {
        textComponent.setEditable(editable);
        textComponent.setBackground(editable ? Color.white : null);
        textComponent.setEnabled(editable);
    }


    public static void disableTextEdition(JTextComponent textComponent) {
        textComponent.setEditable(false);
        textComponent.setBackground(null);
    }


    public static void addComponent(GridBagConstraints gbc, GridBagLayout gridBag,
                                    int gridx, int gridy, double weight, int fill, int width, int anchor,
                                    Insets insets, JPanel panel, JComponent component) {
        gbc.gridwidth = width;
        gbc.anchor = anchor;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weight;
        gbc.fill = fill;
        gbc.insets = insets;
        gridBag.setConstraints(component, gbc);
        panel.add(component);
    }


    public static void runInSwingThread(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        }
        else {
            SwingUtilities.invokeLater(runnable);
        }
    }


    public static BufferedImage createImage(Component component) {
        if (component instanceof Container) {
            Container container = (Container)component;
            boolean mockScreen =
                  container.getComponentCount() > 0 & !component.isDisplayable();

            if (mockScreen) {
                if (component.getParent() == null) {
                    new JPanel().add(component);
                }
                component.addNotify();
                container.getLayout().layoutContainer(container);
            }
        }

        Dimension dimension = component.getPreferredSize();
        component.setSize(dimension);
        return createImage(component, dimension);
    }


    public static BufferedImage scaleImage(Image image, Dimension target) {
        double scaleX = target.getWidth() / image.getWidth(null);
        double scaleY = target.getHeight() / image.getHeight(null);
        double scale = Math.min(scaleX, scaleY);

        BufferedImage scaled = new BufferedImage((int)Math.ceil(image.getWidth(null) * scale),
                                                 (int)Math.ceil(image.getHeight(null) * scale),
                                                 BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = scaled.createGraphics();
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                                                  RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics.setRenderingHints(hints);

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.scale(scale, scale);
        graphics.drawImage(image, affineTransform, null);
        graphics.dispose();

        return scaled;
    }


    public static void iconify(Frame frame) {
        int state = frame.getExtendedState();

        // Set the iconified bit
        state |= Frame.ICONIFIED;

        // Iconify the frame
        frame.setExtendedState(state);
    }


    public static void deiconify(Frame frame) {
        int state = frame.getExtendedState();

        // Clear the iconified bit
        state &= ~Frame.ICONIFIED;

        // Deiconify the frame
        frame.setExtendedState(state);
    }


    public static String computeFormatPattern(int decimalLength, DecimalFormatEnum decimalFormatEnum) {
        StringBuilder format = new StringBuilder("#,##0");
        if (decimalLength > 0) {
            format.append(".");
            for (int i = 0; i < decimalLength; i++) {
                format.append(decimalFormatEnum.getFormatSymbol());
            }
        }
        return format.toString();
    }


    private static BufferedImage createImage(Component component, Dimension dimension) {
        Rectangle region = new Rectangle(0, 0, dimension.width, dimension.height);

        BufferedImage image =
              new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setClip(region);
        component.paint(graphics);
        graphics.dispose();
        return image;
    }
}
