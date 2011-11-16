package net.codjo.gui.toolkit.table;
import net.codjo.test.common.LogString;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
/**
 *
 */
class TableModelLogger implements TableModelListener {
    private final LogString logString;


    TableModelLogger(LogString logString) {
        this.logString = logString;
    }


    public void tableChanged(TableModelEvent event) {
        logString.call("tableChanged", typeToString(event.getType())
                                       + "(" + event.getSource().getClass().getSimpleName() + ")"
                                       + " column[" + toString(event.getColumn()) + "]"
                                       + " row[" + rowToString(event) + "]"
        );
    }


    private String rowToString(TableModelEvent event) {
        if (event.getLastRow() == event.getFirstRow()) {
            return toString(event.getFirstRow());
        }
        return event.getFirstRow() + "/" + toString(event.getLastRow());
    }


    private String toString(int index) {
        if (index == -1) {
            return "ALL";
        }
        return (index == Integer.MAX_VALUE ? "MAX" : Integer.toString(index));
    }


    private String typeToString(int type) {
        switch (type) {
            case TableModelEvent.INSERT:
                return "insert";
            case TableModelEvent.UPDATE:
                return "update";
            case TableModelEvent.DELETE:
                return "delete";
            default:
                return "type inconnu";
        }
    }
}
