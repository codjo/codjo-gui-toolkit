/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.number;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
/**
 * Composant graphique peremettant d'éditer un nombre.
 *
 * <p> Le composant permet de restreindre la saisie en limitant le nombre de décimales ou en limitant la marge
 * de variation du nombre (borne max et/ou min). </p>
 *
 * @author $Author: gaudefr $
 * @version $Revision: 1.8 $
 */
public class NumberField extends JTextField implements NumberFieldInterface {
    public static final String NUMBER_PROPERTY = "number";
    private final NumberOnlyDocument numberOnlyDocument = new NumberOnlyDocument();
    private ErrorManager errorManager = new ErrorManager();
    private FieldModeManager fieldModeManager = new FieldModeManager();
    private MaximumFractionDigitVeto maximumFractionDigitVeto =
          new MaximumFractionDigitVeto();
    private MaximumDigitVeto maximumIntegerDigitVeto = new MaximumDigitVeto();
    private RangeNumberValueVeto rangeNumberValueVeto = new RangeNumberValueVeto();
    private UIChangeListener uiChangeListener = new UIChangeListener();
    private Number number;
    private Document standardDocument;
    private String validInputNumberExpression = "-?\\d*\\.?\\d*";
    private boolean applyRendererInEditMode = false;


    public NumberField() {
        standardDocument = getDocument();
        setHorizontalAlignment(JTextField.RIGHT);

        addVetoableChangeListener(maximumIntegerDigitVeto);
        addVetoableChangeListener(maximumFractionDigitVeto);
        addVetoableChangeListener(rangeNumberValueVeto);

        addFocusListener(fieldModeManager);
    }


    public void applyDecimalFormat(String decimalFormat) {
        applyDecimalFormat(decimalFormat, ' ', '.');
    }


    public void applyDecimalFormat(String decimalFormat, char thousandSeparator, char decimalSeparator) {
        setRenderer(new NumberFieldRenderer(new DecimalFormat(decimalFormat)));
        setSeparatorsOnRenderer(thousandSeparator, decimalSeparator);
    }


    public void setValidInputNumberExpression(String expression) {
        validInputNumberExpression = expression;
    }


    public String getValidInputNumberExpression() {
        return validInputNumberExpression;
    }


    public void setMaxValue(Number maxValue) {
        this.rangeNumberValueVeto.max = maxValue;
    }


    public Number getMaxValue() {
        return rangeNumberValueVeto.max;
    }


    public void setMaximumFractionDigits(int maximumFractionDigits) {
        this.maximumFractionDigitVeto.max = maximumFractionDigits;
    }


    public int getMaximumFractionDigits() {
        return maximumFractionDigitVeto.max;
    }


    public void setMaximumIntegerDigits(int maxIntegerDigits) {
        this.maximumIntegerDigitVeto.max = maxIntegerDigits;
    }


    public int getMaximumIntegerDigits() {
        return maximumIntegerDigitVeto.max;
    }


    public void setMinValue(Number minValue) {
        this.rangeNumberValueVeto.min = minValue;
    }


    public Number getMinValue() {
        return rangeNumberValueVeto.min;
    }


    public void setNumber(Number number) {
        if (fieldModeManager.isEditMode()) {
            getDocument().removeDocumentListener(uiChangeListener);
        }

        fieldModeManager.updateGuiFrom(number);

        setNumberWithoutGuiUpdate(number);

        if (fieldModeManager.isEditMode()) {
            getDocument().addDocumentListener(uiChangeListener);
        }
    }


    public Number getNumber() {
        return number;
    }


    public void setRenderer(NumberFieldRenderer renderer) {
        this.fieldModeManager.renderer = renderer;
    }


    public NumberFieldRenderer getRenderer() {
        return fieldModeManager.renderer;
    }


    public void forceEditionMode(boolean alwaysEditMode) {
        if (alwaysEditMode) {
            removeFocusListener(fieldModeManager);
            fieldModeManager.focusGained(null);
        }
        else {
            removeFocusListener(fieldModeManager);
            addFocusListener(fieldModeManager);
            if (hasFocus()) {
                fieldModeManager.focusGained(null);
            }
            else {
                fieldModeManager.focusLost(null);
            }
        }
    }


    protected Translater newTranslater() {
        return new Translater();
    }


