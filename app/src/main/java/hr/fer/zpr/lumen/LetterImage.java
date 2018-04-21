package hr.fer.zpr.lumen;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Karlo on 7.11.2017..
 */

public class LetterImage {
    private Rect rectangle;
    private Drawable img;
    private boolean updateable = false;
    private Point center;
    private String letter;

    public LetterImage(Point center, Drawable img, String letter, int width, int height) {
        this.center = center;
        this.img = img;
        this.rectangle = new Rect(center.x - GameLayoutConstants.DEFAULT_RECT_WIDTH / 2,
                center.y - GameLayoutConstants.DEFAULT_RECT_HEIGHT / 2,
                center.x - GameLayoutConstants.DEFAULT_RECT_WIDTH / 2 + width,
                center.y - GameLayoutConstants.DEFAULT_RECT_HEIGHT / 2 + height);
        img.setBounds(rectangle);
        this.letter = letter;
    }

    public LetterImage(Point center, Drawable img, Character letter, int width, int height) {
        this(center, img, Character.toString(letter), width, height);
    }

    public String getLetter() {
        return letter;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public boolean insideRectangle(int x, int y) {
        if (rectangle.contains(x, y))
            return true;
        return false;
    }


    public void draw(Canvas canvas) {
        img.draw(canvas);
    }

    public void update() {
        rectangle.set(center.x - rectangle.width() / 2, center.y - rectangle.height() / 2,
                center.x + rectangle.width() / 2, center.y + rectangle.height() / 2);
        img.setBounds(rectangle);
    }

    public void update(Point point) {
        rectangle.set(point.x - rectangle.width() / 2, point.y - rectangle.height() / 2,
                point.x + rectangle.width() / 2, point.y + rectangle.height() / 2);
        img.setBounds(rectangle);
    }

    public Drawable getDrawable() {
        return this.img;
    }

    public void setDrawable(Drawable dw) {
        img = dw;
    }

    public void setRectangle(Rect rect) {
        this.rectangle = rect;
    }
}
