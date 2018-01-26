package hr.fer.zpr.lumen;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import hr.fer.zpr.lumen.R;

/**
 * Created by Zlatko on 21-Jan-18.
 */

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        final ImageButton returnBtn = findViewById(R.id.mainMenuButton);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
