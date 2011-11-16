package net.codjo.gui.toolkit.text;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.uispec4j.Key;
import org.uispec4j.ListBox;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

public class JTextFieldCodeLabelAutoCompleterTest extends UISpecTestCase {
    JFrame frame = new JFrame("Frame de test");
    JTextField fieldText = new JTextField();
    Window window;
    TextBox textBox = new TextBox(fieldText);
    private JTextFieldCodeLabelAutoCompleter codeLabelAutoCompleter;


    public void testRestrictedListDisplay() {
        setCharacter(Key.F);
        assertEquals("F", textBox.getText());

        JPopupMenu popupMenu = (JPopupMenu)searchComponent(JPopupMenu.class, "restrictedList");
        JScrollPane scrollPane = (JScrollPane)searchComponent(JScrollPane.class, "scrollPanel");

        JList listToDisplay = (JList)scrollPane.getViewport().getView();
        assertEquals(4, listToDisplay.getModel().getSize());
        assertTrue(popupMenu.isVisible());

        setCharacter(Key.R);
        assertEquals(3, listToDisplay.getModel().getSize());

        setCharacter(Key.DELETE);
        assertEquals(7, listToDisplay.getModel().getSize());
    }


    public void testUpdatedListDisplay() {
        WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {
                textBox.pressKey(Key.F);
            }
        }).process(new WindowHandler() {
            @Override
            public Trigger process(Window window) throws Exception {
                assertEquals("F", textBox.getText());
                assertAutoCompleteList(window, 4,
                                       new String[]{"FR0000000001", "FR0000000003", "FR1000000004", "FU0000000002"});

                Map<String, String> newValues = new HashMap<String, String>();
                newValues.put("F1", "FR1");
                codeLabelAutoCompleter.updateAutoCompletedValues(newValues);
                WindowInterceptor.init(new Trigger() {
                    public void run() throws Exception {
                        textBox.pressKey(Key.R);
                    }
                }).process(new WindowHandler() {
                    @Override
                    public Trigger process(Window window) throws Exception {
                        assertEquals("FR", textBox.getText());
                        assertAutoCompleteList(window, 1, new String[]{"FR1"});

                        window.getListBox().selectIndex(0);
                        textBox.pressKey(Key.ENTER);
                        assertEquals("FR1", textBox.getText());

                        return Trigger.DO_NOTHING;
                    }
                }).run();

                return Trigger.DO_NOTHING;
            }
        }).run();
    }


    public void testSelectedValue() {
        WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {
                textBox.pressKey(Key.F);
            }
        }).process(new WindowHandler() {
            @Override
            public Trigger process(Window window) throws Exception {
                assertEquals("F", textBox.getText());
                assertAutoCompleteList(window, 4,
                                       new String[]{"FR0000000001", "FR0000000003", "FR1000000004", "FU0000000002"});

                WindowInterceptor.init(new Trigger() {
                    public void run() throws Exception {
                        textBox.pressKey(Key.R);
                    }
                }).process(new WindowHandler() {
                    @Override
                    public Trigger process(Window window) throws Exception {
                        assertEquals("FR", textBox.getText());
                        assertAutoCompleteList(window, 3,
                                               new String[]{"FR0000000001", "FR0000000003", "FR1000000004"});

                        window.getListBox().selectIndex(2);
                        textBox.pressKey(Key.ENTER);
                        assertEquals("FR1000000004", textBox.getText());

                        assertEquals("FR14", codeLabelAutoCompleter.getSelectedCode());

                        return Trigger.DO_NOTHING;
                    }
                }).run();

                return Trigger.DO_NOTHING;
            }
        }).run();
    }


    public void test_forceCode() throws Exception {
        assertEquals("", textBox.getText());
        codeLabelAutoCompleter.setCode("FU2");
        assertEquals("FU0000000002", textBox.getText());

        codeLabelAutoCompleter.setCode("unknownCode");
        assertEquals("", textBox.getText());

        codeLabelAutoCompleter.setCode("FR14");
        assertEquals("FR1000000004", textBox.getText());
    }


    public void test_sameLabelsForDifferentCodes() throws Exception {
        WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {
                textBox.pressKey(Key.L);
            }
        }).process(new WindowHandler() {
            @Override
            public Trigger process(Window window) throws Exception {
                assertEquals("L", textBox.getText());
                assertAutoCompleteList(window, 3, new String[]{"LABEL", "LABEL", "LABEL"});

                window.getListBox().selectIndex(0);
                textBox.pressKey(Key.ENTER);
                assertEquals("LABEL", textBox.getText());

                assertEquals("CODE1", codeLabelAutoCompleter.getSelectedCode());

                codeLabelAutoCompleter.resetText();

                WindowInterceptor.init(new Trigger() {
                    public void run() throws Exception {
                        textBox.pressKey(Key.L);
                    }
                }).process(new WindowHandler() {
                    @Override
                    public Trigger process(Window window) throws Exception {
                        assertEquals("L", textBox.getText());
                        assertAutoCompleteList(window, 3, new String[]{"LABEL", "LABEL", "LABEL"});

                        window.getListBox().selectIndex(1);
                        textBox.pressKey(Key.ENTER);
                        assertEquals("LABEL", textBox.getText());

                        assertEquals("CODE2", codeLabelAutoCompleter.getSelectedCode());

                        codeLabelAutoCompleter.resetText();

                        WindowInterceptor.init(new Trigger() {
                            public void run() throws Exception {
                                textBox.pressKey(Key.L);
                            }
                        }).process(new WindowHandler() {
                            @Override
                            public Trigger process(Window window) throws Exception {
                                assertEquals("L", textBox.getText());
                                assertAutoCompleteList(window, 3, new String[]{"LABEL", "LABEL", "LABEL"});

                                window.getListBox().selectIndex(2);
                                textBox.pressKey(Key.ENTER);
                                assertEquals("LABEL", textBox.getText());

                                assertEquals("CODE3", codeLabelAutoCompleter.getSelectedCode());

                                codeLabelAutoCompleter.resetText();

                                return Trigger.DO_NOTHING;
                            }
                        }).run();

                        return Trigger.DO_NOTHING;
                    }
                }).run();

                return Trigger.DO_NOTHING;
            }
        }).run();
    }


    private void assertAutoCompleteList(Window selectedWindow, int listSize, String[] listElements) {
        ListBox listBox = selectedWindow.getListBox();
        assertEquals(listSize, listBox.getSize());
        assertTrue(listBox.contentEquals(listElements));
    }


    private JComponent searchComponent(Class swingComponentClass, String componentName) {
        assertTrue(window.containsSwingComponent(swingComponentClass));
        JComponent component = (JComponent)window.findSwingComponent(swingComponentClass, componentName);
        assertNotNull(component);
        return component;
    }


    private void setCharacter(final Key character) {
        window = WindowInterceptor.run(new Trigger() {
            public void run() throws Exception {
                textBox.pressKey(character);
            }
        });
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame internal = new JInternalFrame("Frame interne de test");
        desktop.add(internal);
        internal.setVisible(true);
        internal.add(fieldText);
        Map<String, String> codeLabels = new LinkedHashMap<String, String>();
        codeLabels.put("FR1", "FR0000000001");
        codeLabels.put("FU2", "FU0000000002");
        codeLabels.put("FR3", "FR0000000003");
        codeLabels.put("FR14", "FR1000000004");
        codeLabels.put("CODE1", "LABEL");
        codeLabels.put("CODE2", "LABEL");
        codeLabels.put("CODE3", "LABEL");
        codeLabelAutoCompleter = new JTextFieldCodeLabelAutoCompleter(fieldText, codeLabels);

        frame.add(desktop);
        frame.setSize(400, 200);
        frame.pack();

        window = WindowInterceptor.run(new Trigger() {
            public void run() throws Exception {
                frame.setVisible(true);
            }
        });
    }
}
