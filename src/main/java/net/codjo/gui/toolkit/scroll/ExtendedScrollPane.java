/**
 * Copyright (c) 2007 State Of Mind.
 */

package net.codjo.gui.toolkit.scroll;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ExtendedScrollPane extends JScrollPane implements ExtendedScrollPaneConstants {
    protected JViewport columnFooter;
    protected JViewport rowFooter;
    protected Component hLeft;
    protected Component hRight;
    protected Component vTop;
    protected Component vBottom;
    private final static String CLIENT_PROPERTY_SYNCHRONIZE_VIEW = "synchronizeViewChangeListener";


    public ExtendedScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        setLayout(createScrollPaneLayoutUIResource());
        setVerticalScrollBarPolicy(vsbPolicy);
        setHorizontalScrollBarPolicy(hsbPolicy);
        setViewport(createViewport());
        setVerticalScrollBar(createVerticalScrollBar());
        setHorizontalScrollBar(createHorizontalScrollBar());
        if (view != null) {
            setViewportView(view);
        }
        setOpaque(true);
        updateUI();

        if (!getComponentOrientation().isLeftToRight()) {
            viewport.setViewPosition(new Point(Integer.MAX_VALUE, 0));
        }
    }


    public ExtendedScrollPane(Component view) {
        this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }


    public ExtendedScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }


    public ExtendedScrollPane() {
        this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }


    @Override
    public void updateUI() {
        super.updateUI();
        setLayout(createScrollPaneLayoutUIResource());
        LookAndFeel.installBorder(this, "SOScrollPane.border");
    }


    protected ExtendedScrollPaneLayout.UIResource createScrollPaneLayoutUIResource() {
        return new ExtendedScrollPaneLayout.UIResource();
    }


    public JViewport getColumnFooter() {
        return columnFooter;
    }


    public void setColumnFooter(JViewport columnFooter) {
        JViewport old = getColumnFooter();
        this.columnFooter = columnFooter;
        if (columnFooter != null) {
            add(columnFooter, COLUMN_FOOTER);
        }
        else if (old != null) {
            remove(old);
        }
        firePropertyChange("columnFooter", old, columnFooter);
        revalidate();
        repaint();

        synchronizeView(columnFooter, getViewport(), SwingConstants.HORIZONTAL);
        synchronizeView(getViewport(), columnFooter, SwingConstants.HORIZONTAL);
    }


    public void setColumnFooterView(Component view) {
        if (null == getColumnFooter()) {
            setColumnFooter(createViewport());
        }
        getColumnFooter().setView(view);
    }


    @Override
    public void setRowHeaderView(Component view) {
        super.setRowHeaderView(view);

        getRowHeader().setPreferredSize(view.getPreferredSize());
    }


    public JViewport getRowFooter() {
        return rowFooter;
    }


    public void setRowFooter(JViewport rowFooter) {
        JViewport old = getRowFooter();
        this.rowFooter = rowFooter;
        if (rowFooter != null) {
            add(rowFooter, ROW_FOOTER);
        }
        else if (old != null) {
            remove(old);
        }
        firePropertyChange("rowFooter", old, rowFooter);
        revalidate();
        repaint();

        synchronizeView(rowFooter, getViewport(), SwingConstants.VERTICAL);
        synchronizeView(getViewport(), rowFooter, SwingConstants.VERTICAL);
    }


    private void synchronizeView(final JViewport masterViewport,
                                 final JViewport slaveViewport,
                                 final int orientation) {
        final ChangeListener c1 = new SyncViewChangeListener(masterViewport, slaveViewport, orientation);

        masterViewport.addChangeListener(c1);
        masterViewport.putClientProperty(CLIENT_PROPERTY_SYNCHRONIZE_VIEW, c1);
    }


    public void setRowFooterView(Component view) {
        if (null == getRowFooter()) {
            setRowFooter(createViewport());
        }
        getRowFooter().setView(view);
    }


    public Component getScrollBarCorner(String key) {
        boolean isLeftToRight = getComponentOrientation().isLeftToRight();
        if (key.equals(HORIZONTAL_LEADING)) {
            key = isLeftToRight ? HORIZONTAL_LEFT : HORIZONTAL_RIGHT;
        }
        else if (key.equals(HORIZONTAL_TRAILING)) {
            key = isLeftToRight ? HORIZONTAL_RIGHT : HORIZONTAL_LEFT;
        }

        if (key.equals(HORIZONTAL_LEFT)) {
            return hLeft;
        }
        else if (key.equals(HORIZONTAL_RIGHT)) {
            return hRight;
        }
        else if (key.equals(VERTICAL_BOTTOM)) {
            return vBottom;
        }
        else if (key.equals(VERTICAL_TOP)) {
            return vTop;
        }
        else {
            return null;
        }
    }


    public void setScrollBarCorner(String key, Component corner) {
        Component old;
        boolean isLeftToRight = getComponentOrientation().isLeftToRight();
        if (HORIZONTAL_LEADING.equals(key)) {
            key = isLeftToRight ? HORIZONTAL_LEFT : HORIZONTAL_RIGHT;
        }
        else if (HORIZONTAL_TRAILING.equals(key)) {
            key = isLeftToRight ? HORIZONTAL_RIGHT : HORIZONTAL_LEFT;
        }

        if (HORIZONTAL_LEFT.equals(key)) {
            old = hLeft;
            hLeft = corner;
        }
        else if (key.equals(HORIZONTAL_RIGHT)) {
            old = hRight;
            hRight = corner;
        }
        else if (key.equals(VERTICAL_TOP)) {
            old = vTop;
            vTop = corner;
        }
        else if (key.equals(VERTICAL_BOTTOM)) {
            old = vBottom;
            vBottom = corner;
        }
        else {
            throw new IllegalArgumentException("invalid scroll bar corner key '" + key + "'");
        }

        if (old != null) {
            remove(old);
        }
        if (corner != null) {
            add(corner, key);
            corner.setComponentOrientation(getComponentOrientation());
        }
        firePropertyChange(key, old, corner);
        revalidate();
        repaint();
    }


    private static class SyncViewChangeListener implements ChangeListener {
        private final JViewport masterViewport;
        private final JViewport slaveViewport;
        private final int orientation;


        private SyncViewChangeListener(JViewport masterViewport, JViewport slaveViewport, int orientation) {
            this.masterViewport = masterViewport;
            this.slaveViewport = slaveViewport;
            this.orientation = orientation;
        }


        public void stateChanged(ChangeEvent event) {
            Dimension size = masterViewport.getSize();
            if (size.width == 0 || size.height == 0) {
                return;
            }
            Object c2 = slaveViewport.getClientProperty(CLIENT_PROPERTY_SYNCHRONIZE_VIEW);
            try {
                if (c2 instanceof ChangeListener) {
                    slaveViewport.removeChangeListener((ChangeListener)c2);
                }
                if (orientation == SwingConstants.HORIZONTAL) {
                    Point v1 = masterViewport.getViewPosition();
                    Point v2 = slaveViewport.getViewPosition();
                    if (v1.x != v2.x) {
                        slaveViewport.setViewPosition(new Point(v1.x, v2.y));
                    }
                }
                else if (orientation == SwingConstants.VERTICAL) {
                    Point v1 = masterViewport.getViewPosition();
                    Point v2 = slaveViewport.getViewPosition();
                    if (v1.y != v2.y) {
                        slaveViewport.setViewPosition(new Point(v2.x, v1.y));
                    }
                }
            }
            finally {
                if (c2 instanceof ChangeListener) {
                    slaveViewport.addChangeListener((ChangeListener)c2);
                }
            }
        }
    }
}


