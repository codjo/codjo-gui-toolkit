/**
 * Copyright (c) 2007 State Of Mind.
 */
package net.codjo.gui.toolkit.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class TreeFilterModel extends DefaultTreeModel {
    private TreeFilter filter;
    private boolean filterEmptyFolder = true;
    private List<FilterListener> filterListeners = new ArrayList<FilterListener>();
    private boolean needsUpdate = true;
    private DefaultTreeModel filteredModel;


    public TreeFilterModel(TreeNode root) {
        super(root);
        this.filter = new TreeFilter();
    }


    public void addFilteringCriteria(String keyName, Matcher matcher) {
        filter.addCriteriaMatcher(keyName, matcher);
        needsUpdate = true;
        fireFilterChanged();
    }


    public void removeFilteringCriteria(String keyName) {
        filter.removeCriteriaMatcher(keyName);
        needsUpdate = true;
        fireFilterChanged();
    }


    public void setFilteringCriteriaConstraint(String keyName, String constraint) {
        filter.setConstraintValue(keyName, constraint);
        needsUpdate = true;
        fireFilterChanged();
    }


    public void resetFilteringCriteria() {
        filter.removeAllConstraints();
        needsUpdate = true;
        fireFilterChanged();
    }


    public boolean isFilterEmptyFolder() {
        return filterEmptyFolder;
    }


    public void setFilterEmptyFolder(boolean filterEmptyFolder) {
        if (this.filterEmptyFolder != filterEmptyFolder) {
            this.filterEmptyFolder = filterEmptyFolder;
            needsUpdate = true;
            fireFilterChanged();
        }
    }


    public void addFilterListener(FilterListener filterListener) {
        filterListeners.add(filterListener);
    }


    public void removeFilterListener(FilterListener filterListener) {
        filterListeners.remove(filterListener);
    }


    private void notifyFilterListeners() {
        for (FilterListener filterListener : filterListeners) {
            filterListener.update(this, filter);
        }
    }


    @Override
    public DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode)getFilteredModel().getRoot();
    }


    @Override
    public void reload() {
        super.reload();
        getFilteredModel().reload();
        needsUpdate = true;
        fireFilterChanged();
    }


    @Override
    public int getChildCount(Object parent) {
        return getFilteredModel().getChildCount(parent);
    }


    @Override
    public Object getChild(Object parent, int index) {
        return getFilteredModel().getChild(parent, index);
    }


    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return getFilteredModel().getIndexOfChild(parent, child);
    }


    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        super.addTreeModelListener(listener);
        getFilteredModel().addTreeModelListener(listener);
    }


    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        super.removeTreeModelListener(listener);
        getFilteredModel().removeTreeModelListener(listener);
    }


    @Override
    public boolean isLeaf(Object node) {
        return getFilteredModel().isLeaf(node);
    }


    private DefaultTreeModel getFilteredModel() {
        if (needsUpdate) {
            needsUpdate = false;
            updateFilteredModel();
        }
        return filteredModel;
    }


    private void updateFilteredModel() {
        DefaultMutableTreeNode sourceRoot = (DefaultMutableTreeNode)super.getRoot();
        DefaultMutableTreeNode filteredRoot = (DefaultMutableTreeNode)sourceRoot.clone();
        wrapFilteredNodes(sourceRoot, filteredRoot);
        filteredModel = new DefaultTreeModel(filteredRoot);

        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                filteredModel.addTreeModelListener((TreeModelListener)listeners[i + 1]);
            }
        }

        fireFilterChanged();
    }


    private void fireFilterChanged() {
        getFilteredModel().reload();
        notifyFilterListeners();
    }


    private int wrapFilteredNodes(DefaultMutableTreeNode source, DefaultMutableTreeNode target) {
        int filteredNodes = 0;
        for (int i = 0; i < super.getChildCount(source); i++) {
            DefaultMutableTreeNode sourceChild = (DefaultMutableTreeNode)super.getChild(source, i);
            DefaultMutableTreeNode targetChild = (DefaultMutableTreeNode)sourceChild.clone();
            if (filter.isNodeVisible(sourceChild)) {
                int childNodeCount = wrapFilteredNodes(sourceChild, targetChild);
                boolean filterEmpty = filterEmptyFolder && !super.isLeaf(sourceChild) && childNodeCount == 0;
                if (!filterEmpty) {
                    target.add(targetChild);
                    filteredNodes++;
                }
            }
        }
        return filteredNodes;
    }
}
