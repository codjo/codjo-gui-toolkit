package net.codjo.gui.toolkit.number;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.apache.log4j.Logger;
/**
 *
 */
public class PercentRenderer implements TableCellRenderer, ListCellRenderer {
    private static final Logger LOG = Logger.getLogger(PercentRenderer.class.getName());
    public static final String LIST_NOT_TABLE_ERROR =
          "Ce renderer a été initialisé pour une JList et non une JTable.";
    public static final String TABLE_NOT_LIST_ERROR =
          "Ce renderer a été initialisé pour une JTable et non une JList.";
    private TableCellRenderer rootTableRenderer;
    private ListCellRenderer rootListRenderer;


    public PercentRenderer(TableCellRenderer rootRenderer) {
        this.rootTableRenderer = rootRenderer;
    }


    public PercentRenderer(ListCellRenderer rootListRenderer) {
        this.rootListRenderer = rootListRenderer;
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        if (rootTableRenderer == null) {
            throw new IllegalArgumentException(LIST_NOT_TABLE_ERROR);
        }
        value = getRatio(value);

        return rootTableRenderer.getTableCellRendererComponent(table, value, isSelected,
                                                               hasFocus, row, column);
    }


    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        if (rootListRenderer == null) {
            throw new IllegalArgumentException(TABLE_NOT_LIST_ERROR);
        }

        value = getRatio(value);

        return rootListRenderer.getListCellRendererComponent(list, value, index,
                                                             isSelected, cellHasFocus);
    }


    private Object getRatio(Object value) {
        try {
            if (value != null) {
                double ratio = Double.parseDouble((String)value);
                return String.valueOf(ratio * 100);
            }
        }
        catch (NumberFormatException exception) {
            LOG.error(exception);
        }
        return value;
    }
}
