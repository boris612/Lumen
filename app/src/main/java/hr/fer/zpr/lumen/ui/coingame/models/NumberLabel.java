package hr.fer.zpr.lumen.ui.coingame.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class NumberLabel extends GameDrawable {

    private int value;

    private int color;

    public NumberLabel(Rect rect, int value, int color) {
        super(null, rect);
        this.value = value;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(color);
        Rect r = new Rect();
        paint.getTextBounds(Integer.toString(value), 0, Integer.toString(value).length(), r);
        canvas.drawRect(rectangle, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(Integer.toString(value), rectangle.centerX() - paint.measureText(Integer.toString(value)) / 2, rectangle.centerY() + r.height() / 2, paint);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
