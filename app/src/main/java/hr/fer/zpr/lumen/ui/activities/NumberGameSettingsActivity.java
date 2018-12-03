package hr.fer.zpr.lumen.ui.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.numbergame.manager.*;

import javax.inject.Inject;
import java.security.acl.Group;
import java.util.HashSet;
import java.util.Set;

public class NumberGameSettingsActivity extends DaggerActivity {

    private ImageButton returnBtn;
    @Inject
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbergame_settings);
        this.getLumenApplication().getApplicationComponent().inject(this);
        final SharedPreferences.Editor editor = pref.edit();
        if(pref.getStringSet(NumberGamePreferences.OPERATIONS.name(),new HashSet<>()).isEmpty()){
            Set<String> additionSet=new HashSet<>();
            additionSet.add("ADDITION");
            editor.putStringSet(NumberGamePreferences.OPERATIONS.name(),additionSet);
            editor.commit();
        }
        CheckBox permutations=findViewById(R.id.permutation);
        permutations.setChecked(pref.getBoolean("permutationsCheckbox",false));
        permutations.setOnClickListener(v->{
            editor.putBoolean("permutationsCheckbox",permutations.isChecked() );
            if(permutations.isChecked()) editor.putString(NumberGamePreferences.GAMEMODE.name(),"PERMUTATIONS");
            else editor.putString(NumberGamePreferences.GAMEMODE.name(),"CHECKANSWER");
            editor.commit();
        });
        CheckBox onClickOption=findViewById(R.id.onClick);
        onClickOption.setChecked(pref.getBoolean("onClickOptionCheckbox",false));
        onClickOption.setOnClickListener(v->{
            editor.putBoolean("onClickOptionCheckbox",onClickOption.isChecked() );
            if(onClickOption.isChecked()) editor.putString(NumberGamePreferences.INPUTMODE.name(),"ONCLICK");
            else editor.putString(NumberGamePreferences.INPUTMODE.name(),"ONDRAG");
            editor.commit();
        });
        CheckBox addition = findViewById(R.id.additionCB);
        addition.setChecked(pref.getBoolean("additionCheckbox",true));
        RadioGroup additionGroup = findViewById(R.id.addition);
        enableDisableRadioButtons(addition, additionGroup);
        addition.setOnClickListener(v->{
            enableDisableRadioButtons(addition, additionGroup);
            saveCheckboxSettings(editor, addition, Operation.ADDITION, "additionCheckbox");
        });
        additionGroup.setOnCheckedChangeListener((v,i)->{
            int indexOfChecked=saveRadioButtonSettings(additionGroup,"additionGroup",editor);
            switch (indexOfChecked){
                case 0:
                    EquationConstants.setAdditionLimit(20);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(),20);
                    break;
                case 1:
                    EquationConstants.setAdditionLimit(100);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(),100);
                    break;
                case 2:
                    EquationConstants.setAdditionLimit(1000);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(),1000);
                    break;

            }
            editor.commit();
        });

        CheckBox subtraction = findViewById(R.id.subtractionCB);
        subtraction.setChecked(pref.getBoolean("subtractionCheckbox",false));
        RadioGroup subtractionGroup = findViewById(R.id.subtraction);
        enableDisableRadioButtons(subtraction, subtractionGroup);
        subtraction.setOnClickListener(v->{
            enableDisableRadioButtons(subtraction, subtractionGroup);
            saveCheckboxSettings(editor, subtraction, Operation.SUBTRACTION, "subtractionCheckbox");
        });
        subtractionGroup.setOnCheckedChangeListener((v, i)->{
            int indexOfChecked=saveRadioButtonSettings(subtractionGroup,"subtractionGroup",editor);
            switch (indexOfChecked){
                case 0:
                    EquationConstants.setSubtractionLimit(20);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),20);
                    break;
                case 1:
                    EquationConstants.setSubtractionLimit(100);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),100);
                    break;
                case 2:
                    EquationConstants.setSubtractionLimit(1000);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),1000);
                    break;

            }
            editor.commit();
        });

        CheckBox multiplication = findViewById(R.id.multiplicationCB);
        multiplication.setChecked(pref.getBoolean("multiplicationCheckbox",false));
        RadioGroup multiplicationGroup = findViewById(R.id.multiplication);
        enableDisableRadioButtons(multiplication, multiplicationGroup);
        multiplication.setOnClickListener(v->{
            enableDisableRadioButtons(multiplication, multiplicationGroup);
            saveCheckboxSettings(editor, multiplication, Operation.MULTIPLICATION, "multiplicationCheckbox");
        });
        multiplicationGroup.setOnCheckedChangeListener((v,i)->{
            int indexOfChecked=saveRadioButtonSettings(multiplicationGroup,"multiplicationGroup",editor);
            switch (indexOfChecked){
                case 0:
                    EquationConstants.setMultiplicationLimit(10);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),10);
                    break;
                case 1:
                    EquationConstants.setMultiplicationLimit(20);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),20);
                    break;
                case 2:
                    EquationConstants.setMultiplicationLimit(100);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),100);
                    break;

            }
            editor.commit();
        });

        CheckBox division = findViewById(R.id.divisionCB);
        division.setChecked(pref.getBoolean("divisionCheckbox",false));
        RadioGroup divisionGroup = findViewById(R.id.division);
        enableDisableRadioButtons(division, divisionGroup);
        division.setOnClickListener(v->{
            enableDisableRadioButtons(division, divisionGroup);
            saveCheckboxSettings(editor, division, Operation.DIVISION, "divisionCheckbox");
        });
        divisionGroup.setOnCheckedChangeListener((v,i)->{

            int indexOfChecked=saveRadioButtonSettings(divisionGroup,"divisionGroup",editor);
            switch (indexOfChecked){
                case 0:
                    EquationConstants.setDivisionLimit(10);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(),10);
                    break;
                case 1:
                    EquationConstants.setDivisionLimit(20);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(),20);
                    break;
                case 2:
                    EquationConstants.setDivisionLimit(100);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(),100);
                    break;

            }
            editor.commit();
        });

        setRadioGroup(additionGroup,"additionGroup");
        setRadioGroup(subtractionGroup,"subtractionGroup");
        setRadioGroup(multiplicationGroup,"multiplicationGroup");
        setRadioGroup(divisionGroup,"divisionGroup");
        returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(v->onBackPressed());


    }

    private void setRadioGroup(RadioGroup group,String groupname){
        ((RadioButton)group.getChildAt(0)).setChecked(pref.getBoolean(groupname+0,true));
        for (int i = 1; i < group.getChildCount(); i++) {

            ((RadioButton)group.getChildAt(i)).setChecked(pref.getBoolean(groupname+i,false));

        }
    }

    public int saveRadioButtonSettings(RadioGroup group,String groupname,SharedPreferences.Editor editor){
       int indexOfChecked=0;
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button=((RadioButton)group.getChildAt(i));
            boolean buttonIsChecked=button.isChecked();
            editor.putBoolean(groupname+i,buttonIsChecked);
            if(buttonIsChecked)indexOfChecked=i;
        }
        editor.commit();
        return indexOfChecked;

    }

    private void saveCheckboxSettings(SharedPreferences.Editor editor, CheckBox checkBox, Operation operation, String s) {
        Set<String> activeSet = pref.getStringSet(NumberGamePreferences.OPERATIONS.name(), new HashSet<String>());
        if (checkBox.isChecked()) {
            activeSet.add(operation.name());
        } else if (!checkBox.isChecked()) {
            activeSet.remove(operation.name());
        }
        editor.putBoolean(s,checkBox.isChecked() );
        //if(activeSet.isEmpty())activeSet.add("ADDITION");
        editor.putStringSet(NumberGamePreferences.OPERATIONS.name(), activeSet);
        editor.commit();
    }



    public void enableDisableRadioButtons(CheckBox checkBox, RadioGroup group){

        if(checkBox.isChecked()){
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setEnabled(true);
            }
        } else {
            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setEnabled(false);
            }
        }
    }

}