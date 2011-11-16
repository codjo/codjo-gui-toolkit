package net.codjo.gui.toolkit.combo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
/**
 *
 */
public class ComboBoxPopupWidthMaximizer {
    private final JComboBox comboBox;
    private final int comboPreferredWidth;
    private final ItemSizeGetter itemSizeGetter;

    private boolean popupResizeNeeded = true;


    private ComboBoxPopupWidthMaximizer(JComboBox comboBox,
                                        int comboPreferredWidth,
                                        ItemSizeGetter itemSizeGetter) {
        this.comboBox = comboBox;
        this.comboPreferredWidth = comboPreferredWidth;
        this.itemSizeGetter = itemSizeGetter;

        init();
    }


    public static void install(JComboBox comboBox) {
        install(comboBox, -1);
    }


    public static void install(JComboBox comboBox, int comboPreferredWidth) {
        install(comboBox, comboPreferredWidth, new DefaultItemSizeGetter(comboBox));
    }


    public static void install(JComboBox comboBox, ItemSizeGetter itemSizeGetter) {
        install(comboBox, -1, itemSizeGetter);
    }


    public static void install(JComboBox comboBox, int comboPreferredWidth, ItemSizeGetter itemSizeGetter) {
        new ComboBoxPopupWidthMaximizer(comboBox, comboPreferredWidth, itemSizeGetter);
    }


    private void init() {
        if (comboPreferredWidth != -1) {
            comboBox.setPreferredSize(new Dimension(comboPreferredWidth, comboBox.getPreferredSize().height));
        }
        comboBox.getModel().addListDataListener(new MyListDataListener());
        comboBox.addPopupMenuListener(new MyPopupMenuListener());
    }


    public interface ItemSizeGetter {
        Dimension getPreferredSizeForItems();
    }

    private class MyListDataListener implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            popupResizeNeeded = true;
        }


        public void intervalAdded(ListDataEvent e) {
        }


        public void intervalRemoved(ListDataEvent e) {
        }
    }

    private class MyPopupMenuListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            if (!popupResizeNeeded) {
                return;
            }

            BasicComboPopup popup = (BasicComboPopup)comboBox.getUI().getAccessibleChild(comboBox, 0);
            if (popup == null) {
                return;
            }

            Dimension preferredSizeForItem = itemSizeGetter.getPreferredSizeForItems();
            int offset = 0;
            if (comboBox.getItemCount() > comboBox.getMaximumRowCount()) {
                offset = Integer.valueOf(String.valueOf(UIManager.get("ScrollBar.width")));
            }
            int popupNewWidth = Math.max(comboBox.getWidth(), preferredSizeForItem.width + offset + 3);
            int popupNewHeight = Math.min(comboBox.getItemCount(), comboBox.getMaximumRowCount())
                                 * preferredSizeForItem.height;
            popup.setPreferredSize(new Dimension(popupNewWidth, popupNewHeight + 2));
            popup.setLayout(new BorderLayout());
            popup.add(popup.getComponent(0));

            popupResizeNeeded = false;
        }


        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        }


        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    }

    private static class DefaultItemSizeGetter implements ItemSizeGetter {
        private JComboBox comboBox;


        private DefaultItemSizeGetter(JComboBox comboBox) {
            this.comboBox = comboBox;
        }


        public Dimension getPreferredSizeForItems() {
            FontMetrics fontMetrics = comboBox.getFontMetrics(comboBox.getFont());
            int widest = 0;
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                String itemValue = (String)comboBox.getItemAt(i);
                int stringWidth = SwingUtilities.computeStringWidth(fontMetrics, itemValue);
                if (widest < stringWidth) {
                    widest = stringWidth;
                }
            }
            return new Dimension(widest, comboBox.getHeight());
        }
    }
}
