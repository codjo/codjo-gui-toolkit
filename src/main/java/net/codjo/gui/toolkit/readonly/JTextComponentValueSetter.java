package net.codjo.gui.toolkit.readonly;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
/**
 *
 */
public class JTextComponentValueSetter implements ReadOnlyValueSetter {
    private String value;


    public JTextComponentValueSetter(String value) {
        this.value = value;
    }


    public void setValue(JComponent component) {
        ((JTextComponent)component).setText(value);
    }
}
