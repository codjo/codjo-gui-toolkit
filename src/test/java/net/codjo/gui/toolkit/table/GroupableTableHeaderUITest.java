package net.codjo.gui.toolkit.table;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import junit.framework.TestCase;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupableTableHeaderUITest extends TestCase {

    public void testGroupRenderer() {
        TableColumnModel tcm = new DefaultTableColumnModel();
        TableColumn col = new TableColumn();
        col.setHeaderValue("header value");
        TableCellRenderer tableCellRenderer = mock(TableCellRenderer.class);
        when(tableCellRenderer.getTableCellRendererComponent(any(JTable.class),
                                                             any(Object.class),
                                                             anyBoolean(),
                                                             anyBoolean(),
                                                             anyInt(),
                                                             anyInt()))
              .thenReturn(new JLabel("rendered label"));
        col.setHeaderRenderer(tableCellRenderer);
        tcm.addColumn(col);

        Graphics graphics = mock(Graphics.class);
        Rectangle clipBounds = new Rectangle(0, 0, 10, 10);
        when(graphics.getClipBounds()).thenReturn(clipBounds);
        when(graphics.create(anyInt(), anyInt(), anyInt(), anyInt()))
              .thenReturn(graphics);
        when(graphics.create()).thenReturn(graphics);
        when(graphics.getFont()).thenReturn(new Font("", 0, 1));

        GroupableTableHeader groupableTableHeader = new GroupableTableHeader(tcm);
        groupableTableHeader.setSize(100, 100);
        groupableTableHeader.setTable(new JTable());
        groupableTableHeader.getUI().paint(graphics, null);

        verify(tableCellRenderer, times(1)).getTableCellRendererComponent(
              any(JTable.class),
              any(Object.class),
              anyBoolean(),
              anyBoolean(),
              anyInt(),
              anyInt());
    }
}
