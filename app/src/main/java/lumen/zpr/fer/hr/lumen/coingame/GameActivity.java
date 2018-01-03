package lumen.zpr.fer.hr.lumen.coingame;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Activity koji pokrece igru s novcicima.
 */
public class GameActivity extends Activity {
    public static AssetManager assetManager;

    private  GamePanel view=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view!=null) return;
        assetManager = getAssets();
        view=new GamePanel(this);
        setContentView(view);
    }

    @Override
    protected void onPause(){
        super.onPause();
        view.paused=true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        view.paused=false;
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        setContentView(view);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        view.terminated=true;
    }
}
