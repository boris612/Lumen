package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.os.Bundle;

import lumen.zpr.fer.hr.lumen.database.DBHelper;

public class GameActivity extends Activity {
    DBHelper helper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GamePanel(this));
    }
}
