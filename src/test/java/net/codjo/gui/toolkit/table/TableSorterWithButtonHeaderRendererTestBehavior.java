package net.codjo.gui.toolkit.table;
import net.codjo.test.release.task.gui.AbstractClickStep;
import net.codjo.test.release.task.gui.ClickTableHeaderStep;
import net.codjo.test.release.task.gui.GuiFindException;
import net.codjo.test.release.task.gui.TableTools;
import net.codjo.test.release.task.gui.metainfo.ClickTableHeaderDescriptor;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import junit.extensions.jfcunit.eventdata.MouseEventData;
/**
 * Classe de test de {@link TableRendererSorterWithButton}.
 */
public class TableSorterWithButtonHeaderRendererTestBehavior implements ClickTableHeaderDescriptor {

    public Component getComponentToClick(Component comp, AbstractClickStep step) {
        if (((ClickTableHeaderStep)step).componentIsPresent()) {
            Component cellRendererComponent = ((ClickTableHeaderStep)step).getTableHeaderComponent(comp);

            if (cellRendererComponent instanceof JPanel) {
                Component[] components = ((JPanel)cellRendererComponent).getComponents();
                for (Component child : components) {
                    if (child instanceof AbstractButton) {
                        return child;
                    }
                }
            }
        }
        return comp;
    }


    public void setReferencePointIfNeeded(MouseEventData eventData,
                                          Component component,
                                          AbstractClickStep step) {
        if (JTable.class.isInstance(component)) {
            JTable table = (JTable)component;
            int columnNumber = TableTools.searchColumn(table, step.getColumn());

            Rectangle cellRect = table.getTableHeader().getHeaderRect(columnNumber);

            if (((ClickTableHeaderStep)step).componentIsPresent()) {

                if (getComponentToClick(component, step) == null) {
                    throw new GuiFindException("Le bouton du header est introuvable.");
                }
                cellRect.x += table.getColumnModel().getColumn(columnNumber).getWidth() - 15;
            }
            cellRect.y = cellRect.y - cellRect.height / 2;

            eventData.setPosition(MouseEventData.CUSTOM);
            eventData.setReferencePoint(cellRect.getLocation());
        }
    }
}