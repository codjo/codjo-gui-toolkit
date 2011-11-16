package net.codjo.gui.toolkit.waiting;
import net.codjo.test.common.LogString;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.uispec4j.Button;
import org.uispec4j.UISpecTestCase;
/**
 *
 */
public class WaitingPanelTest extends UISpecTestCase {
    private WaitingPanel waitingPanel;
    private JFrame frame = new JFrame();
    private JButton executeButton = new JButton();
    private JTextField textField = new JTextField();
    private LogString log = new LogString();


    public void test_noWaitingPanelDisplayed() throws Exception {
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                waitingPanel.exec(new Runnable() {
                    public void run() {
                        textField.setText("Don Diego");
                    }
                });
            }
        });

        final Button button = new Button(executeButton);
        button.click();

        assertFalse(log.getContent().contains("startAnimation"));
        assertEquals("Don Diego", textField.getText());
    }


    public void test_waitingPanelDisplayed() throws Exception {
        executeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                waitingPanel.exec(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        textField.setText("Zorro");
                    }
                });
            }
        });

        Button button = new Button(executeButton);
        button.click();

        Thread.sleep(3000);

        assertTrue(log.getContent().contains("startAnimation"));
        assertEquals("Zorro", textField.getText());
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        waitingPanel = new WaitingPanelMock(log);
        frame.setGlassPane(waitingPanel);
        frame.add(executeButton);
        log.clear();
    }


    private class WaitingPanelMock extends WaitingPanel {
        private LogString log;


        private WaitingPanelMock(LogString log) {
            this.log = log;
        }


        @Override
        public void startAnimation() {
            log.call("startAnimation");
            super.startAnimation();
        }
    }
}
