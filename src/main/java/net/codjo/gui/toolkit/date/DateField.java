/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.gui.toolkit.date;
import net.codjo.gui.toolkit.calendar.CalendarHelper;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 * Composant graphique pour la saisie de Date.
 *
 * <ul> <li> Un evenenement <code>PropertyChangeEvent</code> est lance des que la date est change (par
 * l'utilisateur ou par programme). </li> <li> Le tooltip renseigne sur le format voulu ou affiche la date en
 * format long. </li> <li> Il est possible de faire afficher le jours de la semaine. Il suffit de mettre la
 * propriete <code>DisplayingDayOfWeek</code> a <code>true</code>. </li> </ul>
 *
 * @author $Author: crego $
 * @version $Revision: 1.25 $
 */
public class DateField extends AbstractDateField {

    // GUI
    private JTextField dayField = new JTextField();
    private JTextField monthField = new JTextField();
    private JTextField yearField = new JTextField();
    private JLabel slashlabelA = new JLabel("/");
    private JLabel slashlabelB = new JLabel("/");
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    // Internal state

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat dayInWeekFormat = new SimpleDateFormat("EEEEEEEE");
    private boolean displayingDayOfWeek = false;

    // Listeners
    private TranslateFocusTo dayTranslateFocus;
    private TranslateFocusTo monthTranslateFocus;
    protected FocusListener focusValidator = new FocusValidator();
    private DateFieldGlobalFocusValidator globalFocusValidator = new DateFieldGlobalFocusValidator();
    private Map<ActionListener, PropertyChangeListener> calendarListeners
          = new HashMap<ActionListener, PropertyChangeListener>();


    public DateField() {
        this(true);
    }


    public DateField(boolean showCalendar) {
        this(showCalendar, null);
    }


    public DateField(boolean showCalendar, CallBack callBack) {
        this(showCalendar, callBack, new JButton());
    }


    public DateField(boolean showCalendar, CallBack callBack, JButton calendarButton) {
        super(calendarButton);
        calendarButtonVisible = showCalendar;
        dateFormat.setLenient(false);
        dayInWeekFormat.setLenient(false);
        dayTranslateFocus = new TranslateFocusTo(monthField);
        monthTranslateFocus = new TranslateFocusTo(yearField);

        this.callBack = callBack;

        addListeners();
        jbInit();
    }


    @Override
    public synchronized void addFocusListener(FocusListener focusListener) {
        globalFocusValidator.addFocusListener(focusListener);
    }


    @Override
    public void setBackground(Color fg) {
        if (dayField == null) {
            super.setBackground(fg);
            return;
        }
        dayField.setBackground(fg);
        monthField.setBackground(fg);
        yearField.setBackground(fg);
    }


    @Override
    public void setName(String name) {
        super.setName(name);
        dayField.setName(getName() + ".dayField");
        monthField.setName(getName() + ".monthField");
        yearField.setName(getName() + ".yearField");
    }


    public void addActionListener(final ActionListener actionListener) {
        dayField.addActionListener(actionListener);
        monthField.addActionListener(actionListener);
        yearField.addActionListener(actionListener);

        PropertyChangeListener propChangeListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                actionListener.actionPerformed(new ActionEvent(evt.getSource(),
                                                               ActionEvent.ACTION_PERFORMED,
                                                               ""));
            }
        };
        calendarListeners.put(actionListener, propChangeListener);
        addPropertyChangeListener(DateField.DATE_PROPERTY_NAME, propChangeListener);
    }


    public void removeActionListener(ActionListener actionListener) {
        dayField.removeActionListener(actionListener);
        monthField.removeActionListener(actionListener);
        yearField.removeActionListener(actionListener);

        removePropertyChangeListener(DateField.DATE_PROPERTY_NAME, calendarListeners.get(actionListener));
    }


    private void setDatePart(JTextField datePart, Calendar calendar, int calendarType) {
        String datePartStr = datePart.getText();
        int finalDatePart = calendar.get(calendarType);
        if (calendarType == Calendar.MONTH) {
            finalDatePart = finalDatePart + 1;
        }
        if (datePartStr == null
            || "".equals(datePartStr)
            || Integer.parseInt(datePartStr) != finalDatePart) {
            if (String.valueOf(finalDatePart).length() == 1) {
                datePart.setText(String.valueOf("0" + finalDatePart));
            }
            else {
                datePart.setText(String.valueOf(finalDatePart));
            }
        }
    }


    @Override
    public void setDefaultCalendarHelper(boolean hasDefaultCalendarHelper) {
        if (hasDefaultCalendarHelper) {
            if (callBack == null) {
                setCalendarHelper(new CalendarHelper());
            }
            else {
                setCalendarHelper(new CalendarHelper(callBack));
            }
        }
        else {
            setCalendarHelper(null);
        }
    }


    /**
     * Affiche (ou non) le jours de la semaine.
     *
     * @param newDisplayingDayOfWeek <code>true</code> affiche le champs.
     */
    public void setDisplayingDayOfWeek(boolean newDisplayingDayOfWeek) {
        if (!isDisplayingDayOfWeek() && newDisplayingDayOfWeek) {
            add(dayOfWeekLabel,
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                       GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 5), 0, 0));
        }
        else if (isDisplayingDayOfWeek() && !newDisplayingDayOfWeek) {
            this.remove(dayOfWeekLabel);
        }
        displayingDayOfWeek = newDisplayingDayOfWeek;
    }


    public void setEditable(boolean isEditable) {
        this.dayField.setEditable(isEditable);
        this.monthField.setEditable(isEditable);
        this.yearField.setEditable(isEditable);
        firePropertyChange("editable", this.dayField.isEditable(), isEditable);
    }


    public boolean isEditable() {
        return dayField.isEditable() && monthField.isEditable() && yearField.isEditable();
    }


    @Override
    public void setEnabled(boolean enabled) {
        if (dayField != null) {
            dayField.setEnabled(enabled);
            monthField.setEnabled(enabled);
            yearField.setEnabled(enabled);
            dayOfWeekLabel.setEnabled(enabled);
            slashlabelA.setEnabled(enabled);
            slashlabelB.setEnabled(enabled);
            calendarButton.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }


    @Override
    public boolean isEnabled() {
        return dayField.isEnabled() && monthField.isEnabled() && yearField.isEnabled();
    }


    @Override
    public void setForeground(Color fg) {
        if (fg != Color.red) {
            normalForegroundColor = fg;
        }
        if (dayField == null) {
            super.setForeground(fg);
            return;
        }

        dayField.setForeground(fg);
        monthField.setForeground(fg);
        yearField.setForeground(fg);
    }


    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        dayField.setToolTipText(text);
        monthField.setToolTipText(text);
        yearField.setToolTipText(text);
        calendarButton.setToolTipText(text);
    }


    /**
     * Indique si le champs du jours de la semaine est affiché.
     *
     * @return <code>true</code> si le champs est visible.
     */
    public boolean isDisplayingDayOfWeek() {
        return displayingDayOfWeek;
    }


    void askDialog() {
        dayField.select(0, 0);
        monthField.select(0, 0);
        yearField.select(0, 0);
        calendarHelper.askDialog(this, this);
    }


    /**
     * Assemblage des composants.
     */
    void jbInit() {
        dayField.setName(this.getName() + ".dayField");
        monthField.setName(this.getName() + ".monthField");
        yearField.setName(this.getName() + ".yearField");

        dayField.setColumns(2);
        monthField.setColumns(2);
        yearField.setColumns(4);

        int dayWidth = dayField.getPreferredSize().width;
        int dayHeight = dayField.getPreferredSize().height - 3;
        Dimension preferredDimension = new Dimension(dayWidth, dayHeight);
        dayField.setPreferredSize(preferredDimension);
        monthField.setPreferredSize(preferredDimension);
        Dimension yearDimension = new Dimension(yearField.getPreferredSize().width, dayHeight);
        yearField.setPreferredSize(yearDimension);

        dayField.setMinimumSize(preferredDimension);
        monthField.setMinimumSize(preferredDimension);
        yearField.setMinimumSize(yearDimension);

        dayField.setMaximumSize(preferredDimension);
        monthField.setMaximumSize(preferredDimension);
        yearField.setMaximumSize(yearDimension);

        dayOfWeekLabel.setMinimumSize(new Dimension(57, 21));
        dayOfWeekLabel.setPreferredSize(new Dimension(57, 21));

        dayField.setHorizontalAlignment(JTextField.CENTER);
        monthField.setHorizontalAlignment(JTextField.CENTER);
        yearField.setHorizontalAlignment(JTextField.CENTER);

        setDefaultCalendarHelper(true);
        calendarButton.setVisible(calendarButtonVisible);
        calendarButton.setName(this.getName() + ".calendarButton");
        calendarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent event) {
                askDialog();
            }
        });
        calendarButton.setBorder(null);
        calendarButton.setIcon(new ImageIcon(DateField.class.getResource("calendar.icon.gif")));

        this.setLayout(gridBagLayout1);
        this.add(dayField,
                 new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(slashlabelA,
                 new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                                        GridBagConstraints.NONE, new Insets(2, 5, 0, 5), 0, 0));
        this.add(monthField,
                 new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.add(slashlabelB,
                 new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                                        GridBagConstraints.NONE, new Insets(2, 5, 0, 5), 0, 0));
        this.add(yearField,
                 new GridBagConstraints(5, 0, 1, 1, 2.0, 0.0, GridBagConstraints.NORTHWEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }


    /**
     * Ajoute les listeners de validation, et de transit de focus.
     */

    @Override
    void addListeners() {
        dayField.getDocument().addDocumentListener(documentValidator);
        monthField.getDocument().addDocumentListener(documentValidator);
        yearField.getDocument().addDocumentListener(documentValidator);
        addFocusListeners();
        dayField.getDocument().addDocumentListener(dayTranslateFocus);
        monthField.getDocument().addDocumentListener(monthTranslateFocus);

        setMaxLengthKeyListener(dayField, 2);
        setMaxLengthKeyListener(monthField, 2);
        setMaxLengthKeyListener(yearField, 4);
        addPropertyChangeListener(DATE_PROPERTY_NAME, displayUpdater);
    }


    private void addFocusListeners() {
        dayField.addFocusListener(focusValidator);
        monthField.addFocusListener(focusValidator);
        yearField.addFocusListener(focusValidator);
        dayField.addFocusListener(globalFocusValidator);
        monthField.addFocusListener(globalFocusValidator);
        yearField.addFocusListener(globalFocusValidator);
    }


    private void setMaxLengthKeyListener(final JTextField field, final int maxLength) {
        field.addKeyListener(new NumberKeyAdapter(field, maxLength));
    }


    protected boolean isDateCorrect() {
        boolean isCorrect = true;
        try {
            if (yearField.getText().trim().length() < 4) {
                isCorrect = false;
            }
            else if ("".equals(monthField.getText())
                     || (!"".equals(monthField.getText())
                         && (Integer.parseInt(monthField.getText()) > 12
                             || Integer.parseInt(monthField.getText()) <= 0))) {
                isCorrect = false;
            }
            else if ("".equals(dayField.getText())
                     || (!"".equals(dayField.getText())
                         && (Integer.parseInt(dayField.getText()) > 31
                             || Integer.parseInt(dayField.getText()) <= 0))) {
                isCorrect = false;
            }
        }
        catch (NumberFormatException e) {
            isCorrect = false;
        }
        return isCorrect;
    }


    /**
     * Construit la date a partir de l'IHM.
     */
    @Override
    void buildDateFromGUI() {
        Date value = buildDateValueFromGUI();
        setDate(value);
        displayUpdater.propertyChange(null);
    }


    protected Date buildDateValueFromGUI() {
        Date value = BAD_DATE;
        try {
            if (isDateCorrect()) {
                value = parseDate();
            }
            else {
                value = BAD_DATE;
            }
        }
        catch (java.text.ParseException ex) {
            APP.error("Erreur lors de la construction de la date à partir de l'IHM.", ex);
            setDate(BAD_DATE);
        }
        catch (RuntimeException ex) {
            APP.error("Erreur lors de la construction de la date à partir de l'IHM.", ex);
            setDate(BAD_DATE);
        }
        return value;
    }


    protected Date parseDate() throws ParseException {
        String dateStr =
              dayField.getText() + "-" + monthField.getText() + "-"
              + yearField.getText();
        return dateFormat.parse(dateStr);
    }


    public void setEnabled(boolean enabledFields, boolean enabledCalendar) {
        calendarButton.setEnabled(enabledCalendar);
        dayField.setEnabled(enabledFields);
        monthField.setEnabled(enabledFields);
        yearField.setEnabled(enabledFields);
    }


    /**
     * Efface tous les champs qui affiche la date.
     */
    @Override
    void clearDateFields() {
        final String empty = "";
        dayOfWeekLabel.setText(empty);
        dayField.setText(empty);
        monthField.setText(empty);
        yearField.setText(empty);
    }


    @Override
    void removeListeners() {
        dayField.getDocument().removeDocumentListener(documentValidator);
        monthField.getDocument().removeDocumentListener(documentValidator);
        yearField.getDocument().removeDocumentListener(documentValidator);
        removeFocusListeners();
        dayField.getDocument().removeDocumentListener(dayTranslateFocus);
        monthField.getDocument().removeDocumentListener(monthTranslateFocus);

        removePropertyChangeListener(DATE_PROPERTY_NAME, displayUpdater);
    }


    private void removeFocusListeners() {
        dayField.removeFocusListener(focusValidator);
        monthField.removeFocusListener(focusValidator);
        yearField.removeFocusListener(focusValidator);
        dayField.removeFocusListener(globalFocusValidator);
        monthField.removeFocusListener(globalFocusValidator);
        yearField.removeFocusListener(globalFocusValidator);
    }


    public boolean containsComponent(Component component) {
        return component != null && (dayField.equals(component) || monthField.equals(component)
                                     || yearField.equals(component));
    }


    protected JTextField getDayField() {
        return dayField;
    }


    protected JTextField getMonthField() {
        return monthField;
    }


    protected JTextField getYearField() {
        return yearField;
    }


    protected JLabel getDayOfWeekLabel() {
        return dayOfWeekLabel;
    }


    protected JLabel getSlashlabelA() {
        return slashlabelA;
    }


    protected JLabel getSlashlabelB() {
        return slashlabelB;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Test DateField");
        DateField dateField = new DateField(true);
        frame.setContentPane(dateField);
        dateField.setEnabled(false, false);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }


    /**
     * Listener qui valide les entrées faite par l'utilisateur et positionne l'attribut date.
     *
     * @author Boris
     * @version $Revision: 1.25 $
     */
    protected class FocusValidator extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent evt) {
            ((JTextField)evt.getComponent()).selectAll();
        }


        @Override
        public void focusLost(FocusEvent evt) {
            removeListeners();
            buildDateFromGUI();
            completeDate((JTextField)evt.getComponent());
            addListeners();
        }


        void completeDate(JTextField dateField) {
            if (dateField.getText() != null && dateField.getText().length() == 1) {
                dateField.setText("0" + dateField.getText());
            }
        }
    }

    protected class DateFieldGlobalFocusValidator extends FocusAdapter {
        private List<FocusListener> focusListeners = new ArrayList<FocusListener>();


        protected DateFieldGlobalFocusValidator() {
        }


        @Override
        public void focusLost(FocusEvent evt) {
            focusValidator.focusLost(evt);
            if (hasGloballyLostFocus(evt)) {
                for (FocusListener focusListener : focusListeners) {
                    focusListener.focusLost(evt);
                }
            }
        }


        boolean hasGloballyLostFocus(FocusEvent focusEvent) {
            Component destObject = focusEvent.getOppositeComponent();
            return (destObject != null && !(containsComponent(destObject)));
        }


        public synchronized void addFocusListener(FocusListener focusListener) {
            focusListeners.add(focusListener);
        }
    }

    /**
     * Listener qui passe le focus a un composant des que au moins 2 characteres ont ete rentre dans le
     * Document ecoutée.
     *
     * @author $Author: crego $
     * @version $Revision: 1.25 $
     */
    private class TranslateFocusTo implements DocumentListener {
        private JTextField component;


        TranslateFocusTo(JTextField cp) {
            component = cp;
        }


        public void changedUpdate(DocumentEvent event) {
        }


        public void insertUpdate(DocumentEvent event) {
            if (event.getDocument().getLength() >= 2) {
                removeListeners();
                component.requestFocus();
                component.selectAll();
                addListeners();
            }
        }


        public void removeUpdate(DocumentEvent event) {
        }
    }


    @Override
    void updateDateField(Date timeDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeDate);
        setDatePart(dayField, calendar, Calendar.DAY_OF_MONTH);
        setDatePart(monthField, calendar, Calendar.MONTH);
        setDatePart(yearField, calendar, Calendar.YEAR);
        dayOfWeekLabel.setText(dayInWeekFormat.format(timeDate));
    }
}
