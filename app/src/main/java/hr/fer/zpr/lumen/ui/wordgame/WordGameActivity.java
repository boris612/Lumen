package hr.fer.zpr.lumen.ui.wordgame;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class WordGameActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WordGameView(this));
    }
}
