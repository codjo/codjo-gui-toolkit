package net.codjo.gui.toolkit.util;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 *
 */
public class TreeUtil {

    public static final TreeCellValueConverter JLABEL_CONVERTER = new JLabelTreeCellValueConverter();


    private TreeUtil() {
    }


    public interface TreeCellValueConverter {

        /**
         * Returns the textual representation of the given tree object
         *
         * @param renderedComponent the Component returned by the JTree's renderer
         * @param modelObject       the Object returned by the JTree's internal model
         */
        String getValue(Component renderedComponent, Object modelObject);
    }


    public static boolean isLeaf(JTree tree, TreePath path) {
        return tree.getModel().isLeaf(path.getLastPathComponent());
    }


    public static void expandSubtree(JTree tree, TreePath path) {
        TreeModel treeModel = tree.getModel();
        Object node = path.getLastPathComponent();
        for (int i = 0; i < treeModel.getChildCount(node); i++) {
            Object child = treeModel.getChild(node, i);
            TreePath childPath = path.pathByAddingChild(child);
            if (!isLeaf(tree, childPath)) {
                expandSubtree(tree, childPath);
            }
        }
        tree.expandPath(path);
    }


    public static String getRenderedLabel(JTree tree, TreePath path) {
        return getRenderedLabel(tree, path, JLABEL_CONVERTER);
    }


    public static String getRenderedLabel(JTree tree, TreePath path, TreeCellValueConverter converter) {
        TreeCellRenderer cellRenderer = tree.getCellRenderer();
        Object lastPathComponent = path.getLastPathComponent();
        Component rendererComponent = cellRenderer.getTreeCellRendererComponent(tree,
                                                                                lastPathComponent,
                                                                                tree.isPathSelected(path),
                                                                                tree.isExpanded(path),
                                                                                tree.getModel().isLeaf(
                                                                                      lastPathComponent),
                                                                                tree.getRowForPath(path),
                                                                                tree.hasFocus());
        return converter.getValue(rendererComponent, lastPathComponent);
    }


    public static void setSelectionPaths(JTree tree, TreePath[] paths) {
        TreePath[] oldSelection = tree.getSelectionPaths();
        tree.setSelectionPaths(paths);
        scrollSelectionToVisible(tree, paths, oldSelection);
    }


    public static void scrollSelectionToVisible(JTree tree,
                                                TreePath[] newSelection,
                                                TreePath[] oldSelection) {
        if ((newSelection == null) || (newSelection.length == 0)) {
            return;
        }
        if ((oldSelection == null) || (oldSelection.length == 0)) {
            tree.scrollPathToVisible(newSelection[0]);
            return;
        }
        for (int index = 0; index < newSelection.length; index++) {
            TreePath treePath = newSelection[index];
            if ((oldSelection.length == index) || (!treePath.equals(oldSelection[index]))) {
                tree.scrollPathToVisible(treePath);
                return;
            }
        }
    }


    private static class JLabelTreeCellValueConverter implements TreeCellValueConverter {
        public String getValue(Component renderedComponent, Object modelObject) {
            try {
                JLabel label = (JLabel)renderedComponent;
                return label.getText();
            }
            catch (ClassCastException e) {
                throw new RuntimeException("Only JLabel components are managed by trees", e);
            }
        }
    }
}
