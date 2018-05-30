package hr.fer.zpr.lumen.localization;

public interface LocalizationProvider {

    String getValueForLanguage(String language, String key);

    String getKeyForLanguage(String language,String value);
}
