/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.waiting;
import net.codjo.gui.toolkit.util.ErrorDialog;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.log4j.Logger;
/**
 * Paneau d'attente (roue tournante) permettant de lancer un traitement dans un thread séparé.
 *
 * <p> Exemple d'utilisation :
 * <pre>
 *    myWaitingPanel = new WaitingPanel();
 *    myframe.setGlassPane(myWaitingPanel);
 *    ...
 *    myWaitingPanel.exec(new Runnable() {
 *        public void run() {
 *           doSomeStuff();
 *        }
 *    });
 * </pre>
 * </p>
 *
 * @version $Revision: 1.1 $
 */
public class WaitingPanel extends JComponent implements ActionListener {
    private static final Logger LOG = Logger.getLogger(WaitingPanel.class);
    private static final int DEFAULT_DELAY_BEFORE_ANIMATION = 1000;
    private static final double SCALE_SIZE_CEIL = 250.0;

    protected Area[] ticker = null;
    protected boolean started = false;
    protected int alphaLevel = 0;
    protected float shield = 0.70f;
    protected String text = "";
    protected int barsCount = 14;
    protected RenderingHints hints = null;
    private MouseAdapter mouseEventEater = new MouseEventEater();
    private Timer startedTimer;
    private AffineTransform toCircle;
    private Thread currentThread;
    private int delay = 200;
    private JInternalFrame container = null;
    private int delayBeforeAnimation = DEFAULT_DELAY_BEFORE_ANIMATION;
    private int tickCount = 0;
    private final Object lock = new Object();


    public WaitingPanel() {
        this("");
    }


    public WaitingPanel(String text) {
        this(text, 14);
    }


    public WaitingPanel(JInternalFrame container, String text) {
        this(container, text, 14);
    }


    public WaitingPanel(JInternalFrame container, String text, int barsCount) {
        this(text, barsCount);
        this.container = container;
    }


    public WaitingPanel(String text, int barsCount) {
        this(text, barsCount, 0.70f);
    }


