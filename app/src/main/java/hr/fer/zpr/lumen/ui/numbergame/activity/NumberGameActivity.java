package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.numbergame.manager.EquationConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGamePreferences;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import hr.fer.zpr.lumen.player.SoundPlayer;

public class NumberGameActivity extends DaggerActivity {

    @Inject
    SharedPreferences pref;
    private Set<String> additionSet=new HashSet<>();
    private final static int MAX_DIGITS_NUMBER_IN_ANSWER = 3;


    @Inject
    SoundPlayer soundPlayer;

    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbol;
    private NumberGamePresenter numberGamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_number_game);
        additionSet.add("ADDITION");
        setSettings();
        findViewById(R.id.guilanguageButton);
        gameSetup();
        setDragAndDropListeners();
        checkButton();
        deleteButton();
    }

    private void deleteButton() {
        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = result.getText().toString();
                int dataLength = data.length();
                if(dataLength > 0) {
                    result.setText(data.substring(0, dataLength-1));
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSettings();
        gameSetup();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setSettings();
        gameSetup();
    }

    private void setSettings() {
        NumberGameConstants.operations=pref.getStringSet(NumberGamePreferences.OPERATIONS.name(),additionSet);
        NumberGameConstants.numberGameMode=pref.getString(NumberGamePreferences.GAMEMODE.name(),"CHECKANSWER");
        EquationConstants.setAdditionLimit(pref.getInt(NumberGamePreferences.ADDITIONLIMIT.name(),20));
        EquationConstants.setSubtractionLimit(pref.getInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),100));
        EquationConstants.setMultiplicationLimit(pref.getInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),10));
        EquationConstants.setDivisionLimit(pref.getInt(NumberGamePreferences.DIVISIONLIMIT.name(),10));

    }

    private void checkButton() {
        ImageButton checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((numberGamePresenter != null) && (result != null) && (!result.getText().toString().isEmpty()) && numberGamePresenter.checkSolution(Integer.parseInt(result.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "Rjesenje je tocno", Toast.LENGTH_LONG).show();
                    soundPlayer.play("database/sound/messages/croatian/correct/bravo.mp3");
                    numberGamePresenter.newEquation();
                    result.setText("");
                } else if ((numberGamePresenter != null) && (result != null) && !result.getText().toString().isEmpty() && !numberGamePresenter.checkSolution(Integer.parseInt(result.getText().toString()))) {
                    Toast.makeText(getApplicationContext(), "Rjesenje nije tocno, pokusaj ponovno", Toast.LENGTH_LONG).show();
                    result.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Nije unesen nijedan broj", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void gameSetup() {
        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        symbol = findViewById(R.id.symbol);
        result = findViewById(R.id.result);
        numberGamePresenter = new NumberGamePresenter(firstNumber, secondNumber, symbol);
        numberGamePresenter.newEquation();

    }

    private void setDragAndDropListeners() {
        linearLayout = findViewById(R.id.digits);
        result = findViewById(R.id.result);

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            textView.setOnTouchListener(new TouchListener(textView));
            textView.setOnClickListener(new ClickListener());
        }

        result.setOnDragListener(new DragListener());
        result.setOnClickListener(new ClickListener());
    }

    public class DragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View receivingLayoutView, DragEvent event) {
            int action = event.getAction();
            switch(action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:

                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);

                    // Gets the text data from the item.
                    String dragData = item.getText().toString();

                    setResult(dragData);

                    // Returns true. DragEvent.getResult() will return true.
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }

    public void setResult(String newDigit) {
        String oldDigits = result.getText().toString();

        if(oldDigits.startsWith("0") && newDigit != null){
            oldDigits = "";
        }
        oldDigits += newDigit;

        if(oldDigits.length() > MAX_DIGITS_NUMBER_IN_ANSWER) return;

        result.setText(oldDigits);
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView textView = (TextView)v;
            if(result.getId() == textView.getId()){
                result.setText("");
            } else {
                setResult(textView.getText().toString());
            }
        }

    }

    class TouchListener implements  View.OnTouchListener {

        private TextView textView;

        TouchListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData.Item item = new ClipData.Item(textView.getText());

            ClipData dragData = new ClipData(
                    textView.getText().toString(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item
            );

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return v.startDragAndDrop(dragData,
                            new View.DragShadowBuilder(textView),
                            v,
                            0);
                } else {
                    return v.startDrag(dragData,
                            new View.DragShadowBuilder(textView),
                            v,
                            0
                    );
                }

            }
            return false;
        }
    }

}
