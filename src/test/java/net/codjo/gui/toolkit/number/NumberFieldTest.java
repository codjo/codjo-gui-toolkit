package net.codjo.gui.toolkit.number;
import net.codjo.gui.toolkit.AbstractJFCTestCase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;

public class NumberFieldTest extends AbstractJFCTestCase {
    private static final String TOOLTIP_HEADER = "<html><b>Erreur de saisie !</b><br><i>";
    private JFrame window = null;
    private NumberField field;
    private MockPropertyChangeListener mockPropChangeListener;
    private JTextField otherField;


    public static void main(String[] args) {
        JFrame wnd = new JFrame("Test à la mimine");
        final NumberField fld = new NumberField();
        fld.setApplyRendererInEditMode(true);
        fld.setMaximumFractionDigits(3);
        fld.setMaximumIntegerDigits(2);
        fld.setMinValue(-2);
        fld.setMaxValue(120000);
        fld.setColumns(10);
        fld.setRenderer(new NumberFieldRenderer(new DecimalFormat("#,###.000 '%'"),
                                                fld.getFont().deriveFont(Font.ITALIC), Color.lightGray,
                                                Color.blue,
                                                BorderFactory.createRaisedBevelBorder()));
        wnd.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        wnd.getContentPane().add(new JLabel("un label"));
        wnd.getContentPane().add(fld);
        wnd.getContentPane().add(new JLabel("un autre label"));
        wnd.getContentPane().add(new JTextField("another field"));

        NumberField field2 = new NumberField();
        field2.setNumber(1234.56);
        field2.setThousandsSeparatorOnRenderer(' ');
        field2.setEditable(false);
        wnd.getContentPane().add(new JLabel("NumberField non éditable :"));
        wnd.getContentPane().add(field2);

        wnd.setSize(400, 150);
        wnd.setVisible(true);
        wnd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });
        JButton button = new JButton();
        button.setText("BIP");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fld.setNumber(12);
            }
        });
        wnd.getContentPane().add(button);
        wnd.pack();
    }


    /**
     * Test la gestion des evenements Property.
     */
    public void test_event_PropertyChangeListener() {
        field.addPropertyChangeListener(NumberField.NUMBER_PROPERTY, mockPropChangeListener);

        // -------- Changement par setText --------
        field.setText("1123.5");
        assertEquals(1, mockPropChangeListener.propChangeCalledTimes);
        assertEquals(NumberField.NUMBER_PROPERTY, mockPropChangeListener.lastReceivedEvent.getPropertyName());
        assertEquals(null, mockPropChangeListener.lastReceivedEvent.getOldValue());
        assertEquals("1123.5", mockPropChangeListener.lastReceivedEvent.getNewValue().toString());

        // -------- Changement par setNumber --------
        field.setNumber(new BigDecimal("1234567890123456789.1234567890123456789"));
        assertEquals(2, mockPropChangeListener.propChangeCalledTimes);

        // -------- Changement par setNumber --------
        field.setNumber(new BigDecimal("-3.5"));
        assertEquals(3, mockPropChangeListener.propChangeCalledTimes);
    }


    /**
     * Test la gestion des evenements Property lors d'une erreur de saisie.
     */
    public void test_event_PropertyChangeListener_badinput() {
        field.addPropertyChangeListener(NumberField.NUMBER_PROPERTY, mockPropChangeListener);
        field.setMinValue(2);

        field.setText("1");
        assertEquals("1", field.getText());
        assertEquals(0, mockPropChangeListener.propChangeCalledTimes);

        field.setCaretPosition(1);
        getHelper().sendString(new StringEventData(this, field, "4"));
        assertEquals("14", field.getText());
        assertEquals(1, mockPropChangeListener.propChangeCalledTimes);
        assertEquals(NumberField.NUMBER_PROPERTY, mockPropChangeListener.lastReceivedEvent.getPropertyName());
        assertEquals(null, mockPropChangeListener.lastReceivedEvent.getOldValue());
        assertEquals("14", mockPropChangeListener.lastReceivedEvent.getNewValue().toString());

        getHelper().sendKeyAction(new KeyEventData(this, field, KeyEvent.VK_BACK_SPACE));
        assertEquals("1", field.getText());
        assertEquals(2, mockPropChangeListener.propChangeCalledTimes);
        assertEquals(NumberField.NUMBER_PROPERTY, mockPropChangeListener.lastReceivedEvent.getPropertyName());
        assertEquals("14", mockPropChangeListener.lastReceivedEvent.getOldValue().toString());
        assertEquals(null, mockPropChangeListener.lastReceivedEvent.getNewValue());
    }


    /**
     * Test le cas particulier de la saisie d'un point en premier.
     */
    public void test_gui_beginWithDot() {
        getHelper().sendString(new StringEventData(this, field, "."));
        assertEquals("0.", field.getText());
        assertEquals("0", field.getNumber().toString());
    }


    /**
     * Test le filtre de saisie sur le nombre de décimale.
     */
    public void test_gui_maxFractionDigit() {
        field.setMaximumFractionDigits(2);
        field.setForeground(Color.blue);
        field.setToolTipText("prevTooltip");

        getHelper().sendString(new StringEventData(this, field, "1.23"));
        assertEquals("1.23", field.getNumber().toString());

        getHelper().sendString(new StringEventData(this, field, "4"));
        assertEquals("1.234", field.getText());
        assertEquals(null, field.getNumber());
        assertEquals(TOOLTIP_HEADER + "Ce nombre ne peut contenir que 2 décimales", field.getToolTipText());
        assertEquals(Color.red, field.getForeground());

        getHelper().sendKeyAction(new KeyEventData(this, field, KeyEvent.VK_BACK_SPACE));
        awtSleep();
        assertEquals("1.23", field.getText());
        assertEquals("1.23", field.getNumber().toString());
        assertEquals("prevTooltip", field.getToolTipText());
        assertEquals(Color.blue, field.getForeground());
    }


    /**
     * * Test la mise a jour de la couleur du texte et des tooltips
     */
    public void test_gui_maxDigits_updateColor() {
        field.setMaximumIntegerDigits(3);
        field.setForeground(Color.blue);
        field.setToolTipText("prevTooltip");

        assertOverflow("4567");
        assertEquals(null, field.getNumber());
        assertEquals(
              TOOLTIP_HEADER + "Ce nombre ne peut contenir au total que 3 chiffres dans sa partie entière",
              field.getToolTipText());

        getHelper().sendKeyAction(new KeyEventData(this, field, KeyEvent.VK_BACK_SPACE));
        awtSleep();

        assertEquals("456", field.getText());
        assertEquals("prevTooltip", field.getToolTipText());
        assertEquals(Color.blue, field.getForeground());
    }


    public void test_gui_maxDigits_overflow() throws Exception {
        field.setMaximumIntegerDigits(3);
        field.setForeground(Color.blue);

        assertOverflow("1234");
        assertEquals(null, field.getNumber());
        assertEquals(
              TOOLTIP_HEADER + "Ce nombre ne peut contenir au total que 3 chiffres dans sa partie entière",
              field.getToolTipText());

        assertNotOverflow("123");
        assertNotOverflow("123.23");
        assertNotOverflow("-123.23");
        assertNotOverflow("-123");
        assertNotOverflow("-123.0");

        assertOverflow("1234");
        assertOverflow("1234.23");
        assertOverflow("-1234.23");
        assertOverflow("-1234");
    }


    public void test_gui_color_after_set_number() throws Exception {
        field.setMaximumIntegerDigits(3);
        field.setForeground(Color.blue);

        assertOverflow("1234");
        transferFocus(field);
        assertTrue(field.hasError());
        field.setNumber(12);
        assertNotOverflow("123");
    }


    public void test_has_error() throws Exception {
        field.setMaximumIntegerDigits(3);
        field.setForeground(Color.blue);
        assertOverflow("1234");
        assertTrue(field.hasError());
        assertNotNull(field.getErrorMessage());
        assertNotOverflow("12");
        assertFalse(field.hasError());
        assertNull(field.getErrorMessage());
    }


    private void assertNotOverflow(String number) {
        field.setNumber(null);
        getHelper().sendString(new StringEventData(this, field, number));
        assertEquals(Color.blue, field.getForeground());
    }


    private void assertOverflow(String number) {
        field.setNumber(null);
        getHelper().sendString(new StringEventData(this, field, number));
        assertEquals(Color.red, field.getForeground());
    }


    /**
     * Test le filtre de saisie sur la valeur max possible.
     */
    public void test_gui_maxValue() {
        field.setMaxValue(2);

        getHelper().sendString(new StringEventData(this, field, "1.23"));
        assertEquals("1.23", field.getNumber().toString());

        field.setCaretPosition(1);
        getHelper().sendString(new StringEventData(this, field, "4"));
        assertEquals("14.23", field.getText());
        assertEquals(null, field.getNumber());
        assertEquals(TOOLTIP_HEADER + "Ce champs doit être inférieur à 2", field.getToolTipText());
        assertEquals(Color.red, field.getForeground());

        getHelper().sendKeyAction(new KeyEventData(this, field, KeyEvent.VK_BACK_SPACE));
        assertEquals("1.23", field.getText());
        assertEquals("1.23", field.getNumber().toString());
        assertEquals(null, field.getToolTipText());
        assertEquals(UIManager.getColor("TextField.foreground"), field.getForeground());
    }


    /**
     * Test le filtre de saisie sur la valeur min possible.
     */
    public void test_gui_minValue() throws Exception {
        field.setMinValue(2);

        getHelper().sendString(new StringEventData(this, field, "1.23"));
        assertEquals("1.23", field.getText());
        assertEquals(null, field.getNumber());
        assertEquals(TOOLTIP_HEADER + "Ce champs doit être supérieur à 2", field.getToolTipText());
        assertEquals(Color.red, field.getForeground());

        field.setCaretPosition(1);
        getHelper().sendString(new StringEventData(this, field, "4"));
        assertEquals("14.23", field.getText());
        assertEquals("14.23", field.getNumber().toString());
        assertEquals(null, field.getToolTipText());
        assertEquals(UIManager.getColor("TextField.foreground"), field.getForeground());
    }


    /**
     * Test que l'on ne peut saisir que des nombres.
     */
    public void test_gui_numberOnly() {
        getHelper().sendString(new StringEventData(this, field, "1235.5"));
        assertEquals("1235.5", field.getText());

        // La saisie est refusé.
        getHelper().sendString(new StringEventData(this, field, "bobo"));
        assertEquals("1235.5", field.getText());
    }


    /**
     * Test qu'un champs vide correspond au nombre null.
     */
    public void test_gui_setNumber_null() {
        field.setText("1");

        field.setCaretPosition(1);
        assertEquals(1, field.getCaretPosition());
        getHelper().sendKeyAction(new KeyEventData(this, field, KeyEvent.VK_BACK_SPACE));

        assertEquals(null, field.getNumber());
        assertEquals("", field.getText());
    }


    public void test_rendererAndEdition() throws Exception {
        Color previousBackground = field.getBackground();
        Color previousForeground = field.getForeground();
        Font previousFont = field.getFont();

        field.setRenderer(createPsychedelicRenderer());
        setFocus(field);
        field.setNumber(new Integer("1234"));
        assertEquals("1234", field.getText());
        assertFieldDisplayProperties(previousFont, previousForeground, previousBackground);

        setFocus(otherField);
        setFocus(field);
        assertEquals("1234", field.getText());
    }


    public void test_rendererAndDisplay() throws Exception {
        final NumberFieldRenderer psychedelicRenderer = createPsychedelicRenderer();
        field.setRenderer(psychedelicRenderer);

        editFieldAndMoveFocus("1234");
        assertEquals("1.234,00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);
    }


    public void test_applyRendererInEditMode() throws Exception {
        field.setRenderer(new NumberFieldRenderer(
              new DecimalFormat("##0.###", new DecimalFormatSymbols(Locale.ENGLISH))));
        field.setApplyRendererInEditMode(false);

        editFieldAndMoveFocus("1234.1500000");
        assertEquals("1234.15", field.getText());

        setFocus(field);
        assertEquals("1234.1500000", field.getText());

        field.setApplyRendererInEditMode(true);
        editFieldAndMoveFocus("2659.120000");
        assertEquals("2659.12", field.getText());

        setFocus(field);
        assertEquals("2659.12", field.getText());
    }


    public void test_noEditModeIfNotEditable() throws Exception {
        field.setRenderer(new NumberFieldRenderer(
              new DecimalFormat("##0.###", new DecimalFormatSymbols(Locale.ENGLISH))));

        editFieldAndMoveFocus("1234.1500000");
        field.setEditable(false);

        assertEquals("1234.15", field.getText());
        setFocus(field);
        assertEquals("1234.15", field.getText());
    }


    public void test_setThousandsSeparatorOnRenderer() {
        editFieldAndMoveFocus("1234");
        assertEquals("1234", field.getText());

        field.setThousandsSeparatorOnRenderer('#');
        editFieldAndMoveFocus("1234");
        assertEquals("1#234,00", field.getText());

        final NumberFieldRenderer psychedelicRenderer = createPsychedelicRenderer();
        field.setRenderer(psychedelicRenderer);

        editFieldAndMoveFocus("1234");
        assertEquals("1.234,00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);

        field.setThousandsSeparatorOnRenderer(' ');
        editFieldAndMoveFocus("1234");
        assertEquals("1 234,00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);
    }


    public void test_setSeparatorsOnRenderer() {
        editFieldAndMoveFocus("1234");
        assertEquals("1234", field.getText());

        field.setSeparatorsOnRenderer('#', '*');
        editFieldAndMoveFocus("1234");
        assertEquals("1#234*00", field.getText());

        final NumberFieldRenderer psychedelicRenderer = createPsychedelicRenderer();
        field.setRenderer(psychedelicRenderer);

        editFieldAndMoveFocus("1234");
        assertEquals("1.234,00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);

        field.setSeparatorsOnRenderer('#', '*');
        editFieldAndMoveFocus("1234");
        assertEquals("1#234*00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);

        field.setThousandsSeparatorOnRenderer('-');
        editFieldAndMoveFocus("1234");
        assertEquals("1-234*00", field.getText());
        assertFieldDisplayPropertiesSameAs(psychedelicRenderer);
    }


    public void test_applyDecimalFormat() throws Exception {
        field.applyDecimalFormat("#,##0.00");
        editFieldAndMoveFocus("1234.465");
        assertEquals("1 234.46", field.getText());

        field.applyDecimalFormat("#,##0.00", ':', ',');
        editFieldAndMoveFocus("1234.465");
        assertEquals("1:234,46", field.getText());
    }


    private void editFieldAndMoveFocus(String newValue) {
        setFocus(field);
        field.setNumber(new BigDecimal(newValue));
        setFocus(otherField);
    }


    private void assertFieldDisplayProperties(Font expectedFont, Color expectedForeground,
                                              Color expectedBackground) {
        assertEquals(expectedBackground, field.getBackground());
        assertEquals(expectedFont, field.getFont());
        assertEquals(expectedForeground, field.getForeground());
    }


    private void assertFieldDisplayPropertiesSameAs(NumberFieldRenderer expectedRender) {
        assertFieldDisplayProperties(expectedRender.getFont(), expectedRender.getForeground(),
                                     expectedRender.getBackground());
    }


    /**
     * Test la fonctionnalité d'édition constante.
     */
    public void test_prg_forceEditionMode() {
        // Backup previuous field state
        Color prevbackground = field.getBackground();
        Color prevforeground = field.getForeground();
        Font prevfont = field.getFont();

        // Build Renderer
        field.setRenderer(createPsychedelicRenderer());
        field.forceEditionMode(true);

        // Mode Edition
        setFocus(field);
        field.setNumber(new Integer("1234"));
        assertEquals("1234", field.getText());

        // Mode Toujours Edition
        setFocus(otherField);
        assertEquals("1234", field.getText());
        assertEquals(prevbackground, field.getBackground());
        assertEquals(prevfont, field.getFont());
        assertEquals(prevforeground, field.getForeground());

        // Mode Toujours Edition
        setFocus(field);
        assertEquals("1234", field.getText());
        assertEquals(prevbackground, field.getBackground());
        assertEquals(prevfont, field.getFont());
        assertEquals(prevforeground, field.getForeground());
    }


    /**
     * Test la modification du champs par programme.
     */
    public void test_prg_setNumber() {
        //-------- Changement par setText --------
        field.setText("1123.5");

        assertEquals("1123.5", field.getText());
        assertEquals("1123.5", field.getNumber().toString());

        //-------- Changement par setNumber --------
        final String bigNb = "1234567890123456789.1234567890123456789";
        field.setNumber(new BigDecimal(bigNb));

        assertEquals(bigNb, field.getNumber().toString());
        assertEquals(bigNb, field.getText());

        //-------- Changement par setNumber --------
        field.setNumber(new BigDecimal("-3.5"));

        assertEquals("-3.5", field.getNumber().toString());
        assertEquals("-3.5", field.getText());
    }


    /**
     * Test le changement du champs pour des valeurs null.
     */
    public void test_prg_setNumber_NA() {
        field.setText("");
        assertEquals(null, field.getNumber());
        assertEquals("", field.getText());

        field.setNumber(null);
        assertEquals(null, field.getNumber());
        assertEquals("", field.getText());
    }


    public void test_setText() throws Exception {
        field.setText("1.618");
        assertEquals("1.618", field.getNumber().toString());
    }


    public void test_setTextNull() throws Exception {
        field.setText("");
        assertNull(field.getNumber());
    }


    public void test_validNumberExpression() throws Exception {
        field.setValidInputNumberExpression("\\d*.?\\d{0,6}");
        field.setText("123.123456");
        assertEquals("123.123456", field.getNumber().toString());

        field.setText("123.12345678966");
        assertNull(field.getNumber());
    }


    @Override
    protected void setUp() {
        setHelper(new JFCTestHelper());
        field = new NumberField();
        buildWindow();
        mockPropChangeListener = new MockPropertyChangeListener();
        setFocus(field);
    }


    @Override
    protected void tearDown() {
        TestHelper.disposeWindow(window, this);
        window = null;
        TestHelper.cleanUp(this);
    }


    private DecimalFormatSymbols buildPseudoFrenchSymbol() {
        DecimalFormatSymbols pseudoFrenchSymbol = new DecimalFormatSymbols();
        pseudoFrenchSymbol.setDecimalSeparator(',');
        pseudoFrenchSymbol.setGroupingSeparator('.');
        return pseudoFrenchSymbol;
    }


    private void buildWindow() {
        window = new JFrame("Test " + getName());
        otherField = new JTextField();
        window.getContentPane().add(otherField, BorderLayout.NORTH);
        window.getContentPane().add(field, BorderLayout.CENTER);
        window.pack();
        window.setVisible(true);
    }


    private NumberFieldRenderer createPsychedelicRenderer() {
        return new NumberFieldRenderer(new DecimalFormat("#,###.00", buildPseudoFrenchSymbol()),
                                       field.getFont().deriveFont(Font.ITALIC), Color.yellow, Color.gray,
                                       null);
    }


    private class MockPropertyChangeListener implements PropertyChangeListener {
        PropertyChangeEvent lastReceivedEvent = null;
        int propChangeCalledTimes = 0;


        public void propertyChange(PropertyChangeEvent evt) {
            propChangeCalledTimes++;
            lastReceivedEvent = evt;
        }
    }
}
