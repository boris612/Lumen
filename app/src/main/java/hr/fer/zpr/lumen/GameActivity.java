package hr.fer.zpr.lumen;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;

import hr.fer.zpr.lumen.database.DBHelper;

public class GameActivity extends Activity {
    DBHelper helper = new DBHelper(this);


    private GamePanel view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view != null) return;
        view = new GamePanel(this);
        setContentView(view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.paused = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        view.updateSettings();
        view.paused = false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setContentView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.terminated = true;
    }
}
