package net.codjo.gui.toolkit.text;
import java.util.ArrayList;
import java.util.List;
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

public class JTextFieldAutoCompleterTest extends UISpecTestCase {
    JFrame frame = new JFrame("Frame de test");
    JTextField fieldText = new JTextField();
    Window window;
    TextBox textBox = new TextBox(fieldText);
    private JTextFieldAutoCompleter textFieldAutoCompleter;


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
        assertEquals(4, listToDisplay.getModel().getSize());
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

                List<String> list = new ArrayList<String>();
                list.add("FR1");
                textFieldAutoCompleter.updateAutoCompletedList(list);
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
        List<String> list = new ArrayList<String>();
        list.add("FR0000000001");
        list.add("FU0000000002");
        list.add("FR0000000003");
        list.add("FR1000000004");
        textFieldAutoCompleter = new JTextFieldAutoCompleter(fieldText, list);

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