    private void setNumberWithoutGuiUpdate(Number number) {
        try {
            Number old = this.number;
            fireVetoableChange(NUMBER_PROPERTY, old, number);
            this.number = number;
            firePropertyChange(NUMBER_PROPERTY, old, this.number);
            errorManager.reset();
        }
        catch (PropertyVetoException ex) {
            errorManager.badInputDetected(ex);
        }
    }


    public void setThousandsSeparatorOnRenderer(char thousandsSeparator) {
        setSeparatorsOnRenderer(false, thousandsSeparator, '!');
    }


    public void setSeparatorsOnRenderer(char thousandsSeparator, char decimalSeparator) {
        setSeparatorsOnRenderer(true, thousandsSeparator, decimalSeparator);
    }


    public void setApplyRendererInEditMode(boolean applyRendererInEditMode) {
        this.applyRendererInEditMode = applyRendererInEditMode;
    }


    private void setSeparatorsOnRenderer(boolean twice, char thousandsSeparator,
                                         char decimalSeparator) {
        final NumberFieldRenderer renderer = getRenderer();
        NumberFormat format = renderer.getFormat();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        if (!(format instanceof DecimalFormat)) {
            format = new DecimalFormat("#,##0.00", decimalFormatSymbols);
        }
        DecimalFormat decimalFormat = (DecimalFormat)format;
        decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator(thousandsSeparator);
        if (twice) {
            decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        }
        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
        renderer.setFormat(decimalFormat);
        setRenderer(renderer);
    }


    public boolean hasError() {
        return errorManager.hasError();
    }


    public String getErrorMessage() {
        if (!hasError()) {
            return null;
        }
        return errorManager.lastError.getLocalizedMessage();
    }


