package net.codjo.gui.toolkit;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.junit.Test;

public class LabelledItemPanelTest {

    private static Component getComponent(JPanel panel, String name) {
        if (name == null) {
            return null;
        }
        for (Component component : panel.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }


    @Test
    public void test_componentNames() throws Exception {
        LabelledItemPanel labelledItemPanel = new LabelledItemPanel();
        JTextField textField = new JTextField();
        textField.setName("textField");
        labelledItemPanel.addItem("monTextField", textField);
        Component myComponent = getComponent(labelledItemPanel, "textField.label");
        assertThat(myComponent, notNullValue());
        assertThat(myComponent instanceof JLabel, equalTo(true));
        assertThat(((JLabel)myComponent).getText(), equalTo("monTextField"));
    }
}
