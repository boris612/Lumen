package hr.fer.zpr.lumen.localization;

public interface LocalizationProvider {

    String getValueForLanguage(String language, String key);

    String getCurrentLanguage();
}