    /**
     * Traduction de texte vers nombre et inversement.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    static class Translater {
        public String numberToString(Number nb) {
            if (nb == null) {
                return "";
            }
            else {
                return nb.toString();
            }
        }


        public BigDecimal stringToNumber(final String txt) {
            try {
                return new BigDecimal(txt);
            }
            catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * Gestion d'une saisie en erreur (Affichage en rouge).
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class ErrorManager {
        private GuiFieldState guiState = new GuiFieldState();
        private PropertyVetoException lastError = null;


        public void badInputDetected(PropertyVetoException veto) {
            if (!hasError()) {
                guiState.backupState();
            }
            lastError = veto;
            if (number != null) {
                firePropertyChange(NUMBER_PROPERTY, number, null);
            }
            number = null;
            setForeground(Color.red);
            setToolTipText("<html><b>Erreur de saisie !</b><br><i>"
                           + veto.getLocalizedMessage());
        }


        public void reset() {
            if (!hasError()) {
                return;
            }
            guiState.restoreState();
            lastError = null;
        }


        private boolean hasError() {
            return lastError != null;
        }
    }

    /**
     * Gere les differents mode du champs (edition ou non). Applique le renderer si nécessaire.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class FieldModeManager implements FocusListener {
        private NumberFieldRenderer renderer = new NumberFieldRenderer();
        private GuiFieldState state = new GuiFieldState();
        private Translater translater = newTranslater();


        public boolean isEditMode() {
            return getDocument() == numberOnlyDocument;
        }


        public Number buildNumberFrom(String txt) {
            return translater.stringToNumber(txt);
        }


        public void focusGained(FocusEvent event) {
            if (!isEditable()) {
                return;
            }
            state.restoreState();
            setDocument(numberOnlyDocument);
            if (!applyRendererInEditMode) {
                setText(translater.numberToString(number));
            }
            else {
                renderer.updateGuiFrom(number, NumberField.this);
            }
            getDocument().addDocumentListener(uiChangeListener);
        }


        public void focusLost(FocusEvent event) {
            if (!isEditable()) {
                return;
            }
            getDocument().removeDocumentListener(uiChangeListener);
            // Ne pas sauvegarder un état en erreur
            // Cf. NumberFieldTest.test_gui_color_after_set_number
            errorManager.guiState.restoreState();
            state.backupState();
            setDocument(standardDocument);
            if (renderer != null) {
                renderer.applyDisplayFormat(NumberField.this);
            }
        }


        public void updateGuiFrom(Number nb) {
            if (isEditMode() && !applyRendererInEditMode) {
                setText(translater.numberToString(nb));
            }
            else {
                renderer.updateGuiFrom(nb, NumberField.this);
            }
        }
    }

    /**
     * Stocke l'etat Graphique du composant.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class GuiFieldState {
        private Color prevBackground = getBackground();
        private Color prevForeground = getForeground();
        private Border prevBorder = getBorder();
        private Font prevFont = getFont();
        private String prevTooltip = getToolTipText();


        public void restoreState() {
            setForeground(prevForeground);
            setBackground(prevBackground);
            setFont(prevFont);
            setToolTipText(prevTooltip);
            setBorder(prevBorder);
        }


        private void backupState() {
            prevForeground = getForeground();
            prevBackground = getBackground();
            prevBorder = getBorder();
            prevFont = getFont();
            prevTooltip = getToolTipText();
        }
    }

    /**
     * Filtre de saisie permettant de limiter la saisie de décimale.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class MaximumFractionDigitVeto implements VetoableChangeListener {
        private int max = -1;


        public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException {
            if (max == -1
                || !NUMBER_PROPERTY.equals(evt.getPropertyName())
                || evt.getNewValue() == null) {
                return;
            }
            String nb = evt.getNewValue().toString();
            int decimalSeparatorIdx = nb.indexOf('.');
            if (decimalSeparatorIdx == -1) {
                return;
            }
            if ((nb.length() - decimalSeparatorIdx) > max + 1) {
                throw new PropertyVetoException("Ce nombre ne peut contenir que " + max
                                                + " décimales", evt);
            }
        }
    }

    /**
     * Filtre de saisie permettant de limiter la saisie des chiffres.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class MaximumDigitVeto implements VetoableChangeListener {
        private int max = -1;


        public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException {

            if (max == -1 || !NUMBER_PROPERTY.equals(evt.getPropertyName()) || evt.getNewValue() == null) {
                return;
            }

            String numberAsString = evt.getNewValue().toString().replace("-", "");
            String integerPart = numberAsString.split("\\.")[0];

            if (integerPart.length() > max) {
                throw new PropertyVetoException("Ce nombre ne peut contenir au total que " + max
                                                + " chiffres dans sa partie entière", evt);
            }
        }
    }

    /**
     * Document permettant de filtrer les charactéres non numériques.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class NumberOnlyDocument extends PlainDocument {
        @Override
        public void insertString(final int offs, final String str,
                                 final AttributeSet attribute) throws BadLocationException {
            if (NumberField.this.getText().length() == 0 && str.startsWith("-")) {
                super.insertString(offs, str, attribute);
                return;
            }
            if (NumberField.this.getText().length() == 0 && str.startsWith(".")) {
                super.insertString(offs, "0" + str, attribute);
                return;
            }

            String current = NumberField.this.getText();
            int min = Math.min(offs, current.length());
            String target = current.substring(0, min) + str + current.substring(min);

            if (isValidNumber(target)) {
                super.insertString(min, str, attribute);
            }
        }


        private boolean isValidNumber(String contents) {
            return contents != null && contents.matches(getValidInputNumberExpression());
        }
    }

    /**
     * Filtre de saisie permettant de limiter la saisie entre une borne max et min.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class RangeNumberValueVeto implements VetoableChangeListener {
        private Number max = null;
        private Number min = null;


        public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException {
            if (!NUMBER_PROPERTY.equals(evt.getPropertyName())
                || evt.getNewValue() == null) {
                return;
            }
            Number current = (Number)evt.getNewValue();
            if (max != null && max.doubleValue() < current.doubleValue()) {
                throw new PropertyVetoException("Ce champs doit être inférieur à " + max,
                                                evt);
            }
            if (min != null && min.doubleValue() > current.doubleValue()) {
                throw new PropertyVetoException("Ce champs doit être supérieur à " + min,
                                                evt);
            }
        }
    }

    /**
     * MAJ le nombre en fonction de modification sur l'IHM.
     *
     * @author $Author: gaudefr $
     * @version $Revision: 1.8 $
     */
    class UIChangeListener implements DocumentListener {
        private boolean blocked = false;


        public void changedUpdate(DocumentEvent event) {
            updateNumber();
        }


        public void insertUpdate(DocumentEvent event) {
            updateNumber();
        }


        public void removeUpdate(DocumentEvent event) {
            updateNumber();
        }


        private void updateNumber() {
            if (blocked) {
                return;
            }
            setNumberWithoutGuiUpdate(fieldModeManager.buildNumberFrom(getText()));
        }
    }
}
