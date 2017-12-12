package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import lumen.zpr.fer.hr.lumen.coingame.ConstantsUtil;

/**
 * Created by Zlatko on 12-Dec-17.
 */

public class CoinComponent implements CoinGameObject {
    private Drawable img;
    private Point position;
    private boolean fixed;
    private int value;

    public CoinComponent(Drawable img, Point position, int value) {
        this.img = img;
        this.position = position;
        this.value=value;
        img.setBounds(position.x - ConstantsUtil.COIN_SIZE / 2, position.y - ConstantsUtil.COIN_SIZE / 2, position.x + ConstantsUtil.COIN_SIZE / 2, position.y + ConstantsUtil.COIN_SIZE / 2);

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        img.draw(canvas);
    }

    @Override
    public void update() {

    }

    public void update(Point point) {
        if (fixed) {
            return;
        }
        position = new Point(point);
        img.setBounds(point.x - ConstantsUtil.COIN_SIZE / 2, point.y - ConstantsUtil.COIN_SIZE / 2, point.x + ConstantsUtil.COIN_SIZE / 2, point.y + ConstantsUtil.COIN_SIZE / 2);
    }

    @Override
    public boolean isSelected(int x, int y) {
        return img.getBounds().contains(x, y);
    }

    public Drawable getImg() {
        return img;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public boolean isFixed() {
        return fixed;
    }

    public int getValue() {
        return value;
    }
}
