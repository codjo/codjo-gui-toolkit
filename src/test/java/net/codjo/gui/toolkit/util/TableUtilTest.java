package net.codjo.gui.toolkit.util;
import net.codjo.gui.toolkit.swing.TextFieldEditor;
import net.codjo.test.common.LogString;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.uispec4j.Button;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class TableUtilTest extends UISpecTestCase {
    private JTable jTable;

    private LogString logString = new LogString();
    private JButton validateButton;
    private JButton cancelButton;
    private Table table;


    public void test_configureStopEditing() throws Exception {
        table.selectRows(new int[]{0, 2});
        table.editCell(0, 1, "1999-01-01", false);
        assertTrue(jTable.isEditing());

        assertTrue(table.contentEquals(new Object[][]{{"Vanessa", "2004-06-19"},
                                                      {"Alex", "1978-06-29"},
                                                      {"Seb", "1974-11-23"}
        }));

        Button button = new Button(validateButton);
        button.click();

        assertFalse("la table est en edition", jTable.isEditing());
        assertTrue(table.contentEquals(new Object[][]{{"Vanessa", "1999-01-01"},
                                                      {"Alex", "1978-06-29"},
                                                      {"Seb", "1974-11-23"}
        }));
        assertTrue(table.rowsAreSelected(new int[]{0, 2}));

        assertEquals("table.isEditing() = false, validate.normal.actionPerformed()", logString.getContent());
    }


    public void test_configureCancelEditing() throws Exception {
        table.selectRows(new int[]{0, 2});
        table.editCell(0, 1, "1999-01-01", false);
        assertTrue(jTable.isEditing());

        assertTrue(table.contentEquals(new Object[][]{{"Vanessa", "2004-06-19"},
                                                      {"Alex", "1978-06-29"},
                                                      {"Seb", "1974-11-23"}
        }));

        Button button = new Button(cancelButton);
        button.click();

        assertFalse("la table est en edition", jTable.isEditing());
        assertTrue(table.contentEquals(new Object[][]{{"Vanessa", "2004-06-19"},
                                                      {"Alex", "1978-06-29"},
                                                      {"Seb", "1974-11-23"}
        }));
        assertTrue(table.rowsAreSelected(new int[]{0, 2}));

        assertEquals("table.isEditing() = false, cancel.normal.actionPerformed()", logString.getContent());
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JFrame window = new JFrame();

        createTable();
        createButtons();
        configureButtons();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(jTable, BorderLayout.CENTER);
        mainPanel.add(validateButton, BorderLayout.EAST);
        mainPanel.add(cancelButton, BorderLayout.WEST);

        window.getContentPane().add(mainPanel);
    }


    private void createButtons() {
        validateButton = new JButton("Valider");
        validateButton.setName("validate");
        validateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logString.info("table.isEditing() = " + jTable.isEditing());
                logString.call("validate.normal.actionPerformed");
            }
        });

        cancelButton = new JButton("Annuler");
        cancelButton.setName("cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                logString.info("table.isEditing() = " + jTable.isEditing());
                logString.call("cancel.normal.actionPerformed");
            }
        });
    }


    private void configureButtons() {
        TableUtil.configureTableCellEditing(jTable,
                                            new JButton[]{validateButton},
                                            new JButton[]{cancelButton});
    }


    private void createTable() {
        DefaultTableModel model = new DefaultTableModel(
              new Object[][]{{"Vanessa", "2004-06-19"},
                             {"Alex", "1978-06-29"},
                             {"Seb", "1974-11-23"}
              },
              new Object[]{"prenom", "dateNaissance"});
        jTable = new JTable(model);
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable.getColumnModel().getColumn(1).setCellEditor(new TextFieldEditor());

        table = new Table(jTable);
    }
}
