package net.codjo.gui.toolkit.button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import static junit.extensions.jfcunit.TestHelper.cleanUp;
import static junit.extensions.jfcunit.TestHelper.disposeWindow;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HyperLinkTest extends JFCTestCase {
    private JFrame jFrame;
    private HyperLink linkLabel;


    public void testActionPerformed() {
        ActionListener actionListener = mock(ActionListener.class);
        linkLabel.addActionListener(actionListener);

        getHelper().enterClickAndLeave(new MouseEventData(this, linkLabel, 1));

        verify(actionListener, times(1)).actionPerformed(any(ActionEvent.class));

        linkLabel.removeActionListener(actionListener);
        getHelper().enterClickAndLeave(new MouseEventData(this, linkLabel, 1));

        verify(actionListener, times(1)).actionPerformed(any(ActionEvent.class));

        linkLabel.addActionListener(actionListener);
        linkLabel.addActionListener(actionListener);
        getHelper().enterClickAndLeave(new MouseEventData(this, linkLabel, 1));

        verify(actionListener, times(3)).actionPerformed(any(ActionEvent.class));

        linkLabel.removeAllActionListeners();
        getHelper().enterClickAndLeave(new MouseEventData(this, linkLabel, 1));

        verify(actionListener, times(3)).actionPerformed(any(ActionEvent.class));
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setHelper(new JFCTestHelper());
        jFrame = new JFrame();
        linkLabel = new HyperLink("click on me");
        jFrame.setContentPane(linkLabel);
        jFrame.pack();
        jFrame.setVisible(true);
        flushAWT();
    }


    @Override
    protected void tearDown() throws Exception {
        disposeWindow(jFrame, this);
        cleanUp(this);
        super.tearDown();
    }
}
