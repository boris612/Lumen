package lumen.zpr.fer.hr.lumen.guicomponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Alen on 16.11.2017..
 */

public class Label {
    private String text;
    private Point position;
    private int color;
    private int textSize;
    private Paint paint = new Paint();

    public Label(String text, Point position, int color, int textSize) {
        this.text = text;
        this.position = position;
        this.color = color;
        this.textSize = textSize;

        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(textSize);
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        paint.setTextSize(textSize);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, position.x, position.y, paint);
    }
}
