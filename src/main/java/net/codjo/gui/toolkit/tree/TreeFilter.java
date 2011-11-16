/**
 * Copyright (c) 2007 State Of Mind.
 */
package net.codjo.gui.toolkit.tree;

import java.util.HashMap;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeFilter {
    private Map<String, Matcher> matchers = new HashMap<String, Matcher>();
    private Map<String, String> constraints = new HashMap<String, String>();


    boolean isNodeVisible(DefaultMutableTreeNode treeNode) {
        Object userObject = treeNode.getUserObject();
        for (Map.Entry<String, Matcher> entry : matchers.entrySet()) {
            String key = entry.getKey();
            Matcher matcher = entry.getValue();
            if (matcher.isApplicable(treeNode) && !matcher.match(userObject, constraints.get(key))) {
                return false;
            }
        }
        return true;
    }


    public TreeFilter addCriteriaMatcher(String keyName, Matcher matcher) {
        matchers.put(keyName, matcher);
        return this;
    }


    public void removeCriteriaMatcher(String keyName) {
        matchers.remove(keyName);
        constraints.remove(keyName);
    }

    public void setConstraintValue(String keyName, String constraint) {
        constraints.put(keyName, constraint);
    }


    public void removeAllConstraints() {
        constraints.clear();
    }


    public boolean isFiltered() {
        return !matchers.isEmpty();
    }
}
