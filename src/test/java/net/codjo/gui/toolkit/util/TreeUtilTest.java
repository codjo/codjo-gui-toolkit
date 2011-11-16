package net.codjo.gui.toolkit.util;
import org.junit.Test;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Tree;
import org.uispec4j.DefaultTreeCellValueConverter;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.Component;
import net.codjo.gui.toolkit.util.TreeUtil.TreeCellValueConverter;
import static net.codjo.gui.toolkit.util.TreeUtil.getRenderedLabel;
import static net.codjo.gui.toolkit.util.TreeUtil.setSelectionPaths;
import static net.codjo.gui.toolkit.util.TreeUtil.expandSubtree;
import static net.codjo.gui.toolkit.util.TreeUtil.isLeaf;
/**
 *
 */
public class TreeUtilTest extends UISpecTestCase{
    private Tree uiSpecTree;
    private JTree jTree;
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode child11;
    private DefaultMutableTreeNode child1;


    public void testIsLeaf() throws Exception {
        assertTrue(isLeaf(jTree, new TreePath(child11.getPath())));
        assertFalse(isLeaf(jTree, new TreePath(root.getPath())));
    }


    @Test
    public void testExpandSubtree() throws Exception{
        expandSubtree(jTree, new TreePath(root.getPath()));
        assertTrue(jTree.isExpanded(new TreePath(child1.getPath())));
    }



    public void testGetRenderedLabelWithDefaultConverter() throws Exception{
        assertEquals("child11", getRenderedLabel(jTree, new TreePath(child11.getPath())));
    }



    public void testGetRenderedLabelWithCustomzedConverter() throws Exception{
        assertEquals("toto child11", getRenderedLabel(jTree, new TreePath(child11.getPath()), new TreeCellValueConverter() {
            public String getValue(Component renderedComponent, Object modelObject) {
                return "toto " + modelObject.toString();
            }
        }));
    }


    @Test
    public void testSetSelectionPaths() throws Exception{
        setSelectionPaths(jTree, new TreePath[] {new TreePath(child11.getPath())});
        uiSpecTree.selectionEquals("child1|child11").check();
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        jTree = createTreeWithChilds();
        uiSpecTree = new Tree(jTree);
        uiSpecTree.setSeparator("|");
        uiSpecTree.setCellValueConverter(new DefaultTreeCellValueConverter() {
            @Override
            public String getValue(Component renderedComponent, Object modelObject) {
                return modelObject.toString();
            }
        });
    }

    private JTree createTreeWithChilds() {
        JTree tree = new JTree();

        // Noeud racine
        root = new DefaultMutableTreeNode("rootName");

        // Ajout d'une feuille à la racine
        child1 = new DefaultMutableTreeNode("child1");
        root.add(child1);
        child11 = new DefaultMutableTreeNode("child11");
        child1.add(child11);
        root.add(new DefaultMutableTreeNode("child2"));

        // Construction de l'arbre
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);

        return tree;
    }
}
