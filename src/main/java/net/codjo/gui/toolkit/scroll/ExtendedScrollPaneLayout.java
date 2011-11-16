/**
 * Copyright (c) 2007 State Of Mind.
 */
package net.codjo.gui.toolkit.scroll;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.border.Border;

@SuppressWarnings("OverlyComplexClass")
public class ExtendedScrollPaneLayout extends ScrollPaneLayout implements ExtendedScrollPaneConstants {
    protected JViewport columnFooter;
    protected JViewport rowFoot;
    protected Component hLeft;
    protected Component hRight;
    protected Component vTop;
    protected Component vBottom;


    @Override
    public void syncWithScrollPane(JScrollPane sp) {
        super.syncWithScrollPane(sp);
        if (sp instanceof ExtendedScrollPane) {
            ExtendedScrollPane jxScrollPane = (ExtendedScrollPane)sp;

            columnFooter = jxScrollPane.getColumnFooter();
            rowFoot = jxScrollPane.getRowFooter();
            hLeft = jxScrollPane.getScrollBarCorner(HORIZONTAL_LEFT);
            hRight = jxScrollPane.getScrollBarCorner(HORIZONTAL_RIGHT);
            vTop = jxScrollPane.getScrollBarCorner(VERTICAL_TOP);
            vBottom = jxScrollPane.getScrollBarCorner(VERTICAL_BOTTOM);
        }
    }


    @Override
    public void addLayoutComponent(String key, Component component) {
        if (COLUMN_FOOTER.equals(key)) {
            columnFooter = (JViewport)addSingletonComponent(columnFooter, component);
        }
        else if (ROW_FOOTER.equals(key)) {
            rowFoot = (JViewport)addSingletonComponent(rowFoot, component);
        }
        else if (HORIZONTAL_LEFT.equals(key)) {
            hLeft = addSingletonComponent(hLeft, component);
        }
        else if (HORIZONTAL_RIGHT.equals(key)) {
            hRight = addSingletonComponent(hRight, component);
        }
        else if (VERTICAL_TOP.equals(key)) {
            vTop = addSingletonComponent(vTop, component);
        }
        else if (VERTICAL_BOTTOM.equals(key)) {
            vBottom = addSingletonComponent(vBottom, component);
        }
        else {
            super.addLayoutComponent(key, component);
        }
    }


    @Override
    public void removeLayoutComponent(Component component) {
        if (component == columnFooter) {
            columnFooter = null;
        }
        else if (component == rowFoot) {
            rowFoot = null;
        }
        else if (component == hLeft) {
            hLeft = null;
        }
        else if (component == hRight) {
            hRight = null;
        }
        else if (component == vTop) {
            vTop = null;
        }
        else if (component == vBottom) {
            vBottom = null;
        }
        else {
            super.removeLayoutComponent(component);
        }
    }


