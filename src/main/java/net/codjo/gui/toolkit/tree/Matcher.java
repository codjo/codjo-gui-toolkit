package net.codjo.gui.toolkit.tree;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 */
public interface Matcher {
    boolean isApplicable(DefaultMutableTreeNode node);
    boolean match(Object userObject, String constraint);
}
