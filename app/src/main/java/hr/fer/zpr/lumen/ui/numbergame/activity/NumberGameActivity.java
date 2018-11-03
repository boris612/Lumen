package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;

public class NumberGameActivity extends DaggerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);
    }
}
