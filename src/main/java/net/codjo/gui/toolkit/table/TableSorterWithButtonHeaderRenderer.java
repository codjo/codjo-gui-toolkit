package net.codjo.gui.toolkit.table;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTable;
/**
 *
 */
class TableSorterWithButtonHeaderRenderer extends TableSorterHeaderRenderer {
    private final AbstractButton button;
    private final int buttonColumn;


    protected TableSorterWithButtonHeaderRenderer(TableRendererSorterWithButton model) {
        super(model);
        this.button = model.getButton();
        this.buttonColumn = model.getButtonColumn();
    }


    public AbstractButton getButton() {
        return button;
    }


    @Override
    public Component getTableCellRendererComponent(JTable tbl, Object value,
                                                   boolean selected, boolean hasFocus, int row, int col) {

        Component label = super.getTableCellRendererComponent(tbl, value, false, false, -1, col);
        if (buttonColumn == tbl.convertColumnIndexToModel(col)) {
            JPanel panelRenderer = new JPanel(new BorderLayout());
            panelRenderer.add(label, BorderLayout.CENTER);
            panelRenderer.add(button, BorderLayout.EAST);
            return panelRenderer;
        }
        return label;
    }
}
