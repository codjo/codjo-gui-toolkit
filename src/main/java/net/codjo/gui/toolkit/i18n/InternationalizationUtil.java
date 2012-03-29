package net.codjo.gui.toolkit.i18n;
import net.codjo.i18n.common.Language;
import net.codjo.i18n.common.TranslationManager;
/**
 *
 */
public class InternationalizationUtil {
    private InternationalizationUtil() {
    }

    public static void registerBundlesIfNeeded(TranslationManager translationManager) {
        translationManager.addBundleOnlyIfNeeded("net.codjo.gui.toolkit.i18n", Language.FR, "AboutWindow.title");
        translationManager.addBundleOnlyIfNeeded("net.codjo.gui.toolkit.i18n", Language.EN, "AboutWindow.title");
    }
}
