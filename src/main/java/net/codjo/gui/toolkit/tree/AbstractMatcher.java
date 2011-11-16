package net.codjo.gui.toolkit.tree;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 *
 */
public abstract class AbstractMatcher<T> implements Matcher {
    private Class<T> aClass;


    protected AbstractMatcher(Class<T> aClass) {
        this.aClass = aClass;
    }


    public final boolean isApplicable(DefaultMutableTreeNode node) {
        return aClass.isInstance(node.getUserObject());
    }


    public final boolean match(Object userObject, String constraint) {
        return matchUserObject(aClass.cast(userObject), constraint);
    }


    public abstract boolean matchUserObject(T userObject, String constraint);
}
