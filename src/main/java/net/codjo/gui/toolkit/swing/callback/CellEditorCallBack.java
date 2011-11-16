package net.codjo.gui.toolkit.swing.callback;
import javax.swing.CellEditor;
/**
 *
 */
public class CellEditorCallBack implements CallBack {
    private CellEditor cellEditor;


    public CellEditorCallBack(CellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }


    public void notifyOk() {
        cellEditor.stopCellEditing();
    }


    public void notifyCancel() {
        cellEditor.cancelCellEditing();
    }
}
