package net.codjo.gui.toolkit.text;
import org.uispec4j.TextBox;
import org.uispec4j.UISpecTestCase;

public class TextFieldTest extends UISpecTestCase {
    protected TextBox textBox;
    protected TextField textField;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        textField = new TextField();
        textField.setMaxTextLength(6);
        textBox = new TextBox(textField);
    }


    public void test_maxTextLength_emptyTextField() throws Exception {
        assertMaxTextLength("123456", "1234567", 0);
    }


    public void test_maxTextLength_notEmptyTextField() throws Exception {
        textBox.insertText("123", 0);
        assertMaxTextLength("145623", "4567", 1);
    }


    public void test_maxTextLength_notSet() throws Exception {
        textField.setMaxTextLength(-1);
        assertMaxTextLength("1234567890", "1234567890", 0);
    }


    private void assertMaxTextLength(String expected, String textToInsert, int textOffset) {
        textBox.insertText(textToInsert, textOffset);

        assertEquals(expected, textBox.getText());
    }
}
