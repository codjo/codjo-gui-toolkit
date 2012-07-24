package net.codjo.gui.toolkit.date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.codjo.i18n.common.Language;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.TranslationNotifier;
import net.codjo.test.common.DateUtil;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpecTestCase;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class InternationalizableDateFieldTest extends UISpecTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testUpdateTranslation() throws Exception {
        TranslationManager translationManager = new TranslationManager();
        TranslationNotifier translationNotifier = new TranslationNotifier(Language.FR, translationManager);

        translationNotifier.setLanguage(Language.FR);

        final JFrame frame = new JFrame("DateField");
        final DateField dateField = new DateField();
        frame.setContentPane(dateField);

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

        final Window mainWindow = WindowInterceptor.run(new Trigger() {
            public void run() throws Exception {
                frame.setVisible(true);
            }
        });

        assertPopupCalendar(mainWindow, "lundi 5 janvier 1998");

        translationNotifier.setLanguage(Language.EN);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getDayField().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getSlashlabelA().getToolTipText(), nullValue());
        assertThat(dateField.getMonthField().getToolTipText(), equalTo("Monday, January 5, 1998"));
        assertThat(dateField.getSlashlabelB().getToolTipText(), nullValue());
        assertThat(dateField.getYearField().getToolTipText(), equalTo("Monday, January 5, 1998"));

        assertPopupCalendar(mainWindow, "Monday, January 5, 1998");

        translationNotifier.setLanguage(Language.FR);
        assertThat(i18nDateField.getComponent().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getCalendarButton().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getDayField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelA().getToolTipText(), nullValue());
        assertThat(dateField.getMonthField().getToolTipText(), equalTo("lundi 5 janvier 1998"));
        assertThat(dateField.getSlashlabelB().getToolTipText(), nullValue());
        assertThat(dateField.getYearField().getToolTipText(), equalTo("lundi 5 janvier 1998"));

        assertPopupCalendar(mainWindow, "lundi 5 janvier 1998");
    }


    private void assertPopupCalendar(final Window mainWindow, final String dateHelpLabel) {
        WindowInterceptor.init(new Trigger() {
            public void run() throws Exception {
                mainWindow.getButton().click();
            }
        }).process(new WindowHandler() {
            @Override
            public Trigger process(final Window window) throws Exception {
                TextBox helpLabelTextBox = window.getTextBox("HelpLabel");
                JLabel helpLabel = (JLabel)helpLabelTextBox.getAwtComponent();
                assertThat(helpLabel.getText(), equalTo(dateHelpLabel));
                return window.getButton("Calendar.OK").triggerClick();
            }
        }).run();
    }


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
