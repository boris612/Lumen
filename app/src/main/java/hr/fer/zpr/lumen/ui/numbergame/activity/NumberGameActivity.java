package hr.fer.zpr.lumen.ui.numbergame.activity;


import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;

public class NumberGameActivity extends DaggerActivity {

    private LinearLayout linearLayout;
    private TextView result;
    private TextView firstNumber;
    private TextView secondNumber;
    private TextView symbole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        firstNumber = findViewById(R.id.firstNumber);
        secondNumber = findViewById(R.id.secondNumber);
        symbole = findViewById(R.id.symbol);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_number_game);
        setDragAndDropListeners();

        generateNumbers();
    }

    private void generateNumbers() {

    }

    private void setDragAndDropListeners() {
        linearLayout = findViewById(R.id.digits);

        for (int i = 0, nChildren = linearLayout.getChildCount(); i < nChildren; i++) {
            TextView textView = (TextView) linearLayout.getChildAt(i);
           // textView.setOnLongClickListener(new LongClickListener(textView));
            textView.setOnClickListener(new ClickListener());
        }

        result = findViewById(R.id.result);
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
                result.setText(textView.getText());
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
