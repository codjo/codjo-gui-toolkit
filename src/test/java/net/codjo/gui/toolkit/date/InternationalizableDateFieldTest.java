package net.codjo.gui.toolkit.date;
import net.codjo.i18n.common.Language;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.TranslationNotifier;
import net.codjo.test.common.DateUtil;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class InternationalizableDateFieldTest {

    @Test
    public void testUpdateTranslation() throws Exception {
        TranslationManager translationManager = new TranslationManager();
        TranslationNotifier translationNotifier = new TranslationNotifier(Language.FR, translationManager);

        translationNotifier.setLanguage(Language.FR);

        DateField dateField = new DateField();
        dateField.setDate(DateUtil.createDate("1998-01-05"));
        InternationalizableDateField i18nDateField = new InternationalizableDateField(dateField,
                                                                                      translationNotifier,
                                                                                      translationManager);
        translationNotifier.addInternationalizableComponent(i18nDateField);

        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getDayField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelA().getToolTipText(), nullValue());
        assertThat(dateField.getMonthField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelB().getToolTipText(), nullValue());
        assertThat(dateField.getYearField().getToolTipText(), equalTo("lundi 5 janvier 1998"));

        translationNotifier.setLanguage(Language.EN);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getDayField().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getSlashlabelA().getToolTipText(), nullValue());
        assertThat(dateField.getMonthField().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getSlashlabelB().getToolTipText(), nullValue());
        assertThat(dateField.getYearField().getToolTipText(), equalTo("Monday, January 5, 1998"));

        translationNotifier.setLanguage(Language.FR);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getDayField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelA().getToolTipText(), nullValue());
        assertThat(dateField.getMonthField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelB().getToolTipText(), nullValue());
        assertThat(dateField.getYearField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
    }

    @Test
    public void testPopupUpdateTranslation() throws Exception {
        TranslationManager translationManager = new TranslationManager();
        TranslationNotifier translationNotifier = new TranslationNotifier(Language.FR, translationManager);

        translationNotifier.setLanguage(Language.FR);

        PopupDateField dateField = new PopupDateField();
        dateField.setDate(DateUtil.createDate("1998-01-05"));
        InternationalizableDateField i18nDateField = new InternationalizableDateField(dateField,
                                                                                      translationNotifier,
                                                                                      translationManager);
        translationNotifier.addInternationalizableComponent(i18nDateField);

        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getDateTextField().getToolTipText(), equalTo("lundi 5 janvier 1998"));

        translationNotifier.setLanguage(Language.EN);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getDateTextField().getToolTipText(), equalTo("Monday, January 5, 1998"));

        translationNotifier.setLanguage(Language.FR);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getDateTextField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
    }
}
