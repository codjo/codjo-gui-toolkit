package net.codjo.gui.toolkit.util;
import net.codjo.gui.toolkit.AbstractJFCTestCase;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.FocusManager;
import javax.swing.JFrame;
import javax.swing.JTextField;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;

public class ModalTest extends AbstractJFCTestCase {
    private JFrame parentFrame;
    private JFrame childFrame;


    public void test_frame_childAlwaysFront() throws Exception {
        parentFrame.toFront();
        setFocus(parentFrame);

        Modal.applyModality(parentFrame, childFrame);

        assertIsActive(childFrame);

        tryToGrabFocus(parentFrame);

        assertIsActive(childFrame);
    }


    public void test_frame_childKeepFocus() throws Exception {
        Modal.applyModality(parentFrame, childFrame);

        setFocus(getField(childFrame));

        tryToGrabFocus(parentFrame);

        assertTrue(getField(childFrame).hasFocus());
    }


    public void test_frame_childKeepFocusWithThirdFrame() throws Exception {
        JFrame thirdFrame = buildFrame("third");
        Modal.applyModality(parentFrame, childFrame);

        tryToGrabFocus(childFrame);
        tryToGrabFocus(thirdFrame);

        assertTrue(getField(thirdFrame).hasFocus());

        TestHelper.disposeWindow(thirdFrame, this);
    }


    public void test_frame_parentCannotBeClosed() throws Exception {
        Modal.applyModality(parentFrame, childFrame);

        closeFrame(parentFrame);

        assertTrue(parentFrame.isShowing());
    }


    public void test_frame_modalityCanBeRemoved() throws Exception {
        Modal.applyModality(parentFrame, childFrame);

        closeFrame(childFrame);

        assertFalse(childFrame.isShowing());
        assertIsActive(parentFrame);

        closeFrame(parentFrame);

        assertFalse(parentFrame.isShowing());
    }


    @Override
    protected void setUp() {
        setHelper(new JFCTestHelper());
        parentFrame = buildFrame("parent");
        childFrame = buildFrame("child");
    }


    @Override
    protected void tearDown() throws Exception {
        TestHelper.disposeWindow(parentFrame, this);
        TestHelper.disposeWindow(childFrame, this);
        TestHelper.cleanUp(this);
    }


    private JFrame buildFrame(String title) {
        JFrame window = new JFrame(title);
        window.setName(title);
        window.getContentPane().add(new JTextField(5));
        window.pack();
        window.setVisible(true);
        return window;
    }


    private void assertIsActive(JFrame frame) {
        assertEquals(frame, FocusManager.getCurrentManager().getActiveWindow());
    }


    private JTextField getField(JFrame frame) {
        return (JTextField)frame.getContentPane().getComponent(0);
    }


    private void tryToGrabFocus(JFrame frame) {
        frame.toFront();
        getField(frame).requestFocusInWindow();
        flushAWT();
    }


    private void closeFrame(JFrame frame) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
              .postEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING, 0, 0));
        flushAWT();
    }
}
