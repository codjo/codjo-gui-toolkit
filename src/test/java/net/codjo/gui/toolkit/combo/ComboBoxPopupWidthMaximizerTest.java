package net.codjo.gui.toolkit.combo;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboPopup;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.interception.WindowInterceptor;
/**
 *
 */
public class ComboBoxPopupWidthMaximizerTest extends UISpecTestCase {
    private JComboBox comboBox = new JComboBox(new String[]{"www", "wwwwwwwwwwwwwwwwwwwwwwww"});


    public void test_comboWidth() throws Exception {
        ComboBoxPopupWidthMaximizer.install(comboBox, 150);

        showPopup();

        assertEquals(150, comboBox.getPreferredSize().width);
        assertEquals(243, getComboPopup().getPreferredSize().width);
    }


    public void test_minimalComboWidth() throws Exception {
        ComboBoxPopupWidthMaximizer.install(comboBox, 777);

        showPopup();

        assertEquals(777, comboBox.getPreferredSize().width);
        assertEquals(777, getComboPopup().getPreferredSize().width);
    }


    private void showPopup() {
        WindowInterceptor.run(new Trigger() {
            public void run() throws Exception {
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();
                frame.add(panel);
                panel.add(comboBox);
                frame.setVisible(true);
            }
        });

        comboBox.firePopupMenuWillBecomeVisible();
    }


    private BasicComboPopup getComboPopup() {
        return (BasicComboPopup)comboBox.getUI().getAccessibleChild(comboBox, 0);
    }
}
