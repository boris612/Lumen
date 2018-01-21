package lumen.zpr.fer.hr.lumen.menus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collection;

import lumen.zpr.fer.hr.lumen.GameSettingsActivity;
import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.database.DBHelper;

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
                        lumen.zpr.fer.hr.lumen.GameActivity.class);
                startActivity(startGameIntent);
            }
        });

        ImageButton startCoinGameBtn = findViewById(R.id.pokreniDruguIgru);
        startCoinGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        lumen.zpr.fer.hr.lumen.coingame.GameActivity.class);
                startActivity(startGameIntent);
            }
        });


        ImageButton openSettings = findViewById(R.id.Postavke);
        openSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        GameSettingsActivity.class);
                startGameIntent.putStringArrayListExtra("categories",(ArrayList<String>)categories);
                startActivity(startGameIntent);
            }
        });

        ImageButton seeInfo = findViewById(R.id.Info);
        seeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeInfoIntent = new Intent(MainMenuActivity.this,
                        lumen.zpr.fer.hr.lumen.InfoActivity.class);
                startActivity(seeInfoIntent);
            }
        });
    }

    public Collection<String> getCurrentCategories(){
        return categories;
    }
}
