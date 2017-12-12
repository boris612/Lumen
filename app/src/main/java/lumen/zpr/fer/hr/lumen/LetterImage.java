package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by Karlo on 7.11.2017..
 */

public class LetterImage {
    private Rect rectangle;
    private Drawable img;
    private boolean updateable=false;
    private Point center;
    private Point oldCenter;
    private Character letter;

    public LetterImage(Point center, Drawable img, Character letter) {
        this.center=center;
        this.img=img;
        this.rectangle=new Rect (center.x-GameLayoutConstants.DEFAULT_RECT_WIDTH/2,
                center.y-GameLayoutConstants.DEFAULT_RECT_HEIGHT/2,
                center.x+GameLayoutConstants.DEFAULT_RECT_WIDTH/2,
                center.y+GameLayoutConstants.DEFAULT_RECT_HEIGHT/2 );
        img.setBounds(rectangle);
        this.letter=letter;
    }

    public boolean collision (LetterImage letter){
        if(rectangle.contains(letter.getRectangle().left, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().top)
                || rectangle.contains(letter.getRectangle().left, letter.getRectangle().bottom)
                || rectangle.contains(letter.getRectangle().right, letter.getRectangle().bottom)){
            return true;
        }
        return false;
    }


    public Character getLetter(){
        return letter;
    }
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        oldCenter=this.center;
        this.center = center;
    }
    public void setOldCenter() {
        center = oldCenter;
    }

    public void setOldCenter(Point center){ oldCenter=center;}
    public boolean isUpdateable() {
        return updateable;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    public Rect getRectangle(){
        return rectangle;
    }

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
        rectangle.set(center.x-rectangle.width()/2, center.y-rectangle.height()/2,
                center.x+rectangle.width()/2, center.y+rectangle.height()/2 );
        img.setBounds(rectangle);
    }

    //potrebno u ovoj inacici?
    public void update(Point point) {
        rectangle.set(point.x-rectangle.width()/2, point.y-rectangle.height()/2,
                point.x+rectangle.width()/2, point.y+rectangle.height()/2 );
        img.setBounds(rectangle);
    }
    public Drawable getDrawable(){
        return this.img;
    }
    public void setRectangle(Rect rect){
        this.rectangle=rect;
    }
}
