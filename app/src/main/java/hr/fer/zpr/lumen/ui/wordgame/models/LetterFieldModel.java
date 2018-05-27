package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class LetterFieldModel extends GameDrawable {

    private LetterModel letter=null;

    private int color=Color.RED;

    public LetterFieldModel(Rect bounds) {
        super(null, bounds);
    }

    public void attachLetter(LetterModel letter) {
        this.letter = letter;
    }

    public void detachLetter() {
        letter = null;
    }

    public boolean containsLetter() {
        return letter == null ? false : true;
    }

    public LetterModel getLetterInside(){
        return letter;
    }



    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawRect(rectangle, paint);
    }

    public void setColor(int color){
        this.color=color;
    }

}
