package lumen.zpr.fer.hr.lumen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Karlo on 7.11.2017..
 */

public class DropArea {
    private Rect rectangle;
    private int color;

    public DropArea(Rect rectangle, int color) {
        this.rectangle=rectangle;
        this.color=color;
    }

    public Point getCenterPoint() {
        return new Point(rectangle.centerX(),rectangle.centerY());
    }

    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    public boolean collision (LetterImage letter){
       /* if(rectangle.contains(letter.getRectangle().left, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().left, letter.getRectangle().bottom)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().bottom)){
            return true;
        }*/
       if(letter.getRectangle().contains(rectangle.centerX(),rectangle.centerY())) return true;
        return false;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
