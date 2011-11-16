package net.codjo.gui.toolkit.table;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import net.codjo.gui.toolkit.swing.CheckBoxRenderer;
import org.apache.log4j.Logger;

/**
 * Sert à trier les colonnes.
 */
public class TableRendererSorter extends TableMap {
    private static final Logger APP = Logger.getLogger(TableRendererSorter.class);
    protected JTable table;
    private int[] indexes;
    protected Map<Integer, SortingType> columnsToSortingType;
    protected Map<Integer, Comparator<Object>> columnsToComparator;
    private Map<JTable, MouseListener> tableToHeaderListeners;


    public TableRendererSorter(JTable table) {
        this.table = table;

        indexes = new int[0];
        columnsToSortingType = new LinkedHashMap<Integer, SortingType>(table.getColumnCount());
        columnsToComparator = new HashMap<Integer, Comparator<Object>>();
        tableToHeaderListeners = new HashMap<JTable, MouseListener>(5);

        this.table.setColumnSelectionAllowed(false);

        setModel(this.table.getModel());
    }


    @Override
    public void setModel(TableModel model) {
        super.setModel(model);
        reallocateIndexes();
    }


    /**
     * Converti l'index trié en index du model origine.
     *
     * @param row l'index trie.
     *
     * @return l'index converti du model origine.
     */
    public int getConvertedIndex(int row) {
        if (row == -1 || row == Integer.MAX_VALUE) {
            return row;
        }
        return indexes[row];
    }


    /**
     * Converti l'index du model origine en index trié.
     *
     * @param row l'index du model.
     *
     * @return l'index trié.
     */
    public int getRealIndex(int row) {
        int realIdx = -1;
        for (int i = 0; i < indexes.length; i++) {
            if (indexes[i] == row) {
                realIdx = i;
            }
        }
        return realIdx;
    }


    @Override
    public Object getValueAt(int aRow, int aColumn) {
        checkModel();
        return getModel().getValueAt(indexes[aRow], aColumn);
    }


