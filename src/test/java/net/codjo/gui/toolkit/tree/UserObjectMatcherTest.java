package net.codjo.gui.toolkit.tree;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.uispec4j.Tree;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class UserObjectMatcherTest extends UISpecTestCase {
    private JTree jtree;
    private TreeFilterModel treeFilterModel;
    private Tree tree;


    @Override
    protected void setUp() throws Exception {
        createObjectTree();
        tree = new Tree(jtree);
    }


    public void test_withoutFiltering() {
        assertTrue(tree.contentEquals("Wilson\n"
                                      + "  Gonnot\n"
                                      + "    crego\n"
                                      + "    mamie\n"
                                      + "    oggi\n"
                                      + "  JHM\n"
                                      + "    fradin\n"
                                      + "    vico\n"
                                      + "    ooo"));
    }


    public void test_personFiltering() throws Exception {
        treeFilterModel
              .addFilteringCriteria("name", new AbstractMatcher<ExternalPerson>(ExternalPerson.class) {
                  public boolean matchUserObject(ExternalPerson userObject, String constraint) {
                      return userObject != null && userObject.getName().contains("e");
                  }
              });
        treeFilterModel.setFilteringCriteriaConstraint("name", "e");
        treeFilterModel.setFilterEmptyFolder(false);

        assertTrue(tree.contentEquals("Wilson\n"
                                      + "  Gonnot\n"
                                      + "    crego\n"
                                      + "    mamie\n"
                                      + "  JHM"));
    }


    private void createObjectTree() {
        DefaultMutableTreeNode wilson = new DefaultMutableTreeNode(new InternalPerson("Wilson", 72, 200000));
        treeFilterModel = new TreeFilterModel(wilson);

        DefaultMutableTreeNode gonnot = new DefaultMutableTreeNode(new InternalPerson("Gonnot", 34, 100000));
        treeFilterModel.insertNodeInto(gonnot, wilson, 0);

        DefaultMutableTreeNode crego = new DefaultMutableTreeNode(new ExternalPerson("crego", 32, 18));
        treeFilterModel.insertNodeInto(crego, gonnot, 0);

        DefaultMutableTreeNode galabert = new DefaultMutableTreeNode(new InternalPerson("mamie", 38, 10));
        treeFilterModel.insertNodeInto(galabert, gonnot, 1);

        DefaultMutableTreeNode oggi = new DefaultMutableTreeNode(new ExternalPerson("oggi", 45, 12));
        treeFilterModel.insertNodeInto(oggi, gonnot, 2);

        DefaultMutableTreeNode moner = new DefaultMutableTreeNode(new InternalPerson("JHM", 37, 150000));
        treeFilterModel.insertNodeInto(moner, wilson, 1);

        DefaultMutableTreeNode fradin = new DefaultMutableTreeNode(new ExternalPerson("fradin", 34, 21));
        treeFilterModel.insertNodeInto(fradin, moner, 0);

        DefaultMutableTreeNode vico = new DefaultMutableTreeNode(new ExternalPerson("vico", 12, 20));
        treeFilterModel.insertNodeInto(vico, moner, 1);

        DefaultMutableTreeNode ooo = new DefaultMutableTreeNode(new ExternalPerson("ooo", 29, 19));
        treeFilterModel.insertNodeInto(ooo, moner, 2);

        jtree = new JTree(treeFilterModel);
        jtree.setCellRenderer(new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value,
                                                          boolean selected,
                                                          boolean expanded,
                                                          boolean leaf,
                                                          int row,
                                                          boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
                Person person = (Person)node.getUserObject();
                setForeground(Color.BLACK);
                setText(person.getName());
                return this;
            }
        });
    }


    private class Person {
        private String name;
        private int age;


        private Person(String name, int age) {
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
    }

    private class InternalPerson extends Person {
        private int bonusAmount = 0;


        private InternalPerson(String name, int age, int bonusAmount) {
            super(name, age);
            this.bonusAmount = bonusAmount;
        }


        public int getBonusAmount() {
            return bonusAmount;
        }


        public void setBonusAmount(int bonusAmount) {
            this.bonusAmount = bonusAmount;
        }
    }

    private class ExternalPerson extends Person {
        private int workingDaysCount;


        private ExternalPerson(String name, int age, int workingDaysCount) {
            super(name, age);
            this.workingDaysCount = workingDaysCount;
        }


        public int getWorkingDaysCount() {
            return workingDaysCount;
        }


        public void setWorkingDaysCount(int workingDaysCount) {
            this.workingDaysCount = workingDaysCount;
        }
    }
}