    public WaitingPanel(String text, int barsCount, float shield) {
        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                synchronized (lock) {
                    if (started) {
                        setVisible(true);
                    }
                }
            }
        });

        setName("waitingPanel");
        this.text = text;
        this.shield = shield >= 0.0f ? shield : 0.0f;
        this.barsCount = barsCount > 0 ? barsCount : 14;

        hints =
              new RenderingHints(RenderingHints.KEY_RENDERING,
                                 RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                  RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }


    public void setText(String text) {
        repaint();
        this.text = text;
    }


    public String getText() {
        return text;
    }


    public void setDelay(int delay) {
        this.delay = delay;
    }


    public void startAnimation() {
        addMouseListener(mouseEventEater);
        ticker = buildTicker();
        init();
        synchronized (lock) {
            started = true;
        }
        startedTimer = new Timer(delay, this);
        startedTimer.start();
    }


    public void stopAnimation() {
        if (startedTimer != null) {
            startedTimer.stop();
            startedTimer = null;
            synchronized (lock) {
                started = false;
            }
            setVisible(false);
            removeMouseListener(mouseEventEater);
        }
    }


    public synchronized void exec(Runnable action) {
        exec(action, null);
    }


    public synchronized void exec(final Runnable action, final Runnable postAction) {
        LOG.debug("Lancement du traitement avec le WaitingPanel " + getName());
        if (currentThread != null) {
            ErrorDialog.show(this, "Erreur lors de l'exécution du traitement",
                             "tentative d'exécution de plusieurs traitements en parallèle");
            return;
        }

        runInSwingThread(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });

        currentThread =
              new Thread() {
                  @Override
                  public void run() {
                      try {
                          action.run();
                      }
                      catch (Throwable throwable) {
                          ErrorDialog.show(WaitingPanel.this,
                                           "Erreur non récuperée lors du lancement du traitement !",
                                           new Exception(throwable));
                      }
                      finally {
                          runInSwingThread(new StopAnimationRunnable());
                          currentThread = null;
                      }
                      if (postAction != null) {
                          postAction.run();
                      }
                  }
              };

        currentThread.start();

        try {
            Thread.sleep(delayBeforeAnimation);
        }
        catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        if (currentThread != null && currentThread.isAlive()) {
            runInSwingThread(new StartAnimationRunnable());
        }
        else {
            runInSwingThread(new Runnable() {
                public void run() {
                    setVisible(false);
                }
            });
        }
    }


    private void runInSwingThread(Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        }
        else {
            SwingUtilities.invokeLater(runnable);
        }
    }


    @Override
    public void paintComponent(Graphics graphics) {
        synchronized (lock) {
            if (started) {
                int width = getWidth();

                double maxY = 0.0;

                Graphics2D g2 = (Graphics2D)graphics;
                g2.setRenderingHints(hints);

                g2.setColor(new Color(255, 255, 255, (int)(alphaLevel * shield)));
                g2.fillRect(0, 0, getWidth(), getHeight());

                for (int i = 0; i < ticker.length; i++) {
                    int channel = 224 - 128 / (i + 1);
                    g2.setColor(new Color(channel, channel, channel, alphaLevel));
                    g2.fill(ticker[i]);

                    Rectangle2D bounds = ticker[i].getBounds2D();
                    if (bounds.getMaxY() > maxY) {
                        maxY = bounds.getMaxY();
                    }
                }

                if (text != null && text.length() > 0) {
                    FontRenderContext context = g2.getFontRenderContext();
                    TextLayout layout = new TextLayout(text, getFont(), context);
                    Rectangle2D bounds = layout.getBounds();
                    g2.setColor(getForeground());
                    layout.draw(g2, (float)(width - bounds.getWidth()) / 2,
                                (float)(maxY + layout.getLeading() + 2 * layout.getAscent()));
                }
            }
        }
    }


    private double getScaleFactor(double width, double height) {
        double minSize = Math.min(width, height);
        return minSize < SCALE_SIZE_CEIL ? (minSize) / (SCALE_SIZE_CEIL) : 1.0;
    }


    @Override
    public int getWidth() {
        return (container != null) ? container.getWidth() : super.getWidth();
    }


    @Override
    public int getHeight() {
        return (container != null) ? container.getHeight() - 26 : super.getHeight();
    }


    private Area[] buildTicker() {
        Area[] newTicker = new Area[barsCount];
        Point2D.Double center =
              new Point2D.Double((double)getWidth() / 2, (double)getHeight() / 2);
        double fixedAngle = 2.0 * Math.PI / ((double)barsCount);

        for (double i = 0.0; i < (double)barsCount; i++) {
            Area primitive = buildPrimitive();
            double scaleFactor = getScaleFactor(getWidth(), getHeight());

            AffineTransform scale = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
            AffineTransform toCenter = AffineTransform.getTranslateInstance(center.getX(), center.getY());
            AffineTransform toBorder = AffineTransform.getTranslateInstance(45.0 * scaleFactor, 0.0);
            AffineTransform newToCircle =
                  AffineTransform.getRotateInstance(-i * fixedAngle, center.getX(),
                                                    center.getY());

            AffineTransform toWheel = new AffineTransform();
            toWheel.concatenate(toCenter);
            toWheel.concatenate(toBorder);

            primitive.transform(scale);
            primitive.transform(toWheel);
            primitive.transform(newToCircle);

            newTicker[(int)i] = primitive;
        }

        return newTicker;
    }


    private Area buildPrimitive() {
        Rectangle2D.Double body = new Rectangle2D.Double(6, -6, 30, 12);
        Ellipse2D.Double head = new Ellipse2D.Double(0, -6, 12, 12);
        Ellipse2D.Double tail = new Ellipse2D.Double(30, -6, 12, 12);

        Area tick = new Area(body);
        tick.add(new Area(head));
        tick.add(new Area(tail));

        return tick;
    }


    public void actionPerformed(ActionEvent event) {
        for (Area aTicker : ticker) {
            aTicker.transform(toCircle);
        }
        if (hasDelayBeforeAnimation()) {
            alphaLevel = 255;
        }
        else {
            changeAlphaLevel();
        }
        repaint();
    }


    private void init() {
        Point2D.Double center = new Point2D.Double((double)getWidth() / 2, (double)getHeight() / 2);
        double fixedIncrement = 2.0 * Math.PI / ((double)barsCount);
        toCircle = AffineTransform.getRotateInstance(fixedIncrement, center.getX(), center.getY());

        alphaLevel = 0;
        tickCount = 0;
    }


    private void changeAlphaLevel() {
        if (tickCount == 0) {
            alphaLevel = 0;
        }

        if (alphaLevel <= 255) {
            tickCount++;
            alphaLevel = (int)(4 * Math.exp(tickCount) * tickCount * tickCount);
            if (alphaLevel > 255) {
                alphaLevel = 255;
            }
        }
    }


    private boolean hasDelayBeforeAnimation() {
        return delayBeforeAnimation > 0;
    }


    private static class MouseEventEater extends MouseAdapter {
    }

    private class StartAnimationRunnable implements Runnable {
        public void run() {
            startAnimation();
        }
    }

    private class StopAnimationRunnable implements Runnable {
        public void run() {
            stopAnimation();
        }
    }


    public void setDelayBeforeAnimation(int delayBeforeAnimation) {
        this.delayBeforeAnimation = delayBeforeAnimation;
    }


    protected Thread getCurrentThread() {
        return currentThread;
    }


    protected boolean isStarted() {
        return started;
    }
}
