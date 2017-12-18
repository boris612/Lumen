package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import lumen.zpr.fer.hr.lumen.menus.MainMenuActivity;

/**
 * Created by Marko on 18.12.2017..
 */

public class GameSettingsActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
     final SharedPreferences pref=this.getSharedPreferences(getResources().getString(R.string.preference_file),Context.MODE_PRIVATE);
     final   SharedPreferences.Editor editor=pref.edit();
   final Button greenWhenCorrectButton = findViewById(R.id.GreenWhenCorrectButton);
   final Button addMoreLetters = findViewById(R.id.GenerateMoreLettersButton);
    if(pref.getBoolean(getResources().getString(R.string.green_on_correct),false)) greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
    else greenWhenCorrectButton.setBackgroundColor(Color.RED);
    if(pref.getBoolean(getResources().getString(R.string.add_more_letters),false)) addMoreLetters.setBackgroundColor(Color.GREEN);
    else addMoreLetters.setBackgroundColor(Color.RED);
greenWhenCorrectButton.setOnClickListener(new View.OnClickListener(){
    @Override
    public void onClick(View view){
        boolean correct=pref.getBoolean(getResources().getString(R.string.green_on_correct),false);
        if(!correct) greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
        else greenWhenCorrectButton.setBackgroundColor(Color.RED);
        editor.putBoolean(getResources().getString(R.string.green_on_correct),!correct);
        editor.commit();
    }
});
    addMoreLetters.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
          boolean more=  pref.getBoolean(getResources().getString(R.string.add_more_letters),false);

            if(!more) addMoreLetters.setBackgroundColor(Color.GREEN);
            else addMoreLetters.setBackgroundColor(Color.RED);
            editor.putBoolean(getResources().getString(R.string.add_more_letters),!more);
            editor.commit();
        }
    });

    }


}
