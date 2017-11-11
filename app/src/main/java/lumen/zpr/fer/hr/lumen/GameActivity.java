package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lumen.zpr.fer.hr.lumen.database.DBHelper;

public class GameActivity extends Activity {
    DBHelper helper = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GamePanel(this));

        //test
        List<String> list = new ArrayList<String>();
        list.add("zvucniZapisi/hrvatskaAbeceda/b.mp3");

        GameSound gs = new GameSound(this,"zvucniZapisi/hrvatskaAbeceda/a.mp3",list);
     //   gs.wordRecording.start();


    }
}
