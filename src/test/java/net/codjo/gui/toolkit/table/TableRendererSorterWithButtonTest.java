package net.codjo.gui.toolkit.table;
import net.codjo.test.common.LogString;
import net.codjo.test.release.task.gui.ClickTableHeaderStep;
import net.codjo.test.release.task.gui.TestContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.JTableHeader;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.TestHelper;
/**
 * Classe de test de {@link net.codjo.gui.toolkit.table.TableRendererSorterWithButton}.
 */
public class TableRendererSorterWithButtonTest extends JFCTestCase {
    private ClickTableHeaderStep step;
    private JTable table;
    private final LogString log = new LogString();


    public void test_doubleClickOnTableHeaderSortColumn() throws Exception {
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        table.getTableHeader().addMouseListener(myMouseAdapter);

        step.setName(table.getName());
        step.setColumn("2");
        step.setCount(2);
        step.proceed(new TestContext(this));

        assertEquals(2, myMouseAdapter.getClickedColumn());
    }


    public void test_clickOnTableHeaderComponent() throws Exception {
        step.setName(table.getName());
        step.setColumn("2");
        step.setCount(1);
        step.setComponent(true);
        step.proceed(new TestContext(this));

        log.assertContent("MyActionListener.actionPerformed()");
    }


    private void showFrame() {
        JToggleButton button = new JToggleButton("X");
        button.addActionListener(new MyActionListener());

        JFrame frame = new JFrame("Frame de test pour ClickStep");

        JPanel panel = new JPanel();
        frame.setContentPane(panel);

        table = new JTable(new Object[][]{
              {"Patrick", "Wilson", 68700, new java.sql.Date(80000000000L)},
              {"Patrick", "Panichi", 68700, new java.sql.Date(90000000000L)},
              {"Boris", "Catteau", 68400, new java.sql.Date(70000000000L)},
              {"Boris", "Panichi", 68700, new java.sql.Date(90000000000L)},
              {"Patricia", "Wilson", 68400, new java.sql.Date(70000000000L)},
              {"Olivier", "Panichi", 68500, new java.sql.Date(90000000000L)},
              {"Olivier", "Wilson", 68400, new java.sql.Date(80000000000L)},
              {"Olivier", "Catteau", 68500, new java.sql.Date(70000000000L)}
        }, new Object[]{"A", "B", "C", "D"});
        table.setName("maTable");

        TableRendererSorterWithButton sorter = new TableRendererSorterWithButton(table, button, 2);
        sorter.addMouseListenerToHeaderInTable(table);
        table.setModel(sorter);
        sorter.changeHeaderRenderer(table);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);

        frame.pack();
        frame.setVisible(true);
        flushAWT();
    }


    public static void main(String[] args) {
        new TableRendererSorterWithButtonTest().showFrame();
    }


    @Override
    protected void setUp() throws Exception {
        log.clear();
        step = new ClickTableHeaderStep();
        step.setTimeout(2);
        showFrame();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        TestHelper.cleanUp(this);
    }


    private class MyMouseAdapter extends MouseAdapter {
        private int clickedColumn = -1;


        public int getClickedColumn() {
            return clickedColumn;
        }


        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getClickCount() > 1) {
                JTableHeader tableHeader = (JTableHeader)event.getSource();
                clickedColumn = tableHeader.getColumnModel().getColumnIndexAtX(event.getX());
            }
        }
    }

    private class MyActionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            log.call("MyActionListener.actionPerformed");
        }
    }
}
