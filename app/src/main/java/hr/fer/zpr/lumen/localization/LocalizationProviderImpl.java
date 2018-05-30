package hr.fer.zpr.lumen.localization;

import android.content.Context;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.application.ApplicationComponent;

public class LocalizationProviderImpl implements LocalizationProvider {

    private Context context;

    private String currentLanguage;

    private Properties translations;

    public LocalizationProviderImpl(Context context){this.context=context;}

    @Override
    public String getValueForLanguage(String language, String key) {
        load(language);
        return translations.getProperty(key);
    }


    @Override
    public String getKeyForLanguage(String language, String value) {
        load(language);
        for (Map.Entry<Object, Object> entry : translations.entrySet()){
            if(entry.getValue().equals(value)) return (String)entry.getKey();
        }
        return null;
    }

    private void load(String language){
        if(currentLanguage!=language ||translations==null){
            translations=new Properties();
            try {
                translations.load(new InputStreamReader(context.getAssets().open(LocalizationConstants.TRANSLATIONS_FOLDER + language + ".properties"),"UTF-8"));
            }catch(Exception e){
                Log.d("error",e.getMessage());}
        }
    }
}
