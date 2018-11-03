package hr.fer.zpr.lumen.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.*;

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
    ChangeGreenInstantlyUseCase changeGreenInstantlyUseCase;

    @Inject
    ChangeGreenWhenFullUseCase changeGreenWhenFullUseCase;

    @Inject
    ChangeCreateMoreLettersUseCase changeCreateMoreLettersUseCase;

    @Inject
    ChangeCreateAllLettersUseCase changeCreateAllLettersUseCase;

    @Inject
    SharedPreferences pref;

    @Inject
    LocalizationProvider localizationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_wordgame_settings);

        showAllLettersBtn = findViewById(R.id.GenerateAllLettersButton);
        returnBtn = findViewById(R.id.returnButton);
        greenWhenCorrectBtn = findViewById(R.id.GreenWhenCorrectButton);
        addMoreLettersBtn = findViewById(R.id.GenerateMoreLettersButton);
        categoriesBtn = findViewById(R.id.categoryButton);
        validateWordBtn = findViewById(R.id.ValidateAllLetters);
        validateLetterByLetterBtn = findViewById(R.id.ValidateLetterByLetter);

        final SharedPreferences.Editor editor = pref.edit();

        setButtonsInitialColor();

        greenWhenCorrectBtn.setOnClickListener(e -> {
            boolean correct = pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false);
            if (!correct) greenWhenCorrectBtn.setBackgroundColor(Color.GREEN);
            else greenWhenCorrectBtn.setBackgroundColor(Color.RED);
            editor.putBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, !correct);
            editor.commit();
            changeGreenOnCorrectUseCase.execute(!correct).subscribe();
        });

        addMoreLettersBtn.setOnClickListener(e -> {
            boolean more = pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false);
            if (more){
                addMoreLettersBtn.setBackgroundColor(Color.GREEN);
                showAllLettersBtn.setBackgroundColor(Color.RED);
            }
            else addMoreLettersBtn.setBackgroundColor(Color.RED);
            editor.putBoolean(ViewConstants.PREFERENCES_LETTERS, !more);
            editor.putBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, more);
            editor.commit();
            changeCreateMoreLettersUseCase.execute(!more).subscribe();
        });

        showAllLettersBtn.setOnClickListener(e -> {
            boolean allLetters = pref.getBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, false);
            if (allLetters) {
                showAllLettersBtn.setBackgroundColor(Color.GREEN);
                addMoreLettersBtn.setBackgroundColor(Color.RED);
            } else {
                showAllLettersBtn.setBackgroundColor(Color.RED);
            }
            editor.putBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, !allLetters);
            editor.putBoolean(ViewConstants.PREFERENCES_LETTERS, allLetters);
            editor.commit();
            changeCreateAllLettersUseCase.execute(!allLetters).subscribe();
        });

        validateWordBtn.setOnClickListener(e -> {
            boolean validateWord = pref.getBoolean(ViewConstants.PREFERENCES_VALIDATE_WORD, false);
            if (!validateWord) validateWordBtn.setBackgroundColor(Color.GREEN);
            else validateWordBtn.setBackgroundColor(Color.RED);
            editor.putBoolean(ViewConstants.PREFERENCES_VALIDATE_WORD, !validateWord);
            editor.commit();
            changeGreenWhenFullUseCase.execute(!validateWord).subscribe();
        });

        validateLetterByLetterBtn.setOnClickListener(e -> {
            boolean validateLetters = pref.getBoolean(ViewConstants.PREFERENCES_VALIDATE_LETTERS, false);
            if (!validateLetters) validateLetterByLetterBtn.setBackgroundColor(Color.GREEN);
            else validateLetterByLetterBtn.setBackgroundColor(Color.RED);
            editor.putBoolean(ViewConstants.PREFERENCES_VALIDATE_LETTERS, !validateLetters);
            editor.commit();
            changeGreenInstantlyUseCase.execute(!validateLetters).subscribe();
        });

        categoriesBtn.setOnClickListener(e -> {
            Intent categoryIntent = new Intent(WordGameSettingsActivity.this,
                    CategorySelectionActivity.class);
            startActivity(categoryIntent);
        });

        returnBtn.setOnClickListener(e -> onBackPressed());


    }

    private void setButtonsInitialColor() {
        if (pref.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false))
            greenWhenCorrectBtn.setBackgroundColor(Color.GREEN);
        else greenWhenCorrectBtn.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_LETTERS, false))
            addMoreLettersBtn.setBackgroundColor(Color.GREEN);
        else addMoreLettersBtn.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_ALL_LETTERS, false))
            showAllLettersBtn.setBackgroundColor(Color.GREEN);
        else showAllLettersBtn.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_VALIDATE_LETTERS, false))
            validateLetterByLetterBtn.setBackgroundColor(Color.GREEN);
        else validateLetterByLetterBtn.setBackgroundColor(Color.RED);
        if (pref.getBoolean(ViewConstants.PREFERENCES_VALIDATE_WORD, false))
            validateWordBtn.setBackgroundColor(Color.GREEN);
        else validateWordBtn.setBackgroundColor(Color.RED);
    }

}
