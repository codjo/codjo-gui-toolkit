/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.number;
import net.codjo.gui.toolkit.AbstractJFCTestCase;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.StringEventData;
/**
 * Classe de test de {@link PercentageField}
 *
 * @author $Author: popov $
 * @version $Revision: 1.7 $
 */
public class PercentageFieldTest extends AbstractJFCTestCase {
    private JFrame window = null;
    private PercentageField field;
    private JTextField otherField;


    public static void main(String[] args) {
        JFrame wnd = new JFrame("Test à la mimine");
        PercentageField fld = new PercentageField(6);
        fld.setColumns(10);
        wnd.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        wnd.getContentPane().add(new JLabel("un label"));
        wnd.getContentPane().add(fld);
        wnd.getContentPane().add(new JLabel("un autre label"));
        wnd.getContentPane().add(new JTextField("another field"));
        wnd.setSize(400, 150);
        wnd.setVisible(true);
        wnd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }


    /**
     * Test le filtre de saisie sur la valeur max possible.
     *
     * @throws Exception
     */
    public void test_gui_badValue() throws Exception {
        getHelper().sendString(new StringEventData(this, field, "100"));
        assertEquals("1.00", field.getNumber().toString());

        field.setText("");
        getHelper().sendString(new StringEventData(this, field, "99.99"));
        assertEquals("0.9999", field.getNumber().toString());

        field.setText("");
        getHelper().sendString(new StringEventData(this, field, "101"));
        assertEquals(null, field.getNumber());

        field.setText("");
        getHelper().sendString(new StringEventData(this, field, "99.999"));
        assertEquals(null, field.getNumber());
    }


    /**
     * Test que l'utilisateur saisie un pourcentage.
     *
     * @throws Exception
     */
    public void test_gui_percentage() throws Exception {
        getHelper().sendString(new StringEventData(this, field, "50.54"));
        assertEquals("50.54", field.getText());

        // Mais le nb est un pourcentage.
        assertEquals("0.5054", field.getNumber().toString());
    }


    /**
     * Test la modification du champs par programme.
     *
     * @throws Exception
     */
    public void test_prg_setNumber() throws Exception {
        //-------- Changement par setNumber --------
        field.setNumber(new BigDecimal("0.5"));

        assertEquals("0.5", field.getNumber().toString());
        assertEquals("50", field.getText());

        setFocus(otherField);

        assertEquals("50,00 %", field.getText());
    }


    /**
     * Test la modification du champs par programme avec nb de digit spécifié.
     *
     * @throws Exception
     */
    public void test_prg_setNumber_digit() throws Exception {
        TestHelper.disposeWindow(window, this);
        field = new PercentageField(9);
        initWindowForTest();
        //-------- Changement par setNumber digit --------
        field.setNumber(new BigDecimal("0.5"));

        assertEquals("0.5", field.getNumber().toString());
        assertEquals("50", field.getText());

        setFocus(otherField);

        assertEquals("50,0000000 %", field.getText());
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setHelper(new JFCTestHelper());
        field = new PercentageField();
        initWindowForTest();
    }


    @Override
    protected void tearDown() throws Exception {
        TestHelper.disposeWindow(window, this);
        TestHelper.cleanUp(this);
        window = null;
        super.tearDown();
    }


    private void initWindowForTest() {
        buildWindow();
        setFocus(field);
    }


    private void buildWindow() {
        window = new JFrame("Test " + getName());
        otherField = new JTextField();
        window.getContentPane().add(otherField, BorderLayout.NORTH);
        window.getContentPane().add(field, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
    }
}
