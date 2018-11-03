package hr.fer.zpr.lumen.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;

public class MainMenuActivity extends DaggerActivity {

    ImageButton startLetterGameBtn;
    ImageButton startCoinGameBtn;
    ImageButton openSettings;
    ImageButton seeInfo;
    ImageButton startNumberGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component.inject(this);
        setContentView(R.layout.activity_main_menu);

        startNumberGame = findViewById(R.id.pokreniNumberGame);
        startLetterGameBtn = findViewById(R.id.pokreniIgru);
        startCoinGameBtn = findViewById(R.id.pokreniDruguIgru);
        openSettings = findViewById(R.id.Postavke);
        seeInfo = findViewById(R.id.Info);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        startLetterGameBtn.setOnClickListener(e -> {
            Intent game = new Intent(this, hr.fer.zpr.lumen.ui.wordgame.WordGameActivity.class);
            this.startActivity(game);
        });

        startCoinGameBtn.setOnClickListener(e -> {
            Intent game = new Intent(this, hr.fer.zpr.lumen.ui.coingame.activity.CoinGameActivity.class);
            this.startActivity(game);
        });

        startNumberGame.setOnClickListener(e -> {
            Intent game = new Intent(this, hr.fer.zpr.lumen.ui.numbergame.activity.NumberGameActivity.class);
            this.startActivity(game);
        });

        openSettings.setOnClickListener(e -> {
            Intent settings = new Intent(this, GameSettingsActivity.class);
            this.startActivity(settings);
        });

        seeInfo.setOnClickListener(e -> {
            Intent info = new Intent(this, InfoActivity.class);
            this.startActivity(info);
        });

    }

}
