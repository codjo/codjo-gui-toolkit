package net.codjo.gui.toolkit.i18n;
import net.codjo.gui.toolkit.util.ErrorDialog;
import net.codjo.i18n.common.Language;
import net.codjo.i18n.common.TranslationManager;
import net.codjo.i18n.gui.TranslationNotifier;
/**
 *
 */
public class InternationalizationTestUtil {
    private InternationalizationTestUtil() {
    }


    public static void initErrorDialogTranslationBackpack() {
        TranslationManager translationManager = new TranslationManager();
        TranslationNotifier translationNotifier = new TranslationNotifier(Language.FR, translationManager);
        InternationalizationUtil.registerBundlesIfNeeded(translationManager);
        ErrorDialog.setTranslationBackpack(translationManager, translationNotifier);
    }
}
