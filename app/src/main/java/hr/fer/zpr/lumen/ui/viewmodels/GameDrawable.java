package hr.fer.zpr.lumen.ui.viewmodels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

public abstract class GameDrawable {

    protected Bitmap image;
    protected Rect rectangle;
    protected int height;
    protected int width;

    public GameDrawable() {
    }

    public GameDrawable(Bitmap image, Rect bounds) {
        this.image = image;
        this.rectangle = bounds;
        height = rectangle.bottom - rectangle.top;
        width = rectangle.right - rectangle.left;
    }


    public boolean isTouched(int x, int y) {
        if (rectangle.contains(x, y)) return true;
        return false;
    }


    public void draw(Canvas canvas) {
        if (image == null) return;

        canvas.drawBitmap(image, null, rectangle, new Paint());
    }

    public Point getCenter() {
        return new Point(getRect().centerX(), getRect().centerY());
    }

    public void setCenter(int x, int y) {
        rectangle.top = y - height / 2;
        rectangle.left = x - width / 2;
        rectangle.right = rectangle.left + width;
        rectangle.bottom = rectangle.top + height;
    }

    public Rect getRect() {
        return this.rectangle;
    }

    public void setRect(Rect rect) {
        this.rectangle = rect;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap getImage(){
        return this.image;
    }

}
