package hr.fer.zpr.lumen.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.localization.LocalizationConstants;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.numbergame.manager.EquationConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGamePreferences;
import hr.fer.zpr.lumen.numbergame.manager.Operation;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

public class NumberGameSettingsActivity extends DaggerActivity {

    private ImageButton returnBtn;
    private int numberOfCheckedBoxes;
    private TextView numberGameSettings;
    private CheckBox permutations;
    private CheckBox onClickOption;
    private CheckBox addition;
    private CheckBox subtraction;
    private CheckBox multiplication;
    private CheckBox division;
    private RadioGroup additionGroup;
    private RadioGroup subtractionGroup;
    private RadioGroup multiplicationGroup;
    private RadioGroup divisionGroup;

    @Inject
    SharedPreferences pref;

    @Inject
    LocalizationProvider localizationProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbergame_settings);
        this.getLumenApplication().getApplicationComponent().inject(this);

        getComponents();
        setLanguageValues(pref.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE));

        final SharedPreferences.Editor editor = pref.edit();
        numberOfCheckedBoxes=pref.getInt("numberOfCheckedBoxes",1);
        if (pref.getStringSet(NumberGamePreferences.OPERATIONS.name(), new HashSet<>()).isEmpty()) {
            Set<String> additionSet = new HashSet<>();
            additionSet.add("ADDITION");
            editor.putStringSet(NumberGamePreferences.OPERATIONS.name(), additionSet);
            editor.commit();
        }

        permutations.setChecked(pref.getBoolean("permutationsCheckbox", false));
        permutations.setOnClickListener(v -> {
            editor.putBoolean("permutationsCheckbox", permutations.isChecked());
            if (permutations.isChecked()) editor.putString(NumberGamePreferences.GAMEMODE.name(), "PERMUTATIONS");
            else editor.putString(NumberGamePreferences.GAMEMODE.name(), "CHECKANSWER");
            editor.commit();
        });

        onClickOption.setChecked(pref.getBoolean("onClickOptionCheckbox", false));
        onClickOption.setOnClickListener(v -> {
            editor.putBoolean("onClickOptionCheckbox", onClickOption.isChecked());
            if (onClickOption.isChecked()) editor.putString(NumberGamePreferences.INPUTMODE.name(), "ONCLICK");
            else editor.putString(NumberGamePreferences.INPUTMODE.name(), "ONDRAG");
            editor.commit();
        });

        addition.setChecked(pref.getBoolean("additionCheckbox", true));
        enableDisableRadioButtons(addition, additionGroup);
        addition.setOnClickListener(v -> {
            int numberOfCBoxes= updateCounter(addition.isChecked(),editor);
            if(numberOfCBoxes==0){
                addition.setChecked(true);
                numberOfCheckedBoxes=1;
                editor.putInt("numberOfCheckedBoxes",numberOfCheckedBoxes);
                editor.commit();
            }
            else{
            enableDisableRadioButtons(addition, additionGroup);
            saveCheckboxSettings(editor, addition, Operation.ADDITION, "additionCheckbox");
            }
        });
        additionGroup.setOnCheckedChangeListener((v, i) -> {
            int indexOfChecked = saveRadioButtonSettings(additionGroup, "additionGroup", editor);
            switch (indexOfChecked) {
                case 0:
                    EquationConstants.setAdditionLimit(20);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(), 20);
                    break;
                case 1:
                    EquationConstants.setAdditionLimit(100);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(), 100);
                    break;
                case 2:
                    EquationConstants.setAdditionLimit(1000);
                    editor.putInt(NumberGamePreferences.ADDITIONLIMIT.name(), 1000);
                    break;

            }
            editor.commit();
        });

        subtraction.setChecked(pref.getBoolean("subtractionCheckbox", false));
        enableDisableRadioButtons(subtraction, subtractionGroup);
        subtraction.setOnClickListener(v -> {
            int numberOfCBoxes= updateCounter(subtraction.isChecked(),editor);
            if(numberOfCBoxes==0){
                subtraction.setChecked(true);
                numberOfCheckedBoxes=1;
                editor.putInt("numberOfCheckedBoxes",numberOfCheckedBoxes);
                editor.commit();
            }
            else {
                enableDisableRadioButtons(subtraction, subtractionGroup);
                saveCheckboxSettings(editor, subtraction, Operation.SUBTRACTION, "subtractionCheckbox");
            }
        });
        subtractionGroup.setOnCheckedChangeListener((v, i) -> {
            int indexOfChecked = saveRadioButtonSettings(subtractionGroup, "subtractionGroup", editor);
            switch (indexOfChecked) {
                case 0:
                    EquationConstants.setSubtractionLimit(20);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(), 20);
                    break;
                case 1:
                    EquationConstants.setSubtractionLimit(100);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(), 100);
                    break;
                case 2:
                    EquationConstants.setSubtractionLimit(1000);
                    editor.putInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(), 1000);
                    break;

            }
            editor.commit();
        });

        multiplication.setChecked(pref.getBoolean("multiplicationCheckbox", false));
        enableDisableRadioButtons(multiplication, multiplicationGroup);
        multiplication.setOnClickListener(v -> {
            int numberOfCBoxes= updateCounter(multiplication.isChecked(),editor);
            if(numberOfCBoxes==0){
                multiplication.setChecked(true);
                numberOfCheckedBoxes=1;
                editor.putInt("numberOfCheckedBoxes",numberOfCheckedBoxes);
                editor.commit();
            }
            else {
                enableDisableRadioButtons(multiplication, multiplicationGroup);
                saveCheckboxSettings(editor, multiplication, Operation.MULTIPLICATION, "multiplicationCheckbox");
            }
        });
        multiplicationGroup.setOnCheckedChangeListener((v, i) -> {
            int indexOfChecked = saveRadioButtonSettings(multiplicationGroup, "multiplicationGroup", editor);
            switch (indexOfChecked) {
                case 0:
                    EquationConstants.setMultiplicationLimit(10);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(), 10);
                    break;
                case 1:
                    EquationConstants.setMultiplicationLimit(20);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(), 20);
                    break;
                case 2:
                    EquationConstants.setMultiplicationLimit(100);
                    editor.putInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(), 100);
                    break;

            }
            editor.commit();
        });

        division.setChecked(pref.getBoolean("divisionCheckbox", false));
        enableDisableRadioButtons(division, divisionGroup);
        division.setOnClickListener(v -> {
            int numberOfCBoxes= updateCounter(division.isChecked(),editor);
            if(numberOfCBoxes==0){
                division.setChecked(true);
                numberOfCheckedBoxes=1;
                editor.putInt("numberOfCheckedBoxes",numberOfCheckedBoxes);
                editor.commit();
            }
            else {
                enableDisableRadioButtons(division, divisionGroup);
                saveCheckboxSettings(editor, division, Operation.DIVISION, "divisionCheckbox");
            }
        });
        divisionGroup.setOnCheckedChangeListener((v, i) -> {

            int indexOfChecked = saveRadioButtonSettings(divisionGroup, "divisionGroup", editor);
            switch (indexOfChecked) {
                case 0:
                    EquationConstants.setDivisionLimit(10);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(), 10);
                    break;
                case 1:
                    EquationConstants.setDivisionLimit(20);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(), 20);
                    break;
                case 2:
                    EquationConstants.setDivisionLimit(100);
                    editor.putInt(NumberGamePreferences.DIVISIONLIMIT.name(), 100);
                    break;

            }
            editor.commit();
        });

        setRadioGroup(additionGroup, "additionGroup");
        setRadioGroup(subtractionGroup, "subtractionGroup");
        setRadioGroup(multiplicationGroup, "multiplicationGroup");
        setRadioGroup(divisionGroup, "divisionGroup");
        returnBtn = findViewById(R.id.returnButton);
        returnBtn.setOnClickListener(v -> onBackPressed());
    }

    private void getComponents(){
        numberGameSettings = findViewById(R.id.number_game_settings);
        permutations = findViewById(R.id.permutation);
        onClickOption = findViewById(R.id.onClick);
        addition = findViewById(R.id.additionCB);
        subtraction = findViewById(R.id.subtractionCB);
        multiplication = findViewById(R.id.multiplicationCB);
        division = findViewById(R.id.divisionCB);
        additionGroup = findViewById(R.id.addition);
        subtractionGroup = findViewById(R.id.subtraction);
        multiplicationGroup = findViewById(R.id.multiplication);
        divisionGroup = findViewById(R.id.division);
    }

    private void setLanguageValues(String newLanguage){
        numberGameSettings.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.CALCULATING_GAME_SETTINGS_PROPERTY));
        permutations.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.PERMUTATION));
        onClickOption.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.ON_CLICK_OPTION));
        addition.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.ADDITION));
        subtraction.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.SUBTRACTION));
        multiplication.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.MULTIPLICATION));
        division.setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.DIVISION));
        setLanguageValuesOnRadioButtons(additionGroup, newLanguage);
        setLanguageValuesOnRadioButtons(subtractionGroup, newLanguage);
        setLanguageValuesOnRadioButtons(multiplicationGroup, newLanguage);
        setLanguageValuesOnRadioButtons(divisionGroup, newLanguage);
    }

    private void setLanguageValuesOnRadioButtons(RadioGroup group, String newLanguage){
        ((RadioButton) group.getChildAt(0)).setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.LVL_1));
        ((RadioButton) group.getChildAt(1)).setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.LVL_2));
        ((RadioButton) group.getChildAt(2)).setText(localizationProvider.getValueForLanguage(newLanguage, LocalizationConstants.LVL_3));
    }

    private int updateCounter(boolean checked,SharedPreferences.Editor editor){
        numberOfCheckedBoxes=pref.getInt("numberOfCheckedBoxes",1);
        if(checked){
            numberOfCheckedBoxes++;
        }
        else {
            numberOfCheckedBoxes--;
        }
        editor.putInt("numberOfCheckedBoxes",numberOfCheckedBoxes);
        editor.commit();
        return numberOfCheckedBoxes;
    }
    private void setRadioGroup(RadioGroup group, String groupname) {
        ((RadioButton) group.getChildAt(0)).setChecked(pref.getBoolean(groupname + 0, true));
        for (int i = 1; i < group.getChildCount(); i++) {

            ((RadioButton) group.getChildAt(i)).setChecked(pref.getBoolean(groupname + i, false));

        }
    }

    public int saveRadioButtonSettings(RadioGroup group, String groupname, SharedPreferences.Editor editor) {
        int indexOfChecked = 0;
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton button = ((RadioButton) group.getChildAt(i));
            boolean buttonIsChecked = button.isChecked();
            editor.putBoolean(groupname + i, buttonIsChecked);
            if (buttonIsChecked) indexOfChecked = i;
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
        editor.putBoolean(s, checkBox.isChecked());
        //if(activeSet.isEmpty())activeSet.add("ADDITION");
        editor.putStringSet(NumberGamePreferences.OPERATIONS.name(), activeSet);
        editor.commit();
    }


    public void enableDisableRadioButtons(CheckBox checkBox, RadioGroup group) {

        if (checkBox.isChecked()) {
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