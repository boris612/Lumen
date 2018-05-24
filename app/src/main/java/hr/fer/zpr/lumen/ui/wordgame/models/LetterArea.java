package hr.fer.zpr.lumen.ui.wordgame.models;

import android.view.MotionEvent;

import java.util.List;

public class LetterArea {

    public List<LetterModel> letters;
    public List<LetterFieldModel> fields;


    public void handleTouch(MotionEvent e){
        switch(e.getActionMasked()){
            case MotionEvent.ACTION_DOWN:{

            }
        }
    }
}
