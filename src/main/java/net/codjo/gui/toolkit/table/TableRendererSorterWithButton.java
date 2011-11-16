package net.codjo.gui.toolkit.table;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Sert à trier les colonnes.
 */
public class TableRendererSorterWithButton extends TableRendererSorter {
    private final AbstractButton button;
    private final int buttonColumn;


    public TableRendererSorterWithButton(JTable theTable, AbstractButton button) {
        this(theTable, button, 0);
    }


    public TableRendererSorterWithButton(JTable theTable, AbstractButton button, int buttonColumn) {
        super(theTable);
        this.button = button;
        this.buttonColumn = buttonColumn;

        initButtonListener();
    }


    public int getButtonColumn() {
        return buttonColumn;
    }


    public AbstractButton getButton() {
        return button;
    }


    private void initButtonListener() {
        button.addChangeListener(new RendererRepaintChangeListener());
    }


    @Override
    public void changeHeaderRenderer(JTable tableToChange) {
        TableColumn tableColumn;
        int nbColumn = tableToChange.getColumnCount();
        for (int i = 0; i < nbColumn; i++) {
            tableColumn = tableToChange.getColumn(getColumnName(i));
            tableColumn.setHeaderRenderer(new TableSorterWithButtonHeaderRenderer(this));
        }
    }


    @Override
    protected void mousePressedOnHeader(MouseEvent event) {
        TableColumnModel columnModel = table.getColumnModel();
        int viewIndex = columnModel.getColumnIndexAtX(event.getX());
        int modelIndex = table.convertColumnIndexToModel(viewIndex);
        TableColumn tableColumn = columnModel.getColumn(modelIndex);
        TableSorterWithButtonHeaderRenderer renderer =
              (TableSorterWithButtonHeaderRenderer)tableColumn.getHeaderRenderer();
        Rectangle rectangle = table.getTableHeader().getHeaderRect(viewIndex);

        Point p2 = new Point(event.getX() - rectangle.x, event.getY() - rectangle.y);

        if (renderer.getButton() != null) {
            p2 = javax.swing.SwingUtilities.convertPoint(renderer, p2, renderer.getButton());
            if (renderer.getButton().contains(p2) && renderer.getButton().isEnabled()) {
                renderer.getButton().doClick();
            }
        }
        else if (event.getClickCount() > 1 && modelIndex != -1) {
            super.mousePressedOnHeader(event);
        }
    }


    private class RendererRepaintChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent event) {
            table.getTableHeader().repaint();
        }
    }
}
