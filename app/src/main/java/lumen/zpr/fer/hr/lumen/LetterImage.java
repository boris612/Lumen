package lumen.zpr.fer.hr.lumen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Karlo on 7.11.2017..
 */

public class LetterImage {

    private Rect rectangle;
    private Drawable img;

    public Rect getRectangle(){
        return rectangle;
    }

    public LetterImage(Rect rectangle, Drawable img) {
        this.rectangle=rectangle;
        this.img=img;
        img.setBounds(rectangle); //reference dobre?
    }

    //TODO metode u interface ???
    public void draw(Canvas canvas) {
        /*
        Paint paint=new Paint();
        paint.set...
        canvas.drawRec...;
         */
        img.draw(canvas);
    }

    public void update() {
    //TODO ?
    }

    public void update(Point point) {
        rectangle.set(point.x-rectangle.width()/2, point.y-rectangle.height()/2,
                point.x+rectangle.width()/2, point.y+rectangle.height()/2 );
        img.setBounds(rectangle);
    }
}
