package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lumen.zpr.fer.hr.lumen.database.DBHelper;

public class GameActivity extends Activity {
    DBHelper helper = new DBHelper(this);


    private GamePanel view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=new GamePanel(this);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.updateSettings();
    }
}
