package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
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

public class NumberGameActivity extends DaggerActivity {

    @Inject
    SharedPreferences pref;
    private Set<String> additionSet=new HashSet<>();
    private final static int MAX_DIGITS_NUMBER_IN_ANSWER = 3;
    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbol;
    private NumberGamePresenter numberGamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);
        additionSet.add("ADDITION");
        setSettings();
        gameSetup();
        setDragAndDropListeners();
        checkButton();
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
        NumberGameConstants.numberGameMode=pref.getString(NumberGamePreferences.GAMEMODE.name(),"ADDITION");
        EquationConstants.setAdditionLimit(pref.getInt(NumberGamePreferences.ADDITIONLIMIT.name(),20));
        EquationConstants.setSubtractionLimit(pref.getInt(NumberGamePreferences.SUBTRACTIONLIMIT.name(),100));
        EquationConstants.setMultiplicationLimit(pref.getInt(NumberGamePreferences.MULTIPLICATIONLIMIT.name(),10));
        EquationConstants.setDivisionLimit(pref.getInt(NumberGamePreferences.DIVISIONLIMIT.name(),10));

    }

    private void checkButton() {
        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if((numberGamePresenter != null) && (result != null) && (!result.getText().toString().isEmpty()) && numberGamePresenter.checkSolution(Integer.parseInt(result.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Rjesenje je tocno",Toast.LENGTH_LONG).show();
                    numberGamePresenter.newEquation();
                    result.setText("");
                } else if((numberGamePresenter != null) && (result != null) && !result.getText().toString().isEmpty() && !numberGamePresenter.checkSolution(Integer.parseInt(result.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Rjesenje nije tocno, pokusaj ponovno",Toast.LENGTH_LONG).show();
                    result.setText("");
                } else {
                    Toast.makeText(getApplicationContext(),"Nije unesen nijedan broj",Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void gameSetup() {
        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        symbol = findViewById(R.id.symbol);
        result = findViewById(R.id.result);
        numberGamePresenter=new NumberGamePresenter(firstNumber,secondNumber,symbol);
        numberGamePresenter.newEquation();

    }

    private void setDragAndDropListeners() {
        linearLayout = findViewById(R.id.digits);
        result = findViewById(R.id.result);

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            textView.setOnLongClickListener(new LongClickListener(textView));
            textView.setOnClickListener(new ClickListener());
        }

        findViewById(R.id.digits).getRootView().setOnDragListener(new DragListener());
        findViewById(R.id.equation).getRootView().setOnDragListener(new DragListener());

        result.getRootView().setOnDragListener(new DragListener());
        result.setOnClickListener(new ClickListener());
    }

    class DragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View layoutView, DragEvent dragEvent) {
            int action = dragEvent.getAction();

            if (action == DragEvent.ACTION_DROP) {
                View view = (View) dragEvent.getLocalState();
                TextView dropped = (TextView) view;

                float userX = dragEvent.getX();
                float userY = dragEvent.getY();

                float textViewX = result.getLeft();
                float textViewY = result.getTop();
                float textViewWidth = result.getWidth();
                float textViewHeight = result.getHeight();

                if (userX >= textViewX && userX <= textViewX + textViewWidth &&
                        userY >= textViewY && userY <= textViewY + textViewHeight) {
                    setResult(dropped.getText().toString());
                    return true;
                }
            }

            return false;
        }
    }

    public void setResult(String newDigit) {
        String oldDigits = result.getText().toString();
        int digitsLength = oldDigits.length();

        if(digitsLength > MAX_DIGITS_NUMBER_IN_ANSWER) return;
        if(oldDigits.startsWith("0") && newDigit != null){
            oldDigits = "";
        }

        oldDigits += newDigit;
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

    class LongClickListener implements  View.OnLongClickListener {

        private TextView textView;

        LongClickListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData.Item item = new ClipData.Item(textView.getText());

            ClipData dragData = new ClipData(
                    textView.getText().toString(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item
            );

            return v.startDrag(dragData,
                    new View.DragShadowBuilder(textView),
                    v,
                    0
            );
        }

    }

}
