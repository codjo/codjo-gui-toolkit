package net.codjo.gui.toolkit.text;
import javax.swing.JTextField;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

public class DocumentWithMaxSizeServiceTest extends UISpecTestCase {

    public void test_install() throws Exception {
        JTextField textField = new JTextField();
        DocumentWithMaxSizeService.install(textField, 6);
        TextBox textBox = new TextBox(textField);

        textBox.setText("1234567");
        assertEquals("123456", textBox.getText());

        textBox.setText(null);
        assertEquals("", textBox.getText());
    }
}
