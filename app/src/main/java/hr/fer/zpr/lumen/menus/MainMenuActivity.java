package hr.fer.zpr.lumen.menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collection;

import hr.fer.zpr.lumen.GameSettingsActivity;
import hr.fer.zpr.lumen.InfoActivity;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.database.DBHelper;

public class MainMenuActivity extends Activity {
    DBHelper helper;
    private Collection<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        helper = new DBHelper(this,
                getAssets());
        helper.onCreate(helper.getWritableDatabase());
        categories = helper.getAllCategories();
        ImageButton startLetterGameBtn = findViewById(R.id.pokreniIgru);
        startLetterGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        hr.fer.zpr.lumen.ui.wordgame.WordGameActivity.class);
                startActivity(startGameIntent);
            }
        });

        ImageButton startCoinGameBtn = findViewById(R.id.pokreniDruguIgru);
        startCoinGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        hr.fer.zpr.lumen.coingame.GameActivity.class);
                startActivity(startGameIntent);
            }
        });


        ImageButton openSettings = findViewById(R.id.Postavke);
        openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        GameSettingsActivity.class);
                startGameIntent.putStringArrayListExtra("categories", (ArrayList<String>) categories);
                startActivity(startGameIntent);
            }
        });

        ImageButton seeInfo = findViewById(R.id.Info);
        seeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeInfoIntent = new Intent(MainMenuActivity.this,
                        InfoActivity.class);
                startActivity(seeInfoIntent);
            }
        });
    }

    public Collection<String> getCurrentCategories() {
        return categories;
    }
}
