package net.codjo.gui.toolkit.text;
import javax.swing.JTextField;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class DocumentWithMaxSizeTest extends UISpecTestCase {
    private JTextField textField = new JTextField();
    private DocumentWithMaxSize document = new DocumentWithMaxSize(10);


    public void test_insertString() throws Exception {
        textField.setDocument(document);

        textField.setText("this is too long");
        assertEquals("", textField.getText());

        textField.setText("this is ok");
        assertEquals("this is ok", textField.getText());
    }
}
