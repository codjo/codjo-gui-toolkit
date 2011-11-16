package net.codjo.gui.toolkit.util;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JViewport;
import javax.swing.OverlayLayout;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

public class FeedbackPanel extends JComponent {
    private static final Logger LOG = Logger.getLogger(FeedbackPanel.class);
    private Set<JComponent> invalidFields = new HashSet<JComponent>();
    private BufferedImage warningIcon;
    private Map<JComponent, ComponentListener> fieldToListener = new HashMap<JComponent, ComponentListener>();
    private JLayeredPane layeredPane;
    private String tooltip;


    public FeedbackPanel(final JComponent jComponent) {
        this(jComponent, FeedbackPanel.class.getResource("bullet_error.png"), "Champ obligatoire");
    }


    public FeedbackPanel(final JComponent jComponent, URL iconURL, String tooltip) {
        this.tooltip = tooltip;
        loadImages(iconURL);
        jComponent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                installLayeredPane(jComponent);
            }


            @Override
            public void componentHidden(ComponentEvent e) {
                if (layeredPane != null) {
                    layeredPane.remove(FeedbackPanel.this);
                }
            }
        });

        jComponent.addHierarchyListener(new HierarchyListener() {
            public void hierarchyChanged(HierarchyEvent e) {
                if ((layeredPane == null) && (jComponent.isValid())) {
                    installLayeredPane(jComponent);
                }
            }
        });
    }


    public void removeIcon(JComponent field) {
        if (invalidFields.contains(field)) {
            invalidFields.remove(field);
            field.setToolTipText(null);
            field.removeComponentListener(fieldToListener.get(field));
            fieldToListener.remove(field);
        }
    }


    public void addIcon(final JComponent field) {
        invalidFields.add(field);
        field.setToolTipText(tooltip);
        ComponentAdapter repainter = new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                repaintIcon(field);
            }
        };
        field.addComponentListener(repainter);
        fieldToListener.put(field, repainter);
    }


    public void removeAllIcons() {
        Object[] fields = invalidFields.toArray();
        for (Object field : fields) {
            removeIcon((JComponent)field);
        }
    }


    public void repaintAll() {
        for (JComponent invalidField : invalidFields) {
            repaintIcon(invalidField);
        }
    }


    public void repaintIcon(JComponent field) {
        Point point = field.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(point, this);

        int posX = point.x - warningIcon.getWidth() / 2;
        int posY = (int)(point.y + field.getHeight() - warningIcon.getHeight() / 1.5);

        repaint(posX, posY, warningIcon.getWidth(), warningIcon.getHeight());
    }


    private void installLayeredPane(JComponent jComponent) {
        layeredPane = jComponent.getRootPane().getLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        layeredPane.add(this, (Integer)(JLayeredPane.DEFAULT_LAYER + 50));
        this.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.validate();
    }


    private void loadImages(URL iconURL) {
        try {
            warningIcon = ImageIO.read(iconURL);
        }
        catch (IOException e) {
            LOG.error("Impossible de charger l'image de validation", e);
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        for (JComponent invalid : invalidFields) {
            if (invalid.getParent() instanceof JViewport) {
                JViewport viewport = (JViewport)invalid.getParent();
                invalid = (JComponent)viewport.getParent();
            }

            if (invalid.isShowing()) {
                Point point = invalid.getLocationOnScreen();
                SwingUtilities.convertPointFromScreen(point, this);

                int posX = point.x - warningIcon.getWidth() / 2;
                int posY = (int)(point.y + invalid.getHeight() - warningIcon.getHeight() / 1.5);

                if (g.getClipBounds().intersects(posX, posY,
                                                 warningIcon.getWidth(), warningIcon.getHeight())) {
                    g.drawImage(warningIcon, posX, posY, null);
                }
            }
        }
    }
}
