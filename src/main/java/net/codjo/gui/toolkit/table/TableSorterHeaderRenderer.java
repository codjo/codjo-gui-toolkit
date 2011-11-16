package net.codjo.gui.toolkit.table;
import net.codjo.gui.toolkit.icon.NumberedIcon;
import net.codjo.gui.toolkit.table.TableRendererSorter.SortingType;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;
/**
 *
 */
class TableSorterHeaderRenderer extends JLabel implements TableCellRenderer {
    private ImageIcon ascendingIcon;
    private ImageIcon descendingIcon;
    private TableRendererSorter model;


    protected TableSorterHeaderRenderer(TableRendererSorter model) {
        super("", null, SwingConstants.CENTER);
        this.model = model;

        setOpaque(true);
        setHorizontalTextPosition(SwingConstants.LEFT);
        setVerticalTextPosition(SwingConstants.CENTER);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        ascendingIcon = loadSortIcon("Ascending.gif");
        descendingIcon = loadSortIcon("Descending.gif");
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean selected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int col) {
        setText(value.toString());
        setIcon(null);

        int columnModelIndex = table.convertColumnIndexToModel(col);

        if (model.columnsToSortingType.containsKey(columnModelIndex)) {
            SortingType sortingType = model.columnsToSortingType.get(columnModelIndex);
            if (SortingType.ASCENDING.equals(sortingType)) {
                setIcon(ascendingIcon, model.getSortingIndex(columnModelIndex));
            }
            else if (SortingType.DESCENDING.equals(sortingType)) {
                setIcon(descendingIcon, model.getSortingIndex(columnModelIndex));
            }
        }

        return this;
    }


    private void setIcon(ImageIcon sortTypeIcon, int sortingIndex) {
        if (model.columnsToSortingType.size() > 1) {
            setIcon(new NumberedIcon(sortTypeIcon, sortingIndex + 1));
        }
        else {
            setIcon(sortTypeIcon);
        }
    }


    private ImageIcon loadSortIcon(String name) {
        try {
            return new ImageIcon(getClass().getResource(name));
        }
        catch (Exception exc) {
            return null;
        }
    }
}
