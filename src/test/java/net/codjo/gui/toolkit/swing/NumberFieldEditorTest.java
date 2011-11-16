package net.codjo.gui.toolkit.swing;
import net.codjo.gui.toolkit.number.NumberField;
import net.codjo.gui.toolkit.number.NumberFieldRenderer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.uispec4j.Table;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class NumberFieldEditorTest extends UISpecTestCase {

    public void test_customizeEditor() throws Exception {
        Table table = createTable();
        assertTrue(table.contentEquals(new Object[][]{
              {"1.15000", "A"},
              {"2.12000", "B"},
        }));

        Table.Cell cell = table.editCell(1, 0);
        assertTrue(cell.getTextBox().textEquals("2.12"));
    }


    private Table createTable() {
        DefaultTableModel model = new DefaultTableModel(
              new Object[][]{{"1.15000", "A"},
                             {"2.12000", "B"},
              },
              new Object[]{"Number", "Data"}) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellEditor(new CustomizedNumberEditor());

        return new Table(table);
    }


    public void test_setNumber() throws Exception {
        NumberFieldEditor editor = new NumberFieldEditor();
        editor.setNumber(10);
        assertEquals("10", editor.getCellEditorValue());
        editor.setNumber(null);
        assertEquals("null", editor.getCellEditorValue());
        editor.setNumber("null");
        assertEquals("null", editor.getCellEditorValue());
        editor.setNumber("10.3");
        assertEquals("10.3", editor.getCellEditorValue());
    }



    private class CustomizedNumberEditor extends NumberFieldEditor {

        @Override
        protected void customizeEditor(NumberField zeEditor) {
            zeEditor.setRenderer(new NumberFieldRenderer(
                  new DecimalFormat("##0.###", new DecimalFormatSymbols(Locale.ENGLISH))));
            zeEditor.setApplyRendererInEditMode(true);
        }
    }
}