    public Component getScrollBarCorner(String key) {
        if (HORIZONTAL_LEFT.equals(key)) {
            return hLeft;
        }
        else if (HORIZONTAL_RIGHT.equals(key)) {
            return hRight;
        }
        else if (VERTICAL_BOTTOM.equals(key)) {
            return vBottom;
        }
        else if (VERTICAL_TOP.equals(key)) {
            return vTop;
        }
        else {
            return super.getCorner(key);
        }
    }


    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public Dimension preferredLayoutSize(Container parent) {
        /* Sync the (now obsolete) policy fields with the JScrollPane. */
        JScrollPane scrollPane = (JScrollPane)parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Insets insets = parent.getInsets();
        int prefWidth = insets.left + insets.right;
        int prefHeight = insets.top + insets.bottom;

        /* Note that viewport.getViewSize() is equivalent to
         * viewport.getView().getPreferredSize() modulo a null
         * view or a view whose size was explicitly set.
         */

        Dimension extentSize = null;
        Dimension viewSize = null;
        Component view = null;

        if (viewport != null) {
            extentSize = viewport.getPreferredSize();
            viewSize = viewport.getViewSize();
            view = viewport.getView();
        }

        /* If there's a viewport add its preferredSize.
         */

        if (extentSize != null) {
            prefWidth += extentSize.width;
            prefHeight += extentSize.height;
        }

        /* If there's a JScrollPane.viewportBorder, add its insets.
         */

        Border viewportBorder = scrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets vpbInsets = viewportBorder.getBorderInsets(parent);
            prefWidth += vpbInsets.left + vpbInsets.right;
            prefHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /* If a header exists and it's visible, factor its
         * preferred size in.
         */

        int rowHeaderWidth = 0;
        if (rowHead != null && rowHead.isVisible()) {
            rowHeaderWidth = rowHead.getPreferredSize().width;
        }
        if (upperLeft != null && upperLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, upperLeft.getPreferredSize().width);
        }
        if (lowerLeft != null && lowerLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, lowerLeft.getPreferredSize().width);
        }
        prefWidth += rowHeaderWidth;

        int upperHeight = getUpperHeight();

        prefHeight += upperHeight;

        if ((rowFoot != null) && rowFoot.isVisible()) {
            prefWidth += rowFoot.getPreferredSize().width;
        }

        int lowerHeight = getLowerHeight();
        prefHeight += lowerHeight;

        /* If a scrollbar is going to appear, factor its preferred size in.
         * If the scrollbars policy is AS_NEEDED, this can be a little
         * tricky:
         *
         * - If the view is a Scrollable then scrollableTracksViewportWidth
         * and scrollableTracksViewportHeight can be used to effectively
         * disable scrolling (if they're true) in their respective dimensions.
         *
         * - Assuming that a scrollbar hasn't been disabled by the
         * previous constraint, we need to decide if the scrollbar is going
         * to appear to correctly compute the JScrollPanes preferred size.
         * To do this we compare the preferredSize of the viewport (the
         * extentSize) to the preferredSize of the view.  Although we're
         * not responsible for laying out the view we'll assume that the
         * JViewport will always give it its preferredSize.
         */

        if ((vsb != null) && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
            if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
                prefWidth += vsb.getPreferredSize().width;
            }
            else if ((viewSize != null) && (extentSize != null)) {
                boolean canScroll = true;
                if (view instanceof Scrollable) {
                    canScroll = !((Scrollable)view).getScrollableTracksViewportHeight();
                }
                if (canScroll && (viewSize.height > extentSize.height)) {
                    prefWidth += vsb.getPreferredSize().width;
                }
            }
        }

        if ((hsb != null) && (hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
            if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
                prefHeight += hsb.getPreferredSize().height;
            }
            else if ((viewSize != null) && (extentSize != null)) {
                boolean canScroll = true;
                if (view instanceof Scrollable) {
                    canScroll = !((Scrollable)view).getScrollableTracksViewportWidth();
                }
                if (canScroll && (viewSize.width > extentSize.width)) {
                    prefHeight += hsb.getPreferredSize().height;
                }
            }
        }
        return new Dimension(prefWidth, prefHeight);
    }


    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public Dimension minimumLayoutSize(Container parent) {
        /* Sync the (now obsolete) policy fields with the JScrollPane. */
        JScrollPane scrollPane = (JScrollPane)parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Insets insets = parent.getInsets();
        int minWidth = insets.left + insets.right;
        int minHeight = insets.top + insets.bottom;

        /* If there's a viewport add its minimumSize. */
        if (viewport != null) {
            Dimension size = viewport.getMinimumSize();
            minWidth += size.width;
            minHeight += size.height;
        }

        /* If there's a JScrollPane.viewportBorder, add its insets. */

        Border viewportBorder = scrollPane.getViewportBorder();
        if (viewportBorder != null) {
            Insets vpbInsets = viewportBorder.getBorderInsets(parent);
            minWidth += vpbInsets.left + vpbInsets.right;
            minHeight += vpbInsets.top + vpbInsets.bottom;
        }

        /* If a header exists and it's visible, factor its minimum size in. */

        int rowHeaderWidth = 0;
        if (rowHead != null && rowHead.isVisible()) {
            Dimension size = rowHead.getMinimumSize();
            rowHeaderWidth = size.width;
            minHeight = Math.max(minHeight, size.height);
        }
        if (upperLeft != null && upperLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, upperLeft.getMinimumSize().width);
        }
        if (lowerLeft != null && lowerLeft.isVisible()) {
            rowHeaderWidth = Math.max(rowHeaderWidth, lowerLeft.getMinimumSize().width);
        }
        minWidth += rowHeaderWidth;

        int upperHeight = 0;

        if ((upperLeft != null) && upperLeft.isVisible()) {
            upperHeight = upperLeft.getMinimumSize().height;
        }
        if ((upperRight != null) && upperRight.isVisible()) {
            upperHeight = Math.max(upperRight.getMinimumSize().height, upperHeight);
        }

        if ((colHead != null) && colHead.isVisible()) {
            Dimension size = colHead.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            upperHeight = Math.max(size.height, upperHeight);
        }
        minHeight += upperHeight;

        int lowerHeight = 0;
        if (lowerLeft != null && lowerLeft.isVisible()) {
            lowerHeight = lowerLeft.getMinimumSize().height;
        }
        if ((lowerRight != null) && lowerRight.isVisible()) {
            lowerHeight = Math.max(lowerRight.getMinimumSize().height, lowerHeight);
        }
        if (columnFooter != null && columnFooter.isVisible()) {
            Dimension size = columnFooter.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            lowerHeight = Math.max(lowerHeight, size.height);
        }
        minHeight += lowerHeight;

        if (rowFoot != null && rowFoot.isVisible()) {
            Dimension size = rowFoot.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            minHeight += size.height;
        }

        /* If a scrollbar might appear, factor its minimum size in. */

        if ((vsb != null) && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
            Dimension size = vsb.getMinimumSize();
            minWidth += size.width;
            minHeight = Math.max(minHeight, size.height);
        }

        if ((hsb != null) && (hsbPolicy != HORIZONTAL_SCROLLBAR_NEVER)) {
            Dimension size = hsb.getMinimumSize();
            minWidth = Math.max(minWidth, size.width);
            minHeight += size.height;
        }

        return new Dimension(minWidth, minHeight);
    }


    @Override
    @SuppressWarnings("OverlyComplexMethod,SimplifiableIfStatement,OverlyLongMethod")
    public void layoutContainer(Container parent) {
        /* Sync the (now obsolete) policy fields with the JScrollPane. */
        JScrollPane scrollPane = (JScrollPane)parent;
        vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
        hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();

        Rectangle availR = scrollPane.getBounds();
        availR.x = 0;
        availR.y = 0;

        Insets insets = parent.getInsets();
        availR.x = insets.left;
        availR.y = insets.top;
        availR.width -= insets.left + insets.right;
        availR.height -= insets.top + insets.bottom;

        /* Get the scrollPane's orientation. */
        boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();

        /* If there's a visible column header remove the space it
         * needs from the top of availR.  The column header is treated
         * as if it were fixed height, arbitrary width.
         */

        Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);

        int upperHeight = getUpperHeight();

        if ((colHead != null) && (colHead.isVisible())) {
            int colHeadHeight = Math.min(availR.height, upperHeight);
            colHeadR.height = colHeadHeight;
            availR.y += colHeadHeight;
            availR.height -= colHeadHeight;
        }

        /* If there's a visible row header remove the space it needs
         * from the left or right of availR.  The row header is treated
         * as if it were fixed width, arbitrary height.
         */

        Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);

        if ((rowHead != null) && (rowHead.isVisible())) {
            int rowHeadWidth = rowHead.getPreferredSize().width;
            if (upperLeft != null && upperLeft.isVisible()) {
                rowHeadWidth = Math.max(rowHeadWidth, upperLeft.getPreferredSize().width);
            }
            if (lowerLeft != null && lowerLeft.isVisible()) {
                rowHeadWidth = Math.max(rowHeadWidth, lowerLeft.getPreferredSize().width);
            }
            rowHeadWidth = Math.min(availR.width, rowHeadWidth);

            rowHeadR.width = rowHeadWidth;
            availR.width -= rowHeadWidth;
            //if (leftToRight) {
            rowHeadR.x = availR.x;
            availR.x += rowHeadWidth;
//            }
            //else {
            //    rowHeadR.x = availR.x + availR.width;
            //}
        }

        /* If there's a JScrollPane.viewportBorder, remove the
         * space it occupies for availR.
         */

        Border viewportBorder = scrollPane.getViewportBorder();
        Insets vpbInsets;
        if (viewportBorder != null) {
            vpbInsets = viewportBorder.getBorderInsets(parent);
            availR.x += vpbInsets.left;
            availR.y += vpbInsets.top;
            availR.width -= vpbInsets.left + vpbInsets.right;
            availR.height -= vpbInsets.top + vpbInsets.bottom;
        }
        else {
            vpbInsets = new Insets(0, 0, 0, 0);
        }

        Rectangle rowFootR = new Rectangle(0, 0, 0, 0);
        if ((rowFoot != null) && (rowFoot.isVisible())) {
            int rowFootWidth = Math.min(availR.width, rowFoot.getViewSize().width);
            rowFootR.width = rowFootWidth;
            availR.width -= rowFootWidth;
            rowFootR.x = availR.x + availR.width;
        }

        Rectangle colFootR = new Rectangle(0, availR.y, 0, 0);
        int lowerHeight = getLowerHeight();
        if (columnFooter != null && columnFooter.isVisible()) {
            int colFootHeight = Math.min(availR.height, lowerHeight);
            colFootR.height = colFootHeight;
            availR.height -= colFootHeight;
            colFootR.y = availR.y + availR.height;
        }

        /* At this point availR is the space available for the viewport
        * and scrollbars. rowHeadR is correct except for its height and y
        * and colHeadR is correct except for its width and x.  Once we're
        * through computing the dimensions  of these three parts we can
        * go back and set the dimensions of rowHeadR.height, rowHeadR.y,
        * colHeadR.width, colHeadR.x and the bounds for the corners.
        *
        * We'll decide about putting up scrollbars by comparing the
        * viewport views preferred size with the viewports extent
        * size (generally just its size).  Using the preferredSize is
        * reasonable because layout proceeds top down - so we expect
        * the viewport to be laid out next.  And we assume that the
        * viewports layout manager will give the view it's preferred
        * size.  One exception to this is when the view implements
        * Scrollable and Scrollable.getViewTracksViewport{Width,Height}
        * methods return true.  If the view is tracking the viewports
        * width we don't bother with a horizontal scrollbar, similarly
        * if view.getViewTracksViewport(Height) is true we don't bother
        * with a vertical scrollbar.
        */

        Component view = (viewport != null) ? viewport.getView() : null;
        Dimension viewPrefSize = (view != null) ? view.getPreferredSize() : new Dimension(0, 0);

        Dimension extentSize = (viewport != null) ?
                               viewport.toViewCoordinates(availR.getSize()) :
                               new Dimension(0, 0);

        boolean viewTracksViewportWidth = false;
        boolean viewTracksViewportHeight = false;
        boolean isEmpty = (availR.width < 0 || availR.height < 0);
        Scrollable sv;
        // Don't bother checking the Scrollable methods if there is no room
        // for the viewport, we aren't going to show any scrollbars in this
        // case anyway.
        if (!isEmpty && view instanceof Scrollable) {
            sv = (Scrollable)view;
            viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
            viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
        }
        else {
            sv = null;
        }

        /* If there's a vertical scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A vertical
         * scrollbar is considered to be fixed width, arbitrary height.
         */

        Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);

        boolean vsbNeeded;
        if (isEmpty) {
            vsbNeeded = false;
        }
        else if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
            vsbNeeded = true;
        }
        else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
            vsbNeeded = false;
        }
        else {  // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
            vsbNeeded = !viewTracksViewportHeight && (viewPrefSize.height > extentSize.height);
        }

        if ((vsb != null) && vsbNeeded) {
            adjustComponentsForVSB(true, availR, vsbR, vpbInsets, leftToRight);
            extentSize = viewport.toViewCoordinates(availR.getSize());
        }

        /* If there's a horizontal scrollbar and we need one, allocate
         * space for it (we'll make it visible later). A horizontal
         * scrollbar is considered to be fixed height, arbitrary width.
         */

        Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
        boolean hsbNeeded;
        if (isEmpty) {
            hsbNeeded = false;
        }
        else if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
            hsbNeeded = true;
        }
        else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
            hsbNeeded = false;
        }
        else {  // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
            hsbNeeded = !viewTracksViewportWidth && (viewPrefSize.width > extentSize.width);
        }

        if ((hsb != null) && hsbNeeded) {
            adjustComponentsForHSB(true, availR, hsbR, vpbInsets);

            /* If we added the horizontal scrollbar then we've implicitly
             * reduced  the vertical space available to the viewport.
             * As a consequence we may have to add the vertical scrollbar,
             * if that hasn't been done so already.  Of course we
             * don't bother with any of this if the vsbPolicy is NEVER.
             */
            if ((vsb != null) && !vsbNeeded &&
                (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {

                extentSize = viewport.toViewCoordinates(availR.getSize());
                vsbNeeded = viewPrefSize.height > extentSize.height;

                if (vsbNeeded) {
                    adjustComponentsForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                }
            }
        }

        /* Set the size of the viewport first, and then recheck the Scrollable
         * methods. Some components base their return values for the Scrollable
         * methods on the size of the Viewport, so that if we don't
         * ask after resetting the bounds we may have gotten the wrong
         * answer.
         */

        if (viewport != null) {
            viewport.setBounds(availR);

            if (sv != null) {
                extentSize = viewport.toViewCoordinates(availR.getSize());

                boolean oldHSBNeeded = hsbNeeded;
                boolean oldVSBNeeded = vsbNeeded;
                viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
                viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
                if (vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
                    boolean newVSBNeeded = !viewTracksViewportHeight && (
                          viewPrefSize.height > extentSize.height || (rowHead != null
                                                                      && rowHead.getView() != null
                                                                      && rowHead.getViewSize().height
                                                                         > extentSize.height));
                    if (newVSBNeeded != vsbNeeded) {
                        vsbNeeded = newVSBNeeded;
                        adjustComponentsForVSB(vsbNeeded, availR, vsbR, vpbInsets, leftToRight);
                        extentSize = viewport.toViewCoordinates(availR.getSize());
                    }
                }
                if (hsb != null && hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED) {
                    boolean newHSBbNeeded = !viewTracksViewportWidth && (viewPrefSize.width
                                                                         > extentSize.width);
                    if (newHSBbNeeded != hsbNeeded) {
                        hsbNeeded = newHSBbNeeded;
                        adjustComponentsForHSB(hsbNeeded, availR, hsbR, vpbInsets);
                        if ((vsb != null) && !vsbNeeded && (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
                            extentSize = viewport.toViewCoordinates(availR.getSize());
                            vsbNeeded = viewPrefSize.height > extentSize.height;

                            if (vsbNeeded) {
                                adjustComponentsForVSB(true, availR, vsbR, vpbInsets, leftToRight);
                            }
                            if (rowFoot != null && rowFoot.isVisible()) {
                                vsbR.x += rowFootR.width;
                            }
                        }
                    }
                }
                if (oldHSBNeeded != hsbNeeded ||
                    oldVSBNeeded != vsbNeeded) {
                    viewport.setBounds(availR);
                    // You could argue that we should recheck the
                    // Scrollable methods again until they stop changing,
                    // but they might never stop changing, so we stop here
                    // and don't do any additional checks.
                }
            }
        }

        /* We now have the final size of the viewport: availR.
         * Now fixup the header and scrollbar widths/heights.
         */
        vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
        rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
        rowHeadR.y = availR.y - vpbInsets.top;
        colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
        colHeadR.x = availR.x - vpbInsets.left;

        colFootR.x = availR.x;
        colFootR.y = rowHeadR.y + rowHeadR.height;
        colFootR.width = availR.width;
        rowFootR.x = availR.x + availR.width;
        rowFootR.y = availR.y;
        rowFootR.height = availR.height;

        vsbR.x += rowFootR.width;
        hsbR.y += colFootR.height;

        /* Set the bounds of the remaining components.  The scrollbars
         * are made invisible if they're not needed.
         */

        if (rowHead != null) {
            rowHead.setBounds(rowHeadR);
        }

        if (rowFoot != null) {
            rowFoot.setBounds(rowFootR);
        }

//        int columnFooterHeight = 0;
//        int columnFooterHeight = Math.max(colFootR.height,
//                                          Math.max(
//                                                lowerLeft == null ? 0 : lowerLeft.getPreferredSize().height,
//                                                lowerRight == null ?
//                                                0 :
//                                                lowerRight.getPreferredSize().height));
//        int columnFooterHeight = isColumnFootersHeightUnified(scrollPane) ? Math.max(colFootR.height,
//                Math.max(lowerLeft == null ? 0 : lowerLeft.getPreferredSize().height, lowerRight == null ? 0 : lowerRight.getPreferredSize().height)) : 0;

        if (colHead != null) {
            int height = Math.min(colHeadR.height, colHead.getPreferredSize().height);
            colHead.setBounds(
                  new Rectangle(colHeadR.x, colHeadR.y + colHeadR.height - height, colHeadR.width, height));
        }

        if (columnFooter != null) {
            int height = Math.min(colFootR.height, columnFooter.getPreferredSize().height);
            columnFooter.setBounds(new Rectangle(colFootR.x, colFootR.y, colFootR.width, height));
        }

        if (vsb != null) {
            if (vsbNeeded) {
                vsb.setVisible(true);
                if (vTop == null && vBottom == null) {
                    vsb.setBounds(vsbR);
                }
                else {
                    Rectangle rect = new Rectangle(vsbR);
                    if (vTop != null) {
                        Dimension dim = vTop.getPreferredSize();
                        rect.y += dim.height;
                        rect.height -= dim.height;
                        vTop.setVisible(true);
                        vTop.setBounds(new Rectangle(vsbR.x, vsbR.y, vsbR.width, dim.height));
                    }
                    if (vBottom != null) {
                        Dimension dim = vBottom.getPreferredSize();
                        rect.height -= dim.height;
                        vBottom.setVisible(true);
                        vBottom.setBounds(new Rectangle(vsbR.x, vsbR.y + vsbR.height - dim.height, vsbR.width,
                                                        dim.height));
                    }
                    vsb.setBounds(rect);
                }
            }
            else {
                if (viewPrefSize.height > extentSize.height) {
                    vsb.setVisible(true);
                    vsb.setBounds(new Rectangle(vsbR.x, vsbR.y, 0, vsbR.height));
                }
                else {
                    vsb.setVisible(false);
                }
                if (vTop != null) {
                    vTop.setVisible(false);
                }
                if (vBottom != null) {
                    vBottom.setVisible(false);
                }
            }
        }

        if (hsb != null) {
            if (hsbNeeded) {
                hsb.setVisible(true);
                if (hLeft == null && hRight == null) {
                    hsb.setBounds(hsbR);
                }
                else {
                    Rectangle rect = new Rectangle(hsbR);
                    if (hLeft != null) {
                        Dimension dim = hLeft.getPreferredSize();
                        rect.x += dim.width;
                        rect.width -= dim.width;
                        hLeft.setVisible(true);
                        hLeft.setBounds(new Rectangle(hsbR.x, hsbR.y, dim.width, hsbR.height));
                        hLeft.doLayout();
                    }
                    if (hRight != null) {
                        Dimension dim = hRight.getPreferredSize();
                        rect.width -= dim.width;
                        hRight.setVisible(true);
                        hRight.setBounds(
                              new Rectangle(hsbR.x + hsbR.width - dim.width, hsbR.y, dim.width, hsbR.height));
                    }
                    hsb.setBounds(rect);
                }
            }
            else {
                if (viewPrefSize.width > extentSize.width) {
                    hsb.setVisible(true);
                    hsb.setBounds(new Rectangle(hsbR.x, hsbR.y, hsbR.width, 0));
                }
                else {
                    hsb.setVisible(false);
                }
                if (hLeft != null) {
                    hLeft.setVisible(false);
                }
                if (hRight != null) {
                    hRight.setVisible(false);
                }
            }
        }

        if (lowerLeft != null && lowerLeft.isVisible()) {
            int height = Math.min(lowerLeft.getPreferredSize().height, colFootR.height);
            lowerLeft.setBounds(
                  new Rectangle(rowHeadR.x, colFootR.y != 0 ? colFootR.y : hsbR.y, rowHeadR.width, height));
        }

        if (lowerRight != null && lowerRight.isVisible()) {
            int height = Math.min(lowerRight.getPreferredSize().height, colFootR.height);
            lowerRight.setBounds(new Rectangle(rowFootR.x, colFootR.y != 0 ? colFootR.y : hsbR.y,
                                               rowFootR.width + vsbR.width, height));
        }

        if (upperLeft != null && upperLeft.isVisible()) {
            int height = Math.min(upperLeft.getPreferredSize().height, colHeadR.height);
            upperLeft.setBounds(
                  new Rectangle(rowHeadR.x, colHeadR.y + colHeadR.height - height, rowHeadR.width, height));
        }

        if (upperRight != null && upperRight.isVisible()) {
            int height = Math.min(upperRight.getPreferredSize().height, colHeadR.height);
            upperRight.setBounds(new Rectangle(rowFootR.x, colHeadR.y + colHeadR.height - height,
                                               rowFootR.width + vsbR.width, height));
        }
    }


    public JViewport getColumnFooter() {
        return columnFooter;
    }


    public JViewport getRowFooter() {
        return rowFoot;
    }


    private void adjustComponentsForVSB(boolean wantsVSB,
                                        Rectangle available,
                                        Rectangle vsbR,
                                        Insets vpbInsets,
                                        boolean leftToRight) {
        int oldWidth = vsbR.width;
        if (wantsVSB) {
            int vsbWidth = Math.max(0, Math.min(vsb.getPreferredSize().width, available.width));

            available.width -= vsbWidth;
            vsbR.width = vsbWidth;

            if (leftToRight) {
                vsbR.x = available.x + available.width + vpbInsets.right;
            }
            else {
                vsbR.x = available.x - vpbInsets.left;
                available.x += vsbWidth;
            }
        }
        else {
            available.width += oldWidth;
        }
    }


    private void adjustComponentsForHSB(boolean wantsHSB,
                                        Rectangle available,
                                        Rectangle hsbR,
                                        Insets vpbInsets) {
        int oldHeight = hsbR.height;
        if (wantsHSB) {
            int hsbHeight = Math.max(0, Math.min(available.height, hsb.getPreferredSize().height));

            available.height -= hsbHeight;
            hsbR.y = available.y + available.height + vpbInsets.bottom;
            hsbR.height = hsbHeight;
        }
        else {
            available.height += oldHeight;
        }
    }


    private int getUpperHeight() {
        int upperHeight = 0;
        if (upperLeft != null && upperLeft.isVisible()) {
            upperHeight = upperLeft.getPreferredSize().height;
        }
        if (upperRight != null && upperRight.isVisible()) {
            upperHeight = Math.max(upperRight.getPreferredSize().height, upperHeight);
        }

        if (colHead != null && colHead.isVisible()) {
            upperHeight = Math.max(colHead.getPreferredSize().height, upperHeight);
        }
        return upperHeight;
    }


    private int getLowerHeight() {
        int lowerHeight = 0;
        if ((lowerLeft != null) && lowerLeft.isVisible()) {
            lowerHeight = lowerLeft.getPreferredSize().height;
        }
        if ((lowerRight != null) && lowerRight.isVisible()) {
            lowerHeight = Math.max(lowerHeight, lowerRight.getPreferredSize().height);
        }
        if ((columnFooter != null) && columnFooter.isVisible()) {
            lowerHeight = Math.max(lowerHeight, columnFooter.getViewSize().height);
        }
        return lowerHeight;
    }


    static class UIResource extends ExtendedScrollPaneLayout implements javax.swing.plaf.UIResource {
    }
}

