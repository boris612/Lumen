package hr.fer.zpr.lumen.localization;

import android.content.Context;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import hr.fer.zpr.lumen.ui.DebugUtil;

public class LocalizationProviderImpl implements LocalizationProvider {

    private Context context;

    private String currentLanguage;

    private Properties translations;

    public LocalizationProviderImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getValueForLanguage(String language, String key) {
        currentLanguage = language;
        load(language);
        return translations.getProperty(key);
    }

    @Override
    public String getCurrentLanguage() {
        return currentLanguage;
    }

    private void load(String language) {
        if (currentLanguage != language || translations == null) {
            translations = new Properties();
            try {
                translations.load(new InputStreamReader(context.getAssets().open(LocalizationConstants.TRANSLATIONS_FOLDER + language + ".properties"), "UTF-8"));
            } catch (Exception e) {
                DebugUtil.LogDebug(e);
            }
        }
    }
}
