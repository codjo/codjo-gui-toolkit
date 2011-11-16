package net.codjo.gui.toolkit.date;
import net.codjo.gui.toolkit.calendar.JCalendarWithYearsButtonHelper;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextField;
/**
 *
 */
public class PopupDateField extends AbstractDateField {

    // GUI
    private JTextField dateTextField = new JTextField();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    // Internal state
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private FocusValidator focusValidator = new FocusValidator();
    private DateFieldMouseListener dateFieldMouseListener;


    public PopupDateField() {
        this(true);
    }


    public PopupDateField(boolean showCalendar) {
        this(showCalendar, null);
    }


    public PopupDateField(boolean showCalendar, CallBack callBack) {
        dateFieldMouseListener = new DateFieldMouseListener();
        calendarButtonVisible = showCalendar;
        dateFormat.setLenient(false);
        this.callBack = callBack;

        addListeners();
        jbInit();
    }


    @Override
    public void setBackground(Color fg) {
        if (dateTextField == null) {
            super.setBackground(fg);
            return;
        }
        dateTextField.setBackground(fg);
    }


    @Override
    public void setName(String name) {
        super.setName(name);
        dateTextField.setName(getName() + ".dateTextField");
    }


    public void setEditable(boolean isEditable) {
        this.dateTextField.setEditable(isEditable);

        firePropertyChange("editable", this.dateTextField.isEditable(), isEditable);
    }


    public boolean isEditable() {
        return dateTextField.isEditable();
    }


