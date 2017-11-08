package lumen.zpr.fer.hr.lumen.menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lumen.zpr.fer.hr.lumen.GameActivity;
import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.database.DBHelper;

public class MainMenuActivity extends Activity {
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        helper = new DBHelper(this,
                getAssets());
        helper.onCreate(helper.getWritableDatabase());
        Button startGameBtn = findViewById(R.id.pokreniIgru);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(MainMenuActivity.this,
                        GameActivity.class);
                startActivity(startGameIntent);
            }
        });

    }
}
