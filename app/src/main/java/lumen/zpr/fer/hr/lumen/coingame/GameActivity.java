package lumen.zpr.fer.hr.lumen.coingame;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;

/**
 * Activity koji pokrece igru s novcicima.
 */
public class GameActivity extends Activity {
    public static AssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetManager = getAssets();
        setContentView(new GamePanel(this));
    }
}
