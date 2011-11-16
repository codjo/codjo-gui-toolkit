package net.codjo.gui.toolkit.readonly;

import net.codjo.gui.toolkit.util.GuiUtil;
import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.uispec4j.Table;
import org.uispec4j.TableCellValueConverter;
import org.uispec4j.UISpecTestCase;

/**
 *
 */
public class ReadOnlyTableComponentTest extends UISpecTestCase {
    private static final Color ENABLED_CHECKBOX_FG_COLOR = new JCheckBox().getForeground();
    private static final Color DISABLED_CHECKBOX_FG_COLOR = GuiUtil.DISABLED_FOREGROUND_COLOR;

    private ReadOnlyManager manager;


    @Override
    protected void setUp() throws Exception {
        manager = new ReadOnlyManager(true, true);
    }


    public void test_readOnlyTableUsingRendererByClass() throws Exception {
        String[][] data = {{"1", "2"}, {"3", "4"}};
        String[] colNames = {"col1", "col2"};
        JTable jTable = new JTable(data, colNames);
        jTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
        Table table = new Table(jTable);

        assertTableByClassIsEnabled(table);

        manager.addReadOnlyComponent(jTable);

        assertTableByClassIsDisabled(table);

        manager.setReadOnly(false);

        assertTableByClassIsEnabled(table);
    }


    public void test_readOnlyTableColumnValuesWithRendererByClass() throws Exception {
        String[][] data = {{"2001-01-02"}, {"2002-01-02"}};
        String[] colNames = {"date"};
        JTable jTable = new JTable(data, colNames);
        jTable.setDefaultRenderer(Object.class, new DateTableCellRenderer());

        Table table = new Table(jTable);

        assertTableColumnRenderedValue(table, new String[][]{{"02/01/2001"}, {"02/01/2002"}});

        manager.addReadOnlyComponent(jTable);

        assertTableColumnRenderedValue(table, new String[][]{{"02/01/2001"}, {"02/01/2002"}});

        manager.setReadOnly(false);

        assertTableColumnRenderedValue(table, new String[][]{{"02/01/2001"}, {"02/01/2002"}});
    }


    private void assertTableColumnRenderedValue(Table table, String[][] expectedRenderedColumnValues)
          throws Exception {
        assertTrue(table.contentEquals(expectedRenderedColumnValues));
    }


    public void test_readOnlyTableUsingRendererByColumn() throws Exception {
        JTable jTable = new JTable(
              new Object[][]{
                    {"one", Boolean.TRUE},
                    {"two", Boolean.FALSE}
              },
              new String[]{"col1", "col2"});

        jTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                setText("number " + value);
                setBackground(Color.RED);
                setForeground(Color.GREEN);
                return this;
            }
        });

        jTable.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JCheckBox checkBox = new JCheckBox("", ((Boolean)value));
                checkBox.setBackground(Color.WHITE);
                return checkBox;
            }
        });

        Table table = new Table(jTable);
        table.setCellValueConverter(1, new TableCellValueConverter() {
            public Object getValue(int row, int column, Component renderedComponent, Object modelObject) {
                return renderedComponent.isEnabled() ? "ENABLED" : "DISABLED";
            }
        });

        assertTableByColumnIsEnabled(table);

        manager.addReadOnlyComponent(jTable, new ReadOnlyValueSetter() {
            public void setValue(JComponent component) {
                JTable table = (JTable)component;
                table.setValueAt("three", 0, 0);
            }
        });

        assertTableByColumnIsDisabled(table);
        jTable.setValueAt("one", 0, 0);
        manager.setReadOnly(false, false);

        assertTableByColumnIsEnabled(table);
    }


    private void assertTableByColumnIsEnabled(Table table) {
        assertTrue(table.isEnabled());
        assertTrue(table.contentEquals(new Object[][]{
              {"number one", "ENABLED"},
              {"number two", "ENABLED"},
        }));
        assertTrue(table.backgroundEquals(
              new Color[][]{
                    {Color.RED, Color.WHITE},
                    {Color.RED, Color.WHITE}
              }));
        assertTrue(table.foregroundEquals(
              new Color[][]{
                    {Color.GREEN, ENABLED_CHECKBOX_FG_COLOR},
                    {Color.GREEN, ENABLED_CHECKBOX_FG_COLOR}
              }));
    }


    private void assertTableByColumnIsDisabled(Table table) {
        assertFalse(table.isEnabled());
        assertTrue(table.contentEquals(new Object[][]{
              {"number three", "DISABLED"},
              {"number two", "DISABLED"},
        }));
        assertTrue(table.backgroundEquals(
              new Color[][]{
                    {GuiUtil.DISABLED_BACKGROUND_COLOR, GuiUtil.DISABLED_BACKGROUND_COLOR},
                    {GuiUtil.DISABLED_BACKGROUND_COLOR, GuiUtil.DISABLED_BACKGROUND_COLOR}
              }));
        assertTrue(table.foregroundEquals(
              new Color[][]{
                    {GuiUtil.DISABLED_FOREGROUND_COLOR, DISABLED_CHECKBOX_FG_COLOR},
                    {GuiUtil.DISABLED_FOREGROUND_COLOR, DISABLED_CHECKBOX_FG_COLOR}
              }));
    }


    private void assertTableByClassIsEnabled(Table table) {
        assertTrue(table.isEnabled());
        assertTrue(table.backgroundEquals(
              new Color[][]{
                    {Color.RED, Color.RED},
                    {Color.RED, Color.RED}
              }));
        assertTrue(table.foregroundEquals(
              new Color[][]{
                    {Color.GREEN, Color.GREEN},
                    {Color.GREEN, Color.GREEN}
              }));
    }


    private void assertTableByClassIsDisabled(Table table) {
        assertFalse(table.isEnabled());
        assertTrue(table.backgroundEquals(
              new Color[][]{
                    {GuiUtil.DISABLED_BACKGROUND_COLOR, GuiUtil.DISABLED_BACKGROUND_COLOR},
                    {GuiUtil.DISABLED_BACKGROUND_COLOR, GuiUtil.DISABLED_BACKGROUND_COLOR}
              }));
        assertTrue(table.foregroundEquals(
              new Color[][]{
                    {GuiUtil.DISABLED_FOREGROUND_COLOR, GuiUtil.DISABLED_FOREGROUND_COLOR},
                    {GuiUtil.DISABLED_FOREGROUND_COLOR, GuiUtil.DISABLED_FOREGROUND_COLOR}
              }));
    }


    private static class MyTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setBackground(Color.RED);
            setForeground(Color.GREEN);
            return this;
        }
    }

    private static class DateTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = (String)value;
            try {
                formattedDate = dateFormatOutput.format(dateFormatInput.parse((String)value));
            }
            catch (ParseException e) {
                ;
            }
            return super.getTableCellRendererComponent(table,
                                                       formattedDate,
                                                       isSelected,
                                                       hasFocus,
                                                       row,
                                                       column);
        }
    }
}
