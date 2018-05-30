package hr.fer.zpr.lumen.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationConstants;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeCreateMoreLettersUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeGreenOnCorrectUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeLanguageUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import wordgame.db.database.WordGameDatabase;

/**
 * Created by Marko on 18.12.2017..
 */

public class GameSettingsActivity extends DaggerActivity {

    Button categoryBtn;

    @Inject
    WordGameManager manager;

    @Inject
    ChangeGreenOnCorrectUseCase changeGreenOnCorrectUseCase;

    @Inject
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase;

    @Inject
    ChangeLanguageUseCase changeLanguageUseCase;

    @Inject
    SharedPreferences pref;

    @Inject
    WordGameDatabase database;

    @Inject
    LocalizationProvider localizationProvider;

    private  Button greenWhenCorrectButton;
    private Button addMoreLetters;
    private EditText coinNumber;
    private Button languageButton;
    private ImageButton returnBtn;
    private TextView settings;
    private TextView coins;
    private Button guiLanguageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_settings);

        final SharedPreferences.Editor editor = pref.edit();

        settings =findViewById(R.id.textView);
        coins=findViewById(R.id.textView3);
        greenWhenCorrectButton = findViewById(R.id.GreenWhenCorrectButton);
        addMoreLetters = findViewById(R.id.GenerateMoreLettersButton);
        coinNumber = findViewById(R.id.coinNumber);
        languageButton=findViewById(R.id.languageButton);
        categoryBtn = findViewById(R.id.categoryButton);
        returnBtn = findViewById(R.id.returnButton);
        guiLanguageButton=findViewById(R.id.guilanguageButton);
        coinNumber.setText(String.valueOf(pref.getInt(getResources().getString(R.string.coins), 0)));
        setLanguageValues(pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE,ViewConstants.DEFAULT_LANGUAGE));
        if (pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false))
            greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
        else greenWhenCorrectButton.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false))
            addMoreLetters.setBackgroundColor(Color.GREEN);
        else addMoreLetters.setBackgroundColor(Color.RED);

        String language=pref.getString(ViewConstants.PREFERENCES_LANGUAGE,ViewConstants.DEFAULT_LANGUAGE);
        String value=localizationProvider.getValueForLanguage(language, LocalizationConstants.WORDS_LANGUAGE_PROPERTY);
        languageButton.setText(value+":"+localizationProvider.getValueForLanguage(language,language));

        guiLanguageButton.setOnClickListener(e->{
            List<String> languages=database.languageDao().getAllLanguagesString();
            String currLanguage=pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE,ViewConstants.DEFAULT_LANGUAGE);
            String newLanguage=languages.get((languages.indexOf(currLanguage)+1)%languages.size());
            editor.putString(ViewConstants.PREFERENCES_GUI_LANGUAGE,newLanguage);
            editor.commit();
            setLanguageValues(newLanguage);

        });
        languageButton.setOnClickListener(e->{
            List<String> languages=database.languageDao().getAllLanguagesString();
            String currLanguage=pref.getString(ViewConstants.PREFERENCES_LANGUAGE,ViewConstants.DEFAULT_LANGUAGE);
            String newLanguage=languages.get((languages.indexOf(currLanguage)+1)%languages.size());
            String guiLanguage=pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE,ViewConstants.DEFAULT_GUI_LANGUAGE);
            languageButton.setText(localizationProvider.getValueForLanguage(guiLanguage,LocalizationConstants.WORDS_LANGUAGE_PROPERTY)+":"+localizationProvider.getValueForLanguage(guiLanguage,newLanguage));
            editor.putString(ViewConstants.PREFERENCES_LANGUAGE,newLanguage);
            editor.commit();
            changeLanguageUseCase.execute(newLanguage);

        });
        greenWhenCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean correct = pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false);
                if (!correct) greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
                else greenWhenCorrectButton.setBackgroundColor(Color.RED);
                editor.putBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, !correct);
                editor.commit();
                changeGreenOnCorrectUseCase.execute(!correct).blockingGet();

            }
        });
        addMoreLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean more = pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false);

                if (!more) addMoreLetters.setBackgroundColor(Color.GREEN);
                else addMoreLetters.setBackgroundColor(Color.RED);
                editor.putBoolean(ViewConstants.PREFERENCES_LETTERS, !more);
                editor.commit();
                changeCreateMoreLettersUseCase.execute(!more).blockingGet();
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(GameSettingsActivity.this,
                        CategorySelectionActivity.class);
                startActivity(categoryIntent);

            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coinNumber.getText().toString().isEmpty()) coinNumber.setText("0");
                editor.putInt(ViewConstants.PREFERENCES_COINS, Integer.parseInt(String.valueOf(coinNumber.getText())));
                editor.commit();

                onBackPressed();
            }
        });

    }

    private void setLanguageValues(String newLanguage) {
        guiLanguageButton.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.GUI_LANGUAGE_PROPERTY)+":"+localizationProvider.getValueForLanguage(newLanguage,newLanguage));
        categoryBtn.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.CATEGORIES_PROPERTY));
        settings.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.SETTINGS_PROPERTY));
        greenWhenCorrectButton.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.GREEN_ON_CORRECT_PROPERTY));
        addMoreLetters.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.CREATE_MORE_LETTERS_PROPERTY));
        coins.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.NUMBER_OF_COINS_PROPERTY));
        String words_language=pref.getString(ViewConstants.PREFERENCES_LANGUAGE,ViewConstants.DEFAULT_LANGUAGE);
        languageButton.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.WORDS_LANGUAGE_PROPERTY)+":"+localizationProvider.getValueForLanguage(newLanguage,words_language));

    }

}
