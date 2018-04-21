package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class LetterModel extends GameDrawable {
    private String value;

    public LetterModel(String value, Bitmap image, Rect bounds) {
        super(image, bounds);
        this.value = value;
    }

    public void move(MotionEvent event) {
        rectangle.left = (int) (event.getX()) - width / 2;
        rectangle.top = (int) (event.getY()) - height / 2;
        rectangle.right = rectangle.left + width;
        rectangle.bottom = rectangle.top + height;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void handleTouch(MotionEvent event) {

    }
}
