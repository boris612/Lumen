package hr.fer.zpr.lumen.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import hr.fer.zpr.lumen.R;

public class NumberGameSettingsActivity extends Activity {

    private ImageButton returnBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbergame_settings);

        CheckBox addition = findViewById(R.id.additionCB);
        RadioGroup additionGroup = findViewById(R.id.addition);
        enableDisableRadioButtons(addition, additionGroup);
        addition.setOnClickListener(v->{
            enableDisableRadioButtons(addition, additionGroup);
        });

        CheckBox subtraction = findViewById(R.id.subtractionCB);
        RadioGroup subtractionGroup = findViewById(R.id.subtraction);
        enableDisableRadioButtons(subtraction, subtractionGroup);
        subtraction.setOnClickListener(v->{
            enableDisableRadioButtons(subtraction, subtractionGroup);
        });

        CheckBox multiplication = findViewById(R.id.multiplicationCB);
        RadioGroup multiplicationGroup = findViewById(R.id.multiplication);
        enableDisableRadioButtons(multiplication, multiplicationGroup);
        multiplication.setOnClickListener(v->{
            enableDisableRadioButtons(multiplication, multiplicationGroup);
        });

        CheckBox division = findViewById(R.id.divisionCB);
        RadioGroup divisionGroup = findViewById(R.id.division);
        enableDisableRadioButtons(division, divisionGroup);
        division.setOnClickListener(v->{
            enableDisableRadioButtons(division, divisionGroup);
        });

        returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(v->onBackPressed());


    }

    public void enableDisableRadioButtons(CheckBox checkBox, RadioGroup group){
        if(checkBox.isChecked()){
            for (int i = 0; i < group.getChildCount(); i++) {
                if(i == 0){
                    ((RadioButton)group.getChildAt(i)).setChecked(true);
                }
                group.getChildAt(i).setEnabled(true);
            }
        } else {
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setEnabled(false);
            }
        }
    }

}