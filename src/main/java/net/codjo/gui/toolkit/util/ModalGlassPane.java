package net.codjo.gui.toolkit.util;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModalGlassPane extends JPanel {
    protected RenderingHints hints = null;


    public ModalGlassPane(final JFrame parentWindow, final JFrame modalWindow) {
        addMouseListener(new MouseEventEater());
        setFocusable(true);
        setOpaque(false);

        new FocusChangeBlocker(parentWindow, modalWindow);
        new Iconifier(parentWindow, modalWindow);
        new WindowClosingBlocker(parentWindow, modalWindow);

        hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Panel avec le label
        JLabel label = new JLabel("<html><body>En attente de " + modalWindow.getTitle() + "...");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.WHITE);

        JPanel textBox = new RoundedPanel();
        textBox.add(label, BorderLayout.CENTER);

        BufferedImage image = GuiUtil.scaleImage(
              GuiUtil.createImage(modalWindow.getContentPane()),
              new Dimension(150, 150));
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(image));
        imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 3, 10, 3));
        imageLabel.setHorizontalAlignment(JLabel.HORIZONTAL);
        textBox.add(imageLabel, BorderLayout.SOUTH);

        // Box au Centre
        setLayout(new GridBagLayout());
        add(textBox, new GridBagConstraints());
    }


    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        g2.setRenderingHints(hints);

        // Background
        g2.setColor(new Color(0, 0, 0, 0.3f));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }


    private static class RoundedPanel extends JPanel {
        RoundedPanel() {
            super(new BorderLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            setBackground(Color.BLACK);
        }


        @Override
        public void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D)graphics;

            g2.setColor(new Color(0, 0, 0, 0.8f));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }
    }

    private static class MouseEventEater extends MouseAdapter {
    }
}
