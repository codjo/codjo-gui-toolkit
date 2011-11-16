package net.codjo.gui.toolkit.readonly;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
/**
 *
 */
public class JToggleButtonValueSetter implements ReadOnlyValueSetter {
    private boolean value;


    public JToggleButtonValueSetter(boolean value) {
        this.value = value;
    }


    public void setValue(JComponent component) {
        ((JToggleButton)component).setSelected(value);
    }
}