    @Override
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        checkModel();
        getModel().setValueAt(aValue, indexes[aRow], aColumn);
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return super.isCellEditable(indexes[row], column);
    }


    protected void mousePressedOnHeader(MouseEvent event) {
        TableColumnModel columnModel = table.getColumnModel();
        int viewColumn = columnModel.getColumnIndexAtX(event.getX());
        int modelColumn = table.convertColumnIndexToModel(viewColumn);
        if (event.getClickCount() > 1 && modelColumn != -1) {
            boolean singleSort = (event.getModifiers() & InputEvent.CTRL_MASK) == 0;
            if (singleSort) {
                SortingType sortingType = null;
                if (columnsToSortingType.containsKey(modelColumn)) {
                    sortingType = columnsToSortingType.get(modelColumn);
                }
                cleanSorting();
                if (null != sortingType) {
                    columnsToSortingType.put(modelColumn, sortingType);
                }
            }
            sortByColumn(modelColumn);
        }
    }


    public void sortByColumn(int column, boolean descending) {
        sortByColumn(column, descending ? SortingType.DESCENDING : SortingType.ASCENDING);
    }


    public void sortByColumn(int column) {
        SortingType sortingType = columnsToSortingType.get(column);
        sortByColumn(column, SortingType.next(sortingType));
    }


    void sortByColumn(int column, SortingType sortingType) {
        if (sortingType.equals(SortingType.NONE)) {
            columnsToSortingType.remove(column);
        }
        else {
            columnsToSortingType.put(column, sortingType);
        }
        sort();
        fireSuperTableChanged(new TableModelEvent(this));
    }


    public void sort() {
        checkModel();
        shuttlesort(indexes.clone(), indexes, 0, indexes.length);
    }


    private void shuttlesort(int[] from, int[] to, int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int pLow = low;
        int qMiddle = middle;

        if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0) {
            System.arraycopy(from, low, to, low, high - low);
            return;
        }

        // A normal merge.
        for (int i = low; i < high; i++) {
            if (qMiddle >= high
                || (pLow < middle && compare(from[pLow], from[qMiddle]) <= 0)) {
                to[i] = from[pLow++];
            }
            else {
                to[i] = from[qMiddle++];
            }
        }
    }


    public void cleanSorting() {
        if (table.getRowCount() == 0) {
            return;
        }

        columnsToSortingType.clear();
    }


    protected int compare(int row1, int row2) {
        return compareRows(row1, row2);
    }


    private int compareRows(int row1, int row2) {
        int comparaison = 0;
        Iterator<Integer> columnIterator = columnsToSortingType.keySet().iterator();
        while (comparaison == 0 && columnIterator.hasNext()) {
            int column = columnIterator.next();

            int columnView = table.convertColumnIndexToView(column);
            TableCellRenderer cellRenderer = table.getCellRenderer(row1, columnView);

            TableModel data = getModel();
            Object value1 = data.getValueAt(row1, column);
            Object value2 = data.getValueAt(row2, column);

            boolean sortableValue = isSortableValue(cellRenderer, value1, row1, column);

            Object o1 = getCellValue(row1, columnView, cellRenderer, value1, sortableValue);
            Object o2 = getCellValue(row2, columnView, cellRenderer, value2, sortableValue);

            Class type = data.getColumnClass(column);
            if (!sortableValue && type != Date.class) {
                type = String.class;
            }

            comparaison = manageOrder(columnsToSortingType.get(column),
                                      compareValues(column, o1, o2, type, value1, value2));
        }
        return comparaison;
    }


    protected boolean isSortableValue(TableCellRenderer cellRenderer, Object value, int row, int column) {
        return cellRenderer instanceof DefaultTableCellRenderer
               || cellRenderer instanceof CheckBoxRenderer;
    }


    private int compareValues(int column, Object o1, Object o2, Class type, Object value1, Object value2) {
        Comparator<Object> comparator = columnsToComparator.get(column);
        if (comparator != null) {
            return comparator.compare(o1, o2);
        }
        else {
            return compareValuesWithDefaultComparator(o1, o2, type, value1, value2);
        }
    }


    private int compareValuesWithDefaultComparator(Object o1,
                                                   Object o2,
                                                   Class type,
                                                   Object value1,
                                                   Object value2) {
        if (o1 == null || o2 == null) {
            return new NullComparator().compare(o1, o2);
        }
        else {
            if (type.getSuperclass() == Number.class || type == Number.class) {
                return new NumberComparator().compare((Number)value1, (Number)value2);
            }
            else if (type == Date.class) {
                return new DateComparator().compare((Date)value1, (Date)value2);
            }
            else if (type == String.class) {
                return new StringComparator().compare((String)o1, (String)o2);
            }
            else if (type == Boolean.class) {
                return new BooleanComparator().compare((Boolean)o1, (Boolean)o2);
            }
            else {
                return new WithToStringComparator().compare(o1, o2);
            }
        }
    }


    private int manageOrder(SortingType sortingType, int comparaison) {
        if (sortingType == SortingType.DESCENDING) {
            return -comparaison;
        }
        return comparaison;
    }


    private Object getCellValue(int row,
                                int col,
                                TableCellRenderer cellRenderer,
                                Object value,
                                boolean sortableValue) {
        Object cellValue;
        if (sortableValue) {
            return value;
        }
        else {
            cellValue = cellRenderer.getTableCellRendererComponent(table, value, false, false, row, col);
            if (cellValue instanceof JLabel) {
                return ((JLabel)cellValue).getText();
            }
            else {
                return cellValue;
            }
        }
    }


    private void fireSuperTableChanged(TableModelEvent event) {
        super.tableChanged(event);
    }


    @Override
    public void tableChanged(TableModelEvent event) {
        if (getModel().getRowCount() != indexes.length) {
            int[] oldIndexes = indexes;
            reallocateIndexes();

            if (event.getType() == TableModelEvent.INSERT) {
                keepGraphicalOrder(event, oldIndexes);
                cleanSorting();
            }
            else if (event.getType() == TableModelEvent.DELETE && isSorting()) {
                sort();
            }
        }

        if (isStructureChangedEvent(event)) {
            cleanSorting();
            reallocateIndexes();
        }
        else if (event.getType() == TableModelEvent.UPDATE) {
            if (onOneRow(event)) {
                if (event.getColumn() == TableModelEvent.ALL_COLUMNS
                    || columnsToSortingType.containsKey(event.getColumn())) {
                    cleanSorting();
                }
            }
            else if (isSorting()) {
                sort();
            }
        }

        table.getTableHeader().repaint();

        super.tableChanged(translateEvent(event));
    }


    private TableModelEvent translateEvent(TableModelEvent event) {
        if (isStructureChangedEvent(event)) {
            return new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        }
        else if (event.getType() == TableModelEvent.UPDATE
                 && onOneRow(event)) {
            return new TableModelEvent(this,
                                       getConvertedIndex(event.getFirstRow()),
                                       getConvertedIndex(event.getLastRow()),
                                       event.getColumn(),
                                       TableModelEvent.UPDATE);
        }
        else {
            return new TableModelEvent(this);
        }
    }


    private void keepGraphicalOrder(TableModelEvent event, int[] oldIndexes) {
        int insertedIndex = event.getFirstRow();
        int shift = 0;
        for (int i = 0; i < oldIndexes.length; i++) {
            if (oldIndexes[i] == insertedIndex) {
                indexes[i] = insertedIndex;
                indexes[i + 1] = insertedIndex + 1;
                shift = 1;
            }
            else if (oldIndexes[i] > insertedIndex) {
                indexes[i + shift] = oldIndexes[i] + 1;
            }
            else {
                indexes[i + shift] = oldIndexes[i];
            }
        }
    }


    private boolean isStructureChangedEvent(TableModelEvent event) {
        return event.getType() == TableModelEvent.UPDATE && event.getFirstRow() == TableModelEvent.HEADER_ROW;
    }


    private boolean onOneRow(TableModelEvent event) {
        return Math.abs(event.getLastRow() - event.getFirstRow()) == 0;
    }


    private void reallocateIndexes() {
        if (getModel() == null) {
            indexes = new int[0];
            return;
        }

        int rowCount = getModel().getRowCount();

        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];

        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
        checkModel();
    }


    private void checkModel() {
        if (indexes.length != getModel().getRowCount()) {
            APP.error("Sorter not informed of a change in model. : " + indexes.length
                      + " != " + getModel().getRowCount());
        }
    }


    // There is no-where else to put this.
    // Add a mouse listener to the Table to trigger a table sort
    // when a column heading is clicked in the JTable.
    public void addMouseListenerToHeaderInTable(JTable tbl) {
        if (tableToHeaderListeners.containsKey(tbl)) {
            return;
        }

        tbl.setColumnSelectionAllowed(false);

        // Add the mouse listener
        MouseAdapter headerMouseListener = new SorterMouseAdapter(this);
        JTableHeader th = tbl.getTableHeader();
        removeOldSorterMouseAdapter(th);
        th.addMouseListener(headerMouseListener);
        tableToHeaderListeners.put(tbl, headerMouseListener);
    }


    private void removeOldSorterMouseAdapter(JTableHeader th) {
        SorterMouseAdapter oldSorterMouseAdapter = null;
        for (int i = 0; i < th.getMouseListeners().length; i++) {
            MouseListener mouseListener = th.getMouseListeners()[i];
            if (mouseListener instanceof SorterMouseAdapter) {
                oldSorterMouseAdapter = (SorterMouseAdapter)mouseListener;
            }
        }
        if (oldSorterMouseAdapter != null) {
            th.removeMouseListener(oldSorterMouseAdapter);
        }
    }


    public void removeMouseListenerToHeaderInTable(JTable aTable) {
        MouseListener mouseListener = tableToHeaderListeners.get(aTable);
        if (mouseListener == null) {
            return;
        }
        tableToHeaderListeners.remove(aTable);
        aTable.getTableHeader().removeMouseListener(mouseListener);
    }


    public void changeHeaderRenderer(JTable tableToChange) {
        TableColumn tableColumn;
        int nbColumn = tableToChange.getColumnCount();
        for (int i = 0; i < nbColumn; i++) {
            tableColumn = tableToChange.getColumn(tableToChange.getColumnName(i));
            tableColumn.setHeaderRenderer(new TableSorterHeaderRenderer(this));
        }
    }


    int getSortingIndex(int columnModelIndex) {
        int sortingIndex = 0;
        for (Iterator<Integer> it = columnsToSortingType.keySet().iterator(); it.hasNext();
             sortingIndex++) {
            int column = it.next();
            if (columnModelIndex == column) {
                break;
            }
        }
        return sortingIndex;
    }


    boolean isSorting() {
        return !columnsToSortingType.isEmpty();
    }


    private static class SorterMouseAdapter extends MouseAdapter {
        private final TableRendererSorter sorter;


        private SorterMouseAdapter(TableRendererSorter sorter) {
            this.sorter = sorter;
        }


        @Override
        public void mousePressed(MouseEvent event) {
            sorter.mousePressedOnHeader(event);
        }
    }

    private class BooleanComparator implements Comparator<Boolean> {

        public int compare(Boolean o1, Boolean o2) {
            int result;
            if (o1 == o2) {
                result = 0;
            }
            else if (o1) {
                // Define false < true
                result = 1;
            }
            else {
                result = -1;
            }
            return result;
        }
    }

    private class WithToStringComparator implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            int result1;
            String s1 = o1.toString().toUpperCase();
            String s2 = o2.toString().toUpperCase();
            int result = s1.compareTo(s2);

            if (result < 0) {
                result1 = -1;
            }
            else if (result > 0) {
                result1 = 1;
            }
            else {
                result1 = 0;
            }
            return result1;
        }
    }

    private class StringComparator implements Comparator<String> {

        public int compare(String o1, String o2) {
            int result = o1.toUpperCase().compareTo(o2.toUpperCase());
            if (result < 0) {
                return -1;
            }
            else if (result > 0) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    private class NumberComparator implements Comparator<Number> {

        public int compare(Number o1, Number o2) {
            double d1 = o1.doubleValue();
            double d2 = o2.doubleValue();
            if (d1 < d2) {
                return -1;
            }
            else if (d1 > d2) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    private class DateComparator implements Comparator<Date> {

        public int compare(Date o1, Date o2) {
            if (o1 == null && o2 == null) {
                return 0;
            }
            else if (o1 == null) {
                return -1;
            }
            else if (o2 == null) {
                return 1;
            }

            long n1 = o1.getTime();
            long n2 = o2.getTime();
            if (n1 < n2) {
                return -1;
            }
            else if (n1 > n2) {
                return 1;
            }
            else {
                return 0;
            }
        }
    }

    private class NullComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            int result;
            if (o1 == null && o2 == null) {
                result = 0;
            }
            else if (o1 == null) {
                result = -1;
            }
            else {
                result = 1;
            }
            return result;
        }
    }

    enum SortingType {
        ASCENDING,
        DESCENDING,
        NONE;


        public static SortingType next(SortingType sortingType) {
            if (null == sortingType || SortingType.NONE.equals(sortingType)) {
                sortingType = SortingType.ASCENDING;
            }
            else if (SortingType.ASCENDING.equals(sortingType)) {
                sortingType = SortingType.DESCENDING;
            }
            else {
                sortingType = SortingType.NONE;
            }
            return sortingType;
        }
    }
}