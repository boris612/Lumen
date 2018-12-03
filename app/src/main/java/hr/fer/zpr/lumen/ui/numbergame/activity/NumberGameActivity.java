package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.numbergame.manager.EquationConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGameConstants;
import hr.fer.zpr.lumen.numbergame.manager.NumberGamePreferences;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import hr.fer.zpr.lumen.player.SoundPlayer;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NumberGameActivity extends DaggerActivity {

    private final static int MAX_DIGITS_NUMBER_IN_ANSWER = 4;

    private final static int BORDER_COLOR = Color.rgb(141,8,110);
    private final static String FILE_PATH_CORRECT_MESSAGE = "database/sound/messages/croatian/correct/bravo.mp3";
    private final static String FILE_PATH_INCORRECT_MESSAGE = "database/sound/messages/croatian/incorrect/pokusaj_opet.mp3";
    private final  DragListener dragListener = new DragListener();

    @Inject
    SharedPreferences pref;

    @Inject
    SoundPlayer soundPlayer;

    private Set<String> additionSet=new HashSet<>();

    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbol;
    private ImageButton deleteButton;
//    private ImageButton checkButton;
    private NumberGamePresenter numberGamePresenter;
    private TextView target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);

        setContentView(R.layout.activity_number_game);
        additionSet.add("ADDITION");
        setSettings();
        gameSetup();
        setListeners();
//        checkButton();
        deleteButton();
    }

    private void deleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = target.getText().toString();
                int dataLength = data.length();
                if(dataLength > 0) {
                    target.setText(data.substring(0, dataLength-1));
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
        NumberGameConstants.setInputMode(pref.getString(NumberGamePreferences.INPUTMODE.name(),"ONDRAG"));
        NumberGameConstants.setNumberGameMode(pref.getString(NumberGamePreferences.GAMEMODE.name(),"CHECKANSWER"));
        EquationConstants.setAdditionLimit(pref.getInt(NumberGamePreferences.ADDITIONLIMIT.name(),20));
        EquationConstants.setSubtractionLimit(pref.getInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),100));
        EquationConstants.setMultiplicationLimit(pref.getInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),10));
        EquationConstants.setDivisionLimit(pref.getInt(NumberGamePreferences.DIVISIONLIMIT.name(),10));
    }

//    private void checkButton() {
//        checkButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if ((numberGamePresenter != null) && (target != null) && (!target.getText().toString().isEmpty()) && numberGamePresenter.checkSolution()) {
//                  //  Toast.makeText(getApplicationContext(), "Rjesenje je tocno", Toast.LENGTH_LONG).show();
//                    soundPlayer.play(FILE_PATH_CORRECT_MESSAGE);
//                    target.setBackgroundColor(Color.GREEN);
//                    setClickable(false);
//                    Completable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                            .subscribe(() -> {
//                                setClickable(true);
//                                target.setOnDragListener(null);
//                                target.setBackgroundColor(Color.TRANSPARENT);
//                                target = numberGamePresenter.newEquation();
//                                target.setOnDragListener(dragListener);
//                                target.setBackgroundColor(BACKGROUND_COLOR);
//                            });
//                } else if ((numberGamePresenter != null) && (target != null) && !target.getText().toString().isEmpty() && !numberGamePresenter.checkSolution()) {
//                 //   Toast.makeText(getApplicationContext(), "Rjesenje nije tocno, pokusaj ponovno", Toast.LENGTH_LONG).show();
//                    soundPlayer.play(FILE_PATH_INCORRECT_MESSAGE);
//                    target.setBackgroundColor(Color.RED);
//                    setClickable(false);
//                    Completable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                            .subscribe(() -> {
//                                target.setText("");
//                                target.setBackgroundColor(BACKGROUND_COLOR);
//                                setClickable(true);
//                            });
//                } else {
//                    Toast.makeText(getApplicationContext(), "Nije unesen nijedan broj", Toast.LENGTH_LONG).show();
//                }
//            }
//
//        });
//    }

    private void setClickable(boolean clickable){
        target.setClickable(clickable);
//        checkButton.setClickable(clickable);
        deleteButton.setClickable(clickable);
        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            textView.setClickable(clickable);
            textView.setEnabled(clickable);
        }
    }

    private void gameSetup() {
        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        symbol = findViewById(R.id.symbol);
        result = findViewById(R.id.result);
        deleteButton = findViewById(R.id.deleteButton);
        linearLayout = findViewById(R.id.digits);
//        checkButton = findViewById(R.id.checkButton);

        firstNumber.setBackgroundColor(Color.TRANSPARENT);
        secondNumber.setBackgroundColor(Color.TRANSPARENT);
        result.setBackgroundColor(Color.TRANSPARENT);

        numberGamePresenter = new NumberGamePresenter(firstNumber, secondNumber, result, symbol);

        target = numberGamePresenter.newEquation();
        target.setOnDragListener(dragListener);
        setTextViewBorder(target);
    }

    private void setTextViewBorder(TextView textView){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(8, BORDER_COLOR);
        textView.setBackground(drawable);
    }

    private void setListeners() {

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);

            if(pref.getBoolean("onClickOptionCheckbox",false)){
                textView.setOnClickListener(new DigitClickListener());
            } else {
                textView.setOnTouchListener(new TouchListener(textView));
            }
        }
    }

    public class DigitClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView textView = (TextView)v;
            setResult(textView.getText().toString());
            checkAnswer();
        }

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
                    checkAnswer();
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
        String oldDigits = target.getText().toString();

        if(oldDigits.startsWith("0") && newDigit != null){
            oldDigits = "";
        }
        oldDigits += newDigit;

        if(oldDigits.length() > MAX_DIGITS_NUMBER_IN_ANSWER) return;

        target.setText(oldDigits);
    }

    private void checkAnswer(){
        if ((numberGamePresenter != null) && (target != null) && (!target.getText().toString().isEmpty()) && numberGamePresenter.checkSolution()) {
                    soundPlayer.play(FILE_PATH_CORRECT_MESSAGE);
                    target.setBackgroundColor(Color.GREEN);
                    setClickable(false);
                    Completable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                setClickable(true);
                                target.setOnDragListener(null);
                                target.setBackgroundColor(Color.TRANSPARENT);
                                target = numberGamePresenter.newEquation();
                                target.setOnDragListener(dragListener);
                                setTextViewBorder(target);
                                soundPlayer.stopPlaying();
                            });
                }
    }

    class onClickDeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView textView = (TextView)v;
            textView.setText("");
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
