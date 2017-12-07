package lumen.zpr.fer.hr.lumen.guicomponents;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;


/**
 * Created by Alen on 16.11.2017..
 */

public class Label {
    private String text;
    private Point position;
    private Paint paint = new Paint();

    public Label(String text, Point positionOfCenter, int color, int textSize) {
        this.text = text;
        this.position = positionOfCenter;

        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, position.x, position.y, paint);
    }
}
