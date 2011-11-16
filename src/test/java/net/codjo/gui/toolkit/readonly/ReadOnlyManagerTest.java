/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.readonly;

import net.codjo.gui.toolkit.util.GuiUtil;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 *
 */
public class ReadOnlyManagerTest extends TestCase {
    private ReadOnlyManager manager;
    private JButton button;
    private JTextField textField;


    @Override
    protected void setUp() throws Exception {
        manager = new ReadOnlyManager(true, true);
        button = new JButton("readonly button");
        textField = new JTextField("readonly textfield");
    }


    public void test_addReadOnlyComponent() throws Exception {
        assertEnabled(button, true);
        assertEnabled(textField, true);

        manager.addReadOnlyComponent(button);
        manager.addReadOnlyComponent(textField);

        assertEnabled(button, false);
        assertEnabled(textField, false);

        button.setEnabled(true);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                GuiUtil.setTextComponentEditable(textField, true);
            }
        });

        assertEnabled(button, false);
        assertEnabled(textField, false);
        assertEquals("readonly textfield", textField.getText());
    }


    public void test_addReadOnlyComponentWithDefaultValue() throws Exception {
        JCheckBox selectedCheckBox = new JCheckBox();
        JCheckBox unselectedCheckBox = new JCheckBox();
        assertEnabled(button, true);
        assertEnabled(textField, true);
        assertEnabled(selectedCheckBox, true);
        assertEnabled(unselectedCheckBox, true);

        manager.addReadOnlyComponent(button);
        manager.addReadOnlyComponent(textField, new JTextComponentValueSetter("default value"));
        manager.addReadOnlyComponent(selectedCheckBox, new JToggleButtonValueSetter((true)));
        manager.addReadOnlyComponent(unselectedCheckBox, new JToggleButtonValueSetter((false)));

        assertEnabled(button, false);
        assertEnabled(textField, false);
        assertEnabled(selectedCheckBox, false);
        assertEnabled(unselectedCheckBox, false);
        assertEquals("default value", textField.getText());
        assertTrue(selectedCheckBox.isSelected());
        assertFalse(unselectedCheckBox.isSelected());

        selectedCheckBox.setSelected(false);
        textField.setText("Changed value");
        manager.setReadOnly(false, false);
        assertEnabled(button, true);
        assertEnabled(textField, true);
        assertEnabled(selectedCheckBox, true);
        assertEnabled(unselectedCheckBox, true);
        assertEquals("Changed value", textField.getText());
        assertFalse(selectedCheckBox.isSelected());
        assertFalse(unselectedCheckBox.isSelected());
    }


    public void test_setReadOnlyChange() throws Exception {
        assertEnabled(button, true);
        assertEnabled(textField, true);

        manager.addReadOnlyComponent(button);
        manager.addReadOnlyComponent(textField);

        assertEnabled(button, false);
        assertEnabled(textField, false);

        manager.setReadOnly(false);

        assertEnabled(button, true);
        assertEnabled(textField, true);
    }


    public void test_addReadOnlyContainer() throws Exception {
        ReadOnlyContainerMock mock = new ReadOnlyContainerMock();

        assertEnabled(button, true);
        assertEnabled(textField, true);

        manager.addReadOnlyContainer(mock);

        assertEnabled(button, false);
        assertEnabled(textField, false);
    }


    public void test_nonReadOnlyMode() throws Exception {
        manager = new ReadOnlyManager(false, true);
        manager.addReadOnlyComponent(button);

        assertEnabled(button, true);

        button.setEnabled(false);

        assertEnabled(button, false);
    }


    public void test_linkReadOnlyManagers() throws Exception {
        manager = new ReadOnlyManager(false);

        ReadOnlyManager subManager1 = new ReadOnlyManager(false);
        subManager1.addReadOnlyComponent(button);

        ReadOnlyManager subManager2 = new ReadOnlyManager(false);
        subManager2.addReadOnlyComponent(textField);

        manager.addSubReadOnlyManager(subManager1);
        manager.addSubReadOnlyManager(subManager2);

        assertEnabled(button, true);
        assertEnabled(textField, true);

        manager.setReadOnly(true);

        assertEnabled(button, false);
        assertEnabled(textField, false);
    }


    private void assertEnabled(final JComponent component, final boolean expected)
          throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                long begin = System.currentTimeMillis();
                while (System.currentTimeMillis() - begin < 2000L) {
                    try {
                        assertEquals(expected, component.isEnabled());
                        return;
                    }
                    catch (AssertionFailedError error) {
                        ;
                    }
                }
            }
        });
        assertEquals(expected, component.isEnabled());
    }


    private class ReadOnlyContainerMock implements ReadOnlyComponentsContainer {
        public void addReadOnlyComponents(ReadOnlyManager readOnlyManager) {
            readOnlyManager.addReadOnlyComponent(button);
            readOnlyManager.addReadOnlyComponent(textField);
        }
    }
}
