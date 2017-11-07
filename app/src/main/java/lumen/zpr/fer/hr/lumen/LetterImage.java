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
    private static final int APPROXIMATION = 20;
    private Rect rectangle;
    private Drawable img;
    private boolean updateable=false;


    public boolean isUpdateable() {
        return updateable;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public LetterImage(Rect rectangle, Drawable img) {
        this.rectangle=rectangle;
        this.img=img;
        img.setBounds(rectangle);
    }

    //TODO metode u interface ???
    public boolean insideRectangle(int x, int y){
        if(rectangle.contains(x,y))
            return true;
        updateable=false;
        return false;
    }

    public void draw(Canvas canvas) {
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
