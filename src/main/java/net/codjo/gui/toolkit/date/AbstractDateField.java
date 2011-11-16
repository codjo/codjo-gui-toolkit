package net.codjo.gui.toolkit.date;
import net.codjo.gui.toolkit.calendar.AbstractCalendarHelper;
import net.codjo.gui.toolkit.calendar.DateFieldInterface;
import net.codjo.gui.toolkit.swing.callback.CallBack;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.log4j.Logger;

public abstract class AbstractDateField extends JPanel implements DateFieldInterface {

    public static final String DATE_PROPERTY_NAME = "date";

    protected static final Logger APP = Logger.getLogger(AbstractDateField.class);
    protected static final Date BAD_DATE = new Date(0);

    protected JLabel dayOfWeekLabel = new JLabel();
    protected DynamicDateValidator documentValidator = new DynamicDateValidator();
    protected Color normalForegroundColor = Color.black;
    protected JButton calendarButton;

    protected CallBack callBack;

    protected boolean calendarButtonVisible = true;
    protected boolean displayMessageToDateValue = true;

    protected DisplayUpdater displayUpdater = new DisplayUpdater();
    protected Date date = null;
    protected AbstractCalendarHelper calendarHelper = null;

    private String tooltipErrorMessage;
    private String tooltipStandardMessage;
    private boolean nullDateValueAuthorized = true;


    protected AbstractDateField() {
        this(new JButton());
    }


    protected AbstractDateField(JButton calendarButton) {
        this.calendarButton = calendarButton;
    }


    abstract void removeListeners();


    abstract void addListeners();


    abstract void buildDateFromGUI();


    abstract void clearDateFields();


    abstract void updateDateField(Date updateDate);


    public abstract void setDefaultCalendarHelper(boolean hasDefaultCalendarHelper);


    public boolean isNullDateValueAuthorized() {
        return nullDateValueAuthorized;
    }


    public void setNullDateValueAuthorized(boolean nullDateValueAuthorized) {
        this.nullDateValueAuthorized = nullDateValueAuthorized;
    }


    @Override
    public void setName(String name) {
        super.setName(name);
        calendarButton.setName(getName() + ".calendarButton");
    }


    /**
     * Positionne la date.
     *
     * @param newDate La nouvelle date
     */
    public void setDate(final Date newDate) {
        if (!isNullDateValueAuthorized() && (newDate == null)) {
            return;
        }
        final Date oldDate = date;
        date = newDate;

        firePropertyChange(DateField.DATE_PROPERTY_NAME, ((oldDate == DateField.BAD_DATE) ? null : oldDate),
                           ((newDate == DateField.BAD_DATE) ? null : newDate));
    }


    public void setCalendarHelper(AbstractCalendarHelper calHelper) {
        if (this.calendarHelper == null && calHelper != null) {
            add(calendarButton,
                new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                                       GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        }
        else if (this.calendarHelper != null && calHelper == null) {
            this.remove(calendarButton);
        }

        this.calendarHelper = calHelper;
        repaint();
    }


    public JButton getCalendarButton() {
        return calendarButton;
    }


    /**
     * Retourne la date représenté.
     *
     * @return La date de ce <code>DateField</code>
     */
    public Date getDate() {
        if (date == BAD_DATE) {
            return null;
        }
        else {
            return date;
        }
    }


    public boolean isTooltipStandardMessageDisplayDateValue() {
        return displayMessageToDateValue;
    }


    public void setTooltipStandardMessageDisplayDateValue(boolean displayMessageToDateValue) {
        this.displayMessageToDateValue = displayMessageToDateValue;
    }


    public String getTooltipErrorMessage() {
        return tooltipErrorMessage;
    }


    public void setTooltipErrorMessage(String tooltipErrorMessage) {
        this.tooltipErrorMessage = tooltipErrorMessage;
    }


    public String getTooltipStandardMessage() {
        return tooltipStandardMessage;
    }


    public void setTooltipStandardMessage(String tooltipStandardMessage) {
        this.tooltipStandardMessage = tooltipStandardMessage;
    }


    public boolean isDefaultCalendarHelper() {
        return calendarHelper != null;
    }


    public AbstractCalendarHelper getCalHelper() {
        return calendarHelper;
    }


    protected class DisplayUpdater implements PropertyChangeListener {

        private boolean enabled = true;


        public boolean isEnabled() {
            return enabled;
        }


        public synchronized void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }


        public void propertyChange(PropertyChangeEvent evt) {
            if (!enabled) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    displayUpdater.setEnabled(false);
                    removeListeners();
                    displayDate();
                    addListeners();
                    displayUpdater.setEnabled(true);
                }
            });
        }
    }

    private class DynamicDateValidator implements DocumentListener {

        public void changedUpdate(DocumentEvent evt) {
            removeListeners();
            buildDateFromGUI();
            addListeners();
        }


        public void insertUpdate(DocumentEvent evt) {
            removeListeners();
            buildDateFromGUI();
            addListeners();
        }


        public void removeUpdate(DocumentEvent evt) {
            removeListeners();
            buildDateFromGUI();
            addListeners();
        }
    }


    /**
     * Affiche la date positionné (attribut date). Cette methode est appele automatiquement par le listener
     * <code>DisplayUpdater</code>. Si la date est <code>BAD_DATE</code> les textes contenuent dans les JTextField sont affiché en
     * rouge. Si la date est <code>null</code> alors les champs sont vidée.
     */
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

        updateDateField(date);
        setForeground(normalForegroundColor);
    }
}
