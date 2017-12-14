package lumen.zpr.fer.hr.lumen.coingame;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity koji pokrece igru s novcicima.
 */
public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GamePanel(this));
    }
}
