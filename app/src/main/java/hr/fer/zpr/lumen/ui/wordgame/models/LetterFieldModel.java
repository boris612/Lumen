package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class LetterFieldModel extends GameDrawable {

    private LetterModel letter;

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

    @Override
    public void handleTouch(MotionEvent event) {

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(rectangle, paint);
    }
}
