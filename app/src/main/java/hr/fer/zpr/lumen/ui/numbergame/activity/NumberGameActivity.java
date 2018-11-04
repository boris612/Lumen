package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.numbergame.generator.EquationGenerator;
import hr.fer.zpr.lumen.numbergame.manager.NumbergameManager;
import hr.fer.zpr.lumen.numbergame.manager.Operation;

public class NumberGameActivity extends DaggerActivity implements View.OnDragListener{

    private final static int MAX_DIGITS_NUMBER_IN_ANSWER = 3;
    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbol;
    private float downX;
    private float downY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_game);
        setDragAndDropListeners();

        generateNumbers();

        checkButton();
    }

    private void checkButton() {
        Button checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });
    }

    private void generateNumbers() {
        //todo
        firstNumber = findViewById(R.id.firstNumber);
        firstNumber.setText("22");
        secondNumber = findViewById(R.id.secondNumber);
        secondNumber.setText("312");
        symbol = findViewById(R.id.symbol);
        symbol.setText("+");
    }

    private void setDragAndDropListeners() {
        linearLayout = findViewById(R.id.digits);

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
            textView.setOnLongClickListener(new LongClickListener(textView));
            textView.setOnClickListener(new ClickListener());
        }

        findViewById(R.id.relative_layout).setOnDragListener(this);
        result = findViewById(R.id.result);
        result.setOnDragListener(this);
        result.setOnClickListener(new ClickListener());
    }

    @Override
    public boolean onDrag(View layoutView, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        Log.d("tu sam","tu sma");
        Log.d("action",String.valueOf(action));
        Log.d("event" ,String.valueOf(DragEvent.ACTION_DROP));
        if(action == DragEvent.ACTION_DROP) {
            Log.d("drop", "drop");
            View view = (View) dragEvent.getLocalState();
//                    float userX = dragEvent.getX();
//                    float userY = dragEvent.getY();
////                    float userX = downX;
////                    float userY = downY;
//
//                    float textViewX = result.getLeft();
//                    float textViewY = result.getTop();
//                    float textViewWidth = result.getWidth();
//                    float textViewHeight = result.getHeight();
//
//
//                    Log.d("x", String.valueOf(userX));
//                    Log.d("x", String.valueOf(textViewX));
//                    Log.d("x", String.valueOf(textViewX+textViewWidth));
//
//
//                    Log.d("y", String.valueOf(userY));
//                    Log.d("x", String.valueOf(textViewY));
//                    Log.d("x", String.valueOf(textViewHeight+textViewY));

            TextView dropTarget = (TextView)layoutView;
            TextView dropped = (TextView)view;

//                    if(userX >= textViewX && userX <= textViewX + textViewWidth &&
//                            userY >= textViewY && userY <= textViewY + textViewHeight) {
            dropTarget.setText(dropped.getText());
            return true;
//                    }
//                    return  false;
        }
        return false;
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView textView = (TextView)v;
            if(result.getId() == textView.getId()){
                result.setText("");
            } else {
                String oldDigits = result.getText().toString();
                String newDigit = textView.getText().toString();
                int digitsLength = oldDigits.length();
                if(digitsLength > MAX_DIGITS_NUMBER_IN_ANSWER) return;
                if(digitsLength == 1 && oldDigits.equals("0") && !newDigit.equals("0")){
                    oldDigits = "";
                }
                oldDigits += newDigit;
                result.setText(oldDigits);
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

            v.startDrag(dragData,
                    new View.DragShadowBuilder(textView),
                    textView,
                    0
            );

            return true;
        }

    }

}
