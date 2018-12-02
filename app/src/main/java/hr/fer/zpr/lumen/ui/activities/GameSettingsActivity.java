package hr.fer.zpr.lumen.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameCoinAmountUseCase;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameLanguageUseCase;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationConstants;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.*;
import wordgame.db.database.WordGameDatabase;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Marko on 18.12.2017..
 */

public class GameSettingsActivity extends DaggerActivity {

    @Inject
    ChangeGreenOnCorrectUseCase changeGreenOnCorrectUseCase;

    @Inject
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase;

    @Inject
    ChangeLanguageUseCase changeLanguageUseCase;

    @Inject
    ChangeCoinGameLanguageUseCase changeCoinGameLanguageUseCase;

    @Inject
    ChangeCoinGameCoinAmountUseCase changeCoinGameCoinAmountUseCase;

    @Inject
    ChangeWordGameCoinAmountUseCase changeWordGameCoinAmountUseCase;

    @Inject
    ChangeMessagesLanguageUseCase changeMessagesLanguageUseCase;

    @Inject
    SharedPreferences pref;

    @Inject
    WordGameDatabase database;

    @Inject
    LocalizationProvider localizationProvider;

    private EditText coinNumber;
    private Button languageButton;
    private ImageButton returnBtn;
    private TextView settings;
    private TextView coins;
    private Button guiLanguageButton;
    private Button wordGameBtn;
    private Button numberGameBtn;
    private Button coinGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_settings);

        final SharedPreferences.Editor editor = pref.edit();

        settings = findViewById(R.id.textView);
        coins = findViewById(R.id.textView3);
        coinNumber = findViewById(R.id.coinNumber);
        languageButton = findViewById(R.id.languageButton);
        returnBtn = findViewById(R.id.returnButton);
        guiLanguageButton = findViewById(R.id.guilanguageButton);
        wordGameBtn=findViewById(R.id.wordGameSettings);
        numberGameBtn = findViewById(R.id.numberGameSettings);
        coinGameBtn=findViewById(R.id.coinGameSettings);
        coinNumber.setText(String.valueOf(pref.getInt(getResources().getString(R.string.coins), 0)));
        setLanguageValues(pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE));


        String language = pref.getString(ViewConstants.PREFERENCES_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE);
        String value = localizationProvider.getValueForLanguage(language, LocalizationConstants.WORDS_LANGUAGE_PROPERTY);
        languageButton.setText(value + ":" + localizationProvider.getValueForLanguage(language, language));

        guiLanguageButton.setOnClickListener(e -> {
            List<String> languages = database.languageDao().getAllLanguagesString();
            String currLanguage = pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE);
            String newLanguage = languages.get((languages.indexOf(currLanguage) + 1) % languages.size());
            editor.putString(ViewConstants.PREFERENCES_GUI_LANGUAGE, newLanguage);
            editor.commit();
            setLanguageValues(newLanguage);
            changeCoinGameLanguageUseCase.execute(newLanguage).subscribe();
            changeMessagesLanguageUseCase.execute(newLanguage).subscribe();

        });
        languageButton.setOnClickListener(e -> {
            List<String> languages = database.languageDao().getAllLanguagesString();
            String currLanguage = pref.getString(ViewConstants.PREFERENCES_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE);
            String newLanguage = languages.get((languages.indexOf(currLanguage) + 1) % languages.size());
            String guiLanguage = pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_GUI_LANGUAGE);
            languageButton.setText(localizationProvider.getValueForLanguage(guiLanguage, LocalizationConstants.WORDS_LANGUAGE_PROPERTY) + ":" + localizationProvider.getValueForLanguage(guiLanguage, newLanguage));
            editor.putString(ViewConstants.PREFERENCES_LANGUAGE, newLanguage);
            editor.commit();
            changeLanguageUseCase.execute(newLanguage).subscribe();

        });


        returnBtn.setOnClickListener(e -> {
            if (coinNumber.getText().toString().isEmpty()) coinNumber.setText("0");
            editor.putInt(ViewConstants.PREFERENCES_COINS, Integer.parseInt(String.valueOf(coinNumber.getText())));
            editor.commit();
            int coins = Integer.parseInt(String.valueOf(coinNumber.getText()));
            if (coins < 0) coins = 0;
            changeCoinGameCoinAmountUseCase.execute(coins).subscribe();
            changeWordGameCoinAmountUseCase.execute(coins).subscribe();
            onBackPressed();
        });

        wordGameBtn.setOnClickListener(e -> {
            Intent settings = new Intent(this, WordGameSettingsActivity.class);
            this.startActivity(settings);
        });

        numberGameBtn.setOnClickListener(e -> {
            Intent settings = new Intent(this, NumberGameSettingsActivity.class);
            this.startActivity(settings);
        });
    }


    private void setLanguageValues(String newLanguage) {
        guiLanguageButton.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.GUI_LANGUAGE_PROPERTY) + ":" + localizationProvider.getValueForLanguage(newLanguage, newLanguage));
        settings.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.SETTINGS_PROPERTY));
        coins.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.NUMBER_OF_COINS_PROPERTY));
        String words_language = pref.getString(ViewConstants.PREFERENCES_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE);
        languageButton.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.WORDS_LANGUAGE_PROPERTY) + ":" + localizationProvider.getValueForLanguage(newLanguage, words_language));
        wordGameBtn.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.WORD_GAME_SETTINGS_PROPERTY));
        coinGameBtn.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.COIN_GAME_SETTINGS_PROPERTY));
        numberGameBtn.setText(localizationProvider.getValueForLanguage(newLanguage,LocalizationConstants.CALCULATING_GAME_SETTINGS_PROPERTY));
    }

}
