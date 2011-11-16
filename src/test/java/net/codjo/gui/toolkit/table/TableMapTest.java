package net.codjo.gui.toolkit.table;

import net.codjo.test.common.LogString;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import javax.swing.table.DefaultTableModel;
import org.junit.Test;

public class TableMapTest {
    private LogString log = new LogString();


    @Test
    public void test_setModel_twice() throws Exception {
        DefaultTableModel subModel = new DefaultTableModel();
        TableMap tableMap = new TableMap();
        tableMap.addTableModelListener(new TableModelLogger(log));

        tableMap.setModel(subModel);
        subModel.addRow(new Object[]{"a"});
        assertThat(log.getContent(), is("tableChanged(insert(DefaultTableModel) column[ALL] row[0])"));

        log.clear();

        DefaultTableModel newSubModel = new DefaultTableModel();
        tableMap.setModel(newSubModel);
        newSubModel.addRow(new Object[]{"a"});
        assertThat(log.getContent(), is("tableChanged(insert(DefaultTableModel) column[ALL] row[0])"));

        log.clear();
        subModel.addRow(new Object[]{"a"});
        assertThat(log.getContent(), is(""));
    }
}
