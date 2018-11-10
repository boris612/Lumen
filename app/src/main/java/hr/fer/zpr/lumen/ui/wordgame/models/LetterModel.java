package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class LetterModel extends GameDrawable {
    private String value;
    private boolean hintActive;

    private int color = Color.BLACK;

    public LetterModel(String value, Bitmap image, Rect bounds) {
        super(image, bounds);
        this.value = value;
    }

    public void move(int x, int y) {
        rectangle.left = x - width / 2;
        rectangle.top = y - height / 2;
        rectangle.right = rectangle.left + width;
        rectangle.bottom = rectangle.top + height;
    }

    public String getValue() {
        return value;
    }

    public void switchHintColor() {
        if (color == Color.BLACK) color = Color.GREEN;
        else color = Color.BLACK;
    }

    public void setHintActive(boolean active) {
        this.hintActive = active;
        if (active == false) {
            color = Color.BLACK;
        }
    }

    public void draw(Canvas canvas) {
        if (hintActive) {
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(image, null, rectangle, paint);
            return;
        }
        super.draw(canvas);
    }

}
