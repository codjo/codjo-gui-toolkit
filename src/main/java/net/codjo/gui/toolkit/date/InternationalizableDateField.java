package net.codjo.gui.toolkit.date;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JComponent;
import net.codjo.i18n.common.Language;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.AbstractInternationalizableComponent;
import net.codjo.i18n.gui.TranslationNotifier;

public class InternationalizableDateField extends AbstractInternationalizableComponent<DateField> {
    private WeakReference<AbstractDateField> reference;


    public InternationalizableDateField(DateField dateField,
                                        final TranslationNotifier notifier,
                                        final TranslationManager manager) {
        super("NO_KEY");
        this.reference = new WeakReference<AbstractDateField>(dateField);

        PropertyChangeListener listener = new PropertyChangeListener() {
            private boolean isUpdating = false;


            public void propertyChange(PropertyChangeEvent evt) {
                if (isUpdating) {
                    return;
                }
                isUpdating = true;
                updateTranslation(notifier.getLanguage(), manager);
                isUpdating = false;
            }
        };

        dateField.addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getDayField().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getMonthField().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getYearField().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getCalendarButton().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getCalHelper().setLocale(notifier.getLanguage().getLocale());
    }


    public InternationalizableDateField(PopupDateField dateField,
                                        final TranslationNotifier notifier,
                                        final TranslationManager manager) {
        super("NO_KEY");
        this.reference = new WeakReference<AbstractDateField>(dateField);

        PropertyChangeListener listener = new PropertyChangeListener() {
            private boolean isUpdating = false;


            public void propertyChange(PropertyChangeEvent evt) {
                if (isUpdating) {
                    return;
                }
                isUpdating = true;
                updateTranslation(notifier.getLanguage(), manager);
                isUpdating = false;
            }
        };

        dateField.addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getDateTextField().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getCalendarButton().addPropertyChangeListener(JComponent.TOOL_TIP_TEXT_KEY, listener);
        dateField.getCalHelper().setLocale(notifier.getLanguage().getLocale());
    }


    public AbstractDateField getComponent() {
        return reference.get();
    }


    public void updateTranslation(Language language, TranslationManager translator) {
        AbstractDateField dateField = reference.get();
        if (dateField == null) {
            return;
        }
        dateField.getCalHelper().setLocale(language.getLocale());

        Date date = dateField.getDate();
        if (date != null) {
            Locale locale = language.getLocale();
            String dateToDisplay = DateFormat.getDateInstance(DateFormat.FULL, locale).format(date);
            dateField.setToolTipText(dateToDisplay);
        }
        else {
            dateField.setToolTipText(null);
        }
    }
}
