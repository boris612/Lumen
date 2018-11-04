package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.numbergame.manager.Operation;

public class NumberGameActivity extends DaggerActivity {

    private final static int MAX_DIGITS_NUMBER_IN_ANSWER = 3;
    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbole;
    private NumberGamePresenter numberGamePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_game);
        gameSetup();

        setDragAndDropListeners();
        checkButton();



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
                }
                else if((numberGamePresenter != null) && (result != null) && !result.getText().toString().isEmpty() && !numberGamePresenter.checkSolution(Integer.parseInt(result.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Rjesenje nije tocno, pokusaj ponovno",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Nije unesen nijedan broj",Toast.LENGTH_LONG).show();
            }

        });
    }

    private void gameSetup() {
        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        symbole = findViewById(R.id.symbol);
        result = findViewById(R.id.result);
        numberGamePresenter=new NumberGamePresenter(firstNumber,secondNumber,symbole);
        numberGamePresenter.newEquation();
    }

    private void setDragAndDropListeners() {
        linearLayout = findViewById(R.id.digits);

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
           // textView.setOnLongClickListener(new LongClickListener(textView));
            textView.setOnClickListener(new ClickListener());
        }

      //  result = findViewById(R.id.result);
     //   result.setOnDragListener(new DragListener());
        result.setOnClickListener(new ClickListener());
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView textView = (TextView)v;
            if(result.getId() == textView.getId()){
                result.setText("");
            } else {
                String answer = result.getText().toString();
                answer += textView.getText().toString();
                if(answer.toCharArray().length > MAX_DIGITS_NUMBER_IN_ANSWER) return;
                result.setText(answer);
            }
        }

    }

//    class DragListener implements View.OnDragListener {
//
    
//        @Override
//        public boolean onDrag(View layoutView, DragEvent dragEvent) {
//            int action = dragEvent.getAction();
//
//            switch (action) {
//                case DragEvent.ACTION_DRAG_STARTED:
//
//                case DragEvent.ACTION_DRAG_LOCATION:
//
//                case DragEvent.ACTION_DRAG_EXITED:
//
//                case DragEvent.ACTION_DROP:
//                    Log.d("sam dropo", "ola");
//                    View view = (View) dragEvent.getLocalState();
//
//                    TextView dropTarget = (TextView)layoutView;
//                    TextView dropped = (TextView)view;
//
//                    if(result.getId() == dropTarget.getId()) {
//                        dropTarget.setText(dropped.getText());
//                        return true;
//                    }
//                    return false;
//                case DragEvent.ACTION_DRAG_ENDED:
//
//                default:
//                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
//                    break;
//            }
//            return false;
//        }
//    }
//
//    class LongClickListener implements  View.OnLongClickListener {
//
//        private TextView textView;
//
//        LongClickListener(TextView textView) {
//            this.textView = textView;
//        }
//
//        @Override
//        public boolean onLongClick(View v) {
//            ClipData.Item item = new ClipData.Item(textView.getText());
//
//            ClipData dragData = new ClipData(
//                    textView.getText().toString(),
//                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
//                    item
//            );
//
//            v.startDrag(dragData,
//                    new View.DragShadowBuilder(textView),
//                    textView,
//                    0
//            );
//
//            return true;
//        }
//
//    }

}
