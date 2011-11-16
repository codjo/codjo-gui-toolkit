package net.codjo.gui.toolkit.tree;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import org.uispec4j.Tree;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class TreeFilterModelTest extends UISpecTestCase {
    private JTree jtree;
    private TreeFilterModel treeFilterModel;


    public void test_withoutFilteringOnString() {
        createStringTree();

        Tree tree = new Tree(jtree);
        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    efg #(color=333333)\n"
                                      + "    ghi #(color=333333)\n"
                                      + "    hij #(color=333333)\n"
                                      + "  cde #(color=333333)\n"
                                      + "    jkl #(color=333333)\n"
                                      + "    lmg #(color=333333)\n"
                                      + "    nop #(color=333333)"));
    }


    public void test_withFilteringOnString() {
        createStringTree();

        Tree tree = new Tree(jtree);
        treeFilterModel.addFilteringCriteria("keyName", new Matcher() {
            public boolean isApplicable(DefaultMutableTreeNode node) {
                return node.isLeaf();
            }


            public boolean match(Object userObject, String constraint) {
                String value = (String)userObject;
                return value != null && constraint != null && value.contains(constraint);
            }
        });
        treeFilterModel.setFilteringCriteriaConstraint("keyName", "g");

        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    efg #(color=333333)\n"
                                      + "    ghi #(color=333333)\n"
                                      + "  cde #(color=333333)\n"
                                      + "    lmg #(color=333333)"));
    }


    public void test_withoutFilteringOnObject() {
        createObjectTree();

        Tree tree = new Tree(jtree);
        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    efg #(color=333333)\n"
                                      + "    ghi #(color=333333)\n"
                                      + "    hij #(color=333333)\n"
                                      + "  cde #(color=333333)\n"
                                      + "    jkl #(color=333333)\n"
                                      + "    lmg #(color=333333)\n"
                                      + "    nop #(color=333333)"));
    }


    public void test_withFilteringOnObject() {
        createObjectTree();

        Tree tree = new Tree(jtree);
        treeFilterModel.addFilteringCriteria("name", new PersonNameMatcher(true));
        treeFilterModel.setFilteringCriteriaConstraint("name", "efg");

        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    efg #(color=333333)"));
    }


    public void test_withFilteringUpdateOnObject() {
        createObjectTree();

        Tree tree = new Tree(jtree);
        treeFilterModel.addFilteringCriteria("name", new PersonNameMatcher(true));
        treeFilterModel.setFilteringCriteriaConstraint("name", "efg");

        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    efg #(color=333333)"));

        treeFilterModel.setFilteringCriteriaConstraint("name", "ghi");
        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  abc #(color=333333)\n"
                                      + "    ghi #(color=333333)"));
    }


    public void test_nodeFilteringOnObject() {
        createObjectTree();

        treeFilterModel.setFilterEmptyFolder(false);
        Tree tree = new Tree(jtree);
        treeFilterModel.addFilteringCriteria("name", new PersonNameMatcher(false));
        treeFilterModel.setFilteringCriteriaConstraint("name", "cde");
        assertTrue(tree.contentEquals("Racine #(color=333333)\n"
                                      + "  cde #(color=333333)"));
    }


    private void createStringTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");
        treeFilterModel = new TreeFilterModel(root);

        DefaultMutableTreeNode node0 = new DefaultMutableTreeNode("abc");
        treeFilterModel.insertNodeInto(node0, root, 0);

        DefaultMutableTreeNode node00 = new DefaultMutableTreeNode("efg");
        treeFilterModel.insertNodeInto(node00, node0, 0);

        DefaultMutableTreeNode node01 = new DefaultMutableTreeNode("ghi");
        treeFilterModel.insertNodeInto(node01, node0, 1);

        DefaultMutableTreeNode node02 = new DefaultMutableTreeNode("hij");
        treeFilterModel.insertNodeInto(node02, node0, 2);

        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("cde");
        treeFilterModel.insertNodeInto(node1, root, 1);

        DefaultMutableTreeNode node10 = new DefaultMutableTreeNode("jkl");
        treeFilterModel.insertNodeInto(node10, node1, 0);

        DefaultMutableTreeNode node11 = new DefaultMutableTreeNode("lmg");
        treeFilterModel.insertNodeInto(node11, node1, 1);

        DefaultMutableTreeNode node12 = new DefaultMutableTreeNode("nop");
        treeFilterModel.insertNodeInto(node12, node1, 2);

        jtree = new JTree(treeFilterModel);
    }


    private void createObjectTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Person("Racine", 41));
        treeFilterModel = new TreeFilterModel(root);

        DefaultMutableTreeNode node0 = new DefaultMutableTreeNode(new Person("abc", 28));
        treeFilterModel.insertNodeInto(node0, root, 0);

        DefaultMutableTreeNode node00 = new DefaultMutableTreeNode(new Person("efg", 30));
        treeFilterModel.insertNodeInto(node00, node0, 0);

        DefaultMutableTreeNode node01 = new DefaultMutableTreeNode(new Person("ghi", 31));
        treeFilterModel.insertNodeInto(node01, node0, 1);

        DefaultMutableTreeNode node02 = new DefaultMutableTreeNode(new Person("hij", 30));
        treeFilterModel.insertNodeInto(node02, node0, 2);

        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(new Person("cde", 29));
        treeFilterModel.insertNodeInto(node1, root, 1);

        DefaultMutableTreeNode node10 = new DefaultMutableTreeNode(new Person("jkl", 28));
        treeFilterModel.insertNodeInto(node10, node1, 0);

        DefaultMutableTreeNode node11 = new DefaultMutableTreeNode(new Person("lmg", 29));
        treeFilterModel.insertNodeInto(node11, node1, 1);

        DefaultMutableTreeNode node12 = new DefaultMutableTreeNode(new Person("nop", 29));
        treeFilterModel.insertNodeInto(node12, node1, 2);

        jtree = new JTree(treeFilterModel);
        jtree.setCellRenderer(new TreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value,
                                                          boolean selected,
                                                          boolean expanded,
                                                          boolean leaf,
                                                          int row,
                                                          boolean hasFocus) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
                Person person = (Person)node.getUserObject();
                return new JLabel(person.getName());
            }
        });
    }


    private class Person {
        String name;
        int age;


        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }


        public String getName() {
            return name;
        }


        public void setName(String name) {
            this.name = name;
        }


        public int getAge() {
            return age;
        }


        public void setAge(int age) {
            this.age = age;
        }


        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }

            Person person = (Person)object;
            return age == person.age && name.equals(person.name);
        }


        @Override
        public int hashCode() {
            int result;
            result = name.hashCode();
            result = 31 * result + age;
            return result;
        }
    }

    private class PersonNameMatcher implements Matcher {
        private boolean leavesOnly;


        private PersonNameMatcher(boolean leavesOnly) {
            this.leavesOnly = leavesOnly;
        }


        public boolean isApplicable(DefaultMutableTreeNode node) {
            if (leavesOnly) {
                return node.isLeaf();
            }
            return true;
        }


        public boolean match(Object userObject, String constraint) {
            Person person = (Person)userObject;
            return person != null && constraint != null && constraint.equals(person.getName());
        }
    }
}
