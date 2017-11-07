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



    //TODO metode u interface ???
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
    }

    //TODO bolja detekcija
    public boolean collision (LetterImage letter){
        if(rectangle.contains(letter.getRectangle().left, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().left, letter.getRectangle().bottom)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().bottom)){
            color=Color.WHITE;//pozadina
            return true;
        }
        color=Color.RED;
        return false;
    }

}
