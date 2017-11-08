package lumen.zpr.fer.hr.lumen.menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import lumen.zpr.fer.hr.lumen.GameActivity;
import lumen.zpr.fer.hr.lumen.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
