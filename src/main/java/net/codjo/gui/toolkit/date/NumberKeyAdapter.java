package net.codjo.gui.toolkit.date;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
/**
 *
 */
public class NumberKeyAdapter extends KeyAdapter {
    private final JTextField field;
    private final int maxLength;


    public NumberKeyAdapter(JTextField field, int maxLength) {
        this.field = field;
        this.maxLength = maxLength;
    }


    @Override
    public void keyTyped(KeyEvent evt) {
        if ((evt.getKeyChar() != KeyEvent.VK_BACK_SPACE
             && evt.getKeyCode() != KeyEvent.VK_DELETE)) {
            if (field.getText().length() >= maxLength
                && (field.getSelectedText() == null
                    || field.getSelectedText().length() == 0)) {
                evt.consume();
            }
            else if (evt.getKeyChar() < '0' || evt.getKeyChar() > '9') {
                evt.consume();
            }
        }
    }
}
