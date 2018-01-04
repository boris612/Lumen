package lumen.zpr.fer.hr.lumen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

    Button categoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences pref = this.getSharedPreferences(getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final Button greenWhenCorrectButton = findViewById(R.id.GreenWhenCorrectButton);
        final Button addMoreLetters = findViewById(R.id.GenerateMoreLettersButton);
        final EditText coinNumber = findViewById(R.id.coinNumber);
        categoryBtn = findViewById(R.id.categoryButton);
        final ImageButton returnBtn = findViewById(R.id.returnButton);
        coinNumber.setText(String.valueOf(pref.getInt(getResources().getString(R.string.coins),0)));

        if (pref.getBoolean(getResources().getString(R.string.green_on_correct), false))
            greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
        else greenWhenCorrectButton.setBackgroundColor(Color.RED);
        if (pref.getBoolean(getResources().getString(R.string.add_more_letters), false))
            addMoreLetters.setBackgroundColor(Color.GREEN);
        else addMoreLetters.setBackgroundColor(Color.RED);

        String currentCategory = pref.getString("category", "sve");
        categoryBtn.setText("Kategorija: " + currentCategory, TextView.BufferType.EDITABLE);


        greenWhenCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean correct = pref.getBoolean(getResources().getString(R.string.green_on_correct), false);
                if (!correct) greenWhenCorrectButton.setBackgroundColor(Color.GREEN);
                else greenWhenCorrectButton.setBackgroundColor(Color.RED);
                editor.putBoolean(getResources().getString(R.string.green_on_correct), !correct);
                editor.commit();
            }
        });
        addMoreLetters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean more = pref.getBoolean(getResources().getString(R.string.add_more_letters), false);

                if (!more) addMoreLetters.setBackgroundColor(Color.GREEN);
                else addMoreLetters.setBackgroundColor(Color.RED);
                editor.putBoolean(getResources().getString(R.string.add_more_letters), !more);
                editor.commit();
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(GameSettingsActivity.this,
                        lumen.zpr.fer.hr.lumen.CategorySelectionActivity.class);
                if (getIntent().getStringArrayListExtra("categories") == null)
                    System.out.println("i tu je null");
                categoryIntent.putStringArrayListExtra("categories", getIntent().getStringArrayListExtra("categories"));
                startActivity(categoryIntent);

            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt(getResources().getString(R.string.coins),Integer.parseInt(String.valueOf(coinNumber.getText())));
                editor.commit();

                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        final SharedPreferences pref = this.getSharedPreferences(getResources().getString(R.string.preference_file), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        String currentCategory = pref.getString("category", "sve");
        categoryBtn.setText("Kategorija: " + currentCategory, TextView.BufferType.EDITABLE);


    }

}