    @Override
    public void setEnabled(boolean enabled) {
        if (dateTextField != null) {
            dateTextField.setEnabled(enabled);

            calendarButton.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }


    @Override
    public boolean isEnabled() {
        return dateTextField.isEnabled();
    }


    @Override
    public void setForeground(Color fg) {
        if (fg != Color.red) {
            normalForegroundColor = fg;
        }
        if (dateTextField == null) {
            super.setForeground(fg);
            return;
        }

        dateTextField.setForeground(fg);
    }


    @Override
    public void setToolTipText(String text) {
        super.setToolTipText(text);
        dateTextField.setToolTipText(text);

        calendarButton.setToolTipText(text);
    }


    void askDialog() {
        dateTextField.select(0, 0);
        calendarHelper.askDialog(this, this);
    }


    /**
     * Assemblage des composants.
     */
    void jbInit() {
        dateTextField.setName(this.getName() + ".dateTextField");

        dateTextField.setColumns(10);

        int dayWidth = dateTextField.getPreferredSize().width;
        int dayHeight = dateTextField.getPreferredSize().height - 3;
        Dimension preferredDimension = new Dimension(dayWidth, dayHeight);
        dateTextField.setPreferredSize(preferredDimension);

        dateTextField.setMinimumSize(preferredDimension);

        dateTextField.setMaximumSize(preferredDimension);

        dateTextField.setHorizontalAlignment(JTextField.CENTER);

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
        this.add(dateTextField,
                 new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    }


    @Override
    public void setDefaultCalendarHelper(boolean hasDefaultCalendarHelper) {
        if (hasDefaultCalendarHelper) {
            if (callBack == null) {
                setCalendarHelper(new JCalendarWithYearsButtonHelper());
            }
            else {
                setCalendarHelper(new JCalendarWithYearsButtonHelper(callBack));
            }
        }
        else {
            setCalendarHelper(null);
        }
    }


    /**
     * Ajoute les listeners de validation, et de transit de focus.
     */
    @Override
    void addListeners() {
        dateTextField.getDocument().addDocumentListener(documentValidator);

        dateTextField.addFocusListener(focusValidator);

        dateTextField.addMouseListener(dateFieldMouseListener);

        setMaxLengthKeyListener(dateTextField, 10);

        addPropertyChangeListener(DATE_PROPERTY_NAME, displayUpdater);
    }


    private void setMaxLengthKeyListener(final JTextField field, final int maxLength) {
        field.addKeyListener(new DateKeyAdatper(field, maxLength));
    }


    protected boolean isDateCorrect() {
        boolean isCorrect = true;

        String dateFieldText = dateTextField.getText();
        String[] partDateTeb = dateFieldText.split("/");
        if (partDateTeb.length != 3 || partDateTeb[2].length() != 4) {
            isCorrect = false;
        }
        else {
            try {

                dateFormat.parse(dateTextField.getText());
            }
            catch (ParseException e) {
                isCorrect = false;
            }
        }

        return isCorrect;
    }


    /**
     * Construit la date a partir de l'IHM.
     */
    @Override
    void buildDateFromGUI() {
        try {
            if (isDateCorrect()) {
                String dateStr =
                      dateTextField.getText();
                setDate(dateFormat.parse(dateStr));
            }
            else {
                setDate(BAD_DATE);
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
        displayUpdater.propertyChange(null);
    }


    /**
     * Efface tous les champs qui affiche la date.
     */
    @Override
    void clearDateFields() {
        final String empty = "";
        dateTextField.setText(empty);
    }


    /**
     * Affiche la date positionné (attribut date). Cette methode est appele automatiquement par le listener
     * <code>DisplayUpdater</code>. Si la date est <code>BAD_DATE</code> les textes contenuent dans les
     * JTextField sont affiché en rouge. Si la date est <code>null</code> alors les champs sont vidée.
     */
    @Override
    protected void displayDate() {
        if (date == BAD_DATE) {
            this.setTooltipErrorMessage("Date invalide (Format JJ / MM / AAAA)");
            setToolTipText(this.getTooltipErrorMessage());
            dayOfWeekLabel.setText("");
            setForeground(Color.red);
            return;
        }
        else if (date == null) {
            this.setTooltipErrorMessage("Date vide (Format JJ / MM / AAAA)");
            setToolTipText(this.getTooltipErrorMessage());
            clearDateFields();
            return;
        }

        if (isTooltipStandardMessageDisplayDateValue()) {
            //Do not override the value of the standard message !
            setToolTipText(DateFormat.getDateInstance(DateFormat.FULL).format(date));
        }
        else {
            setToolTipText(this.getTooltipStandardMessage());
        }

        dateTextField.setText(dateFormat.format(date));
        setForeground(normalForegroundColor);
    }


    @Override
    void removeListeners() {
        dateTextField.getDocument().removeDocumentListener(documentValidator);

        dateTextField.removeFocusListener(focusValidator);

        dateTextField.removeMouseListener(dateFieldMouseListener);

        removePropertyChangeListener(DATE_PROPERTY_NAME, displayUpdater);
    }


    public JTextField getDateTextField() {
        return dateTextField;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Test DateField");
        frame.setContentPane(new PopupDateField());
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
    private class FocusValidator extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent evt) {
            ((JTextField)evt.getComponent()).selectAll();
        }


        @Override
        public void focusLost(FocusEvent evt) {
            removeListeners();
            buildDateFromGUI();
            addListeners();
        }
    }

    private class DateFieldMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {
            if (calendarButton.isEnabled()) {
                askDialog();
            }
        }
    }
    ;

    private static final class DateKeyAdatper extends KeyAdapter {
        private final JTextField field;
        private final int maxLength;


        DateKeyAdatper(JTextField field, int maxLength) {
            this.field = field;
            this.maxLength = maxLength;
        }


        @Override
        public void keyTyped(KeyEvent evt) {
            if (evt.getKeyChar() != KeyEvent.VK_BACK_SPACE
                && evt.getKeyCode() != KeyEvent.VK_DELETE && evt.getKeyChar() != KeyEvent.VK_SLASH) {
                if (field.getText().length() >= maxLength
                    && (field.getSelectedText() == null
                        || field.getSelectedText().length() == 0)) {
                    evt.consume();
                }
                else if (evt.getKeyChar() < '0' || evt.getKeyChar() > '9') {
                    evt.consume();
                }
            }
        }
    }


    @Override
    void updateDateField(Date timeDate) {
        dateTextField.setText(dateFormat.format(timeDate));
    }
}
