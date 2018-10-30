package hr.fer.zpr.lumen.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameCoinAmountUseCase;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameLanguageUseCase;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.*;
import wordgame.db.database.WordGameDatabase;

import javax.inject.Inject;

public class WordGameSettingsActivity extends DaggerActivity {

    private Button greenWhenCorrectBtn;
    private Button addMoreLettersBtn;
    private Button showAllLettersBtn;
    private ImageButton returnBtn;
    private Button categoriesBtn;
    private Button validateWordBtn;
    private Button validateLetterByLetterBtn;

    @Inject
    ChangeGreenOnCorrectUseCase changeGreenOnCorrectUseCase;

    @Inject
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase;

    @Inject
    ChangeCreateAllLettersUseCase changeCreateAllLettersUseCase;

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

    SharedPreferences pref;

    @Inject
    WordGameDatabase database;

    @Inject
    LocalizationProvider localizationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_wordgame_settings);
        pref = getSharedPreferences("Lumen", Context.MODE_PRIVATE);

        showAllLettersBtn = findViewById(R.id.GenerateAllLettersButton);
        returnBtn = findViewById(R.id.returnButton);
        greenWhenCorrectBtn = findViewById(R.id.GreenWhenCorrectButton);
        addMoreLettersBtn = findViewById(R.id.GenerateMoreLettersButton);
        categoriesBtn = findViewById(R.id.categoryButton);
        validateWordBtn = findViewById(R.id.ValidateAllLetters);
        validateLetterByLetterBtn = findViewById(R.id.ValidateLetterByLetter);

        final SharedPreferences.Editor editor = pref.edit();

        if (pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false))
            greenWhenCorrectBtn.setBackgroundColor(Color.GREEN);
        else greenWhenCorrectBtn.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false))
            addMoreLettersBtn.setBackgroundColor(Color.GREEN);
        else addMoreLettersBtn.setBackgroundColor(Color.RED);

        greenWhenCorrectBtn.setOnClickListener(e -> {
            boolean correct = pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false);
            if (!correct) greenWhenCorrectBtn.setBackgroundColor(Color.GREEN);
            else greenWhenCorrectBtn.setBackgroundColor(Color.RED);
            editor.putBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, !correct);
            editor.commit();
            changeGreenOnCorrectUseCase.execute(!correct).subscribe();
        });

//        addMoreLettersBtn.setOnClickListener(e -> {
//            boolean more = pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false);
//            if (!more) addMoreLettersBtn.setBackgroundColor(Color.GREEN);
//            else addMoreLettersBtn.setBackgroundColor(Color.RED);
//            editor.putBoolean(ViewConstants.PREFERENCES_LETTERS, !more);
//            editor.commit();
//            changeCreateMoreLettersUseCase.execute(!more).subscribe();
//        });
//
//        showAllLettersBtn.setOnClickListener(e -> {
//            boolean allLetters = pref.getBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, false);
//            if (!allLetters) showAllLettersBtn.setBackgroundColor(Color.GREEN);
//            else showAllLettersBtn.setBackgroundColor(Color.RED);
//            editor.putBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, !allLetters);
//            editor.commit();
//            changeCreateAllLettersUseCase.execute(!allLetters).subscribe();
//        });
//
//        categoriesBtn.setOnClickListener(e -> {
//            Intent categoryIntent = new Intent(WordGameSettingsActivity.this,
//                    CategorySelectionActivity.class);
//            startActivity(categoryIntent);
//        });

        returnBtn.setOnClickListener(e -> onBackPressed());


    }
}
