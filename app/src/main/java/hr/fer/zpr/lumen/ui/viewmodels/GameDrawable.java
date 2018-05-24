package hr.fer.zpr.lumen.ui.viewmodels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public abstract class GameDrawable {

    protected Bitmap image;
    protected Rect rectangle;
    protected int height;
    protected int width;

    public GameDrawable(){}

    public GameDrawable(Bitmap image, Rect bounds) {
        this.image = image;
        this.rectangle = bounds;
        height = rectangle.top - rectangle.bottom;
        width = rectangle.right - rectangle.left;
    }


    public boolean isTouched(MotionEvent event) {
        if (rectangle.contains((int) event.getX(), (int) event.getY())) return true;
        return false;
    }

    public abstract void handleTouch(MotionEvent event);

    public void draw(Canvas canvas) {
        if(image==null) return;
        canvas.drawBitmap(image, null, rectangle, new Paint());
    }
}
