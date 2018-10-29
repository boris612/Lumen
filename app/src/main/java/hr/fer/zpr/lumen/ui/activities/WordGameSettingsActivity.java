package hr.fer.zpr.lumen.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;

public class WordGameSettingsActivity extends DaggerActivity {

    private Button greenWhenCorrectButton;
    private Button addMoreLetters;
    private Button showAllLetters;
    private ImageButton returnBtn;
    private Button categories;
    private Button validateWord;
    private Button validateLetterByLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_wordgame_settings);

        showAllLetters = findViewById(R.id.GenerateAllLettersButton);
        returnBtn = findViewById(R.id.returnButton);
        greenWhenCorrectButton = findViewById(R.id.GreenWhenCorrectButton);
        addMoreLetters = findViewById(R.id.GenerateMoreLettersButton);
        categories = findViewById(R.id.categoryButton);


        returnBtn.setOnClickListener(e -> onBackPressed());
    }
}
