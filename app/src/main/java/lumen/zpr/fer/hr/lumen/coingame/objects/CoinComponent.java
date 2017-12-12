package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import lumen.zpr.fer.hr.lumen.coingame.ConstantsUtil;

/**
 * Komponenta koja reprezentira kovanicu. Sadrzi sliku, poziciju i vrijednost.
 * Created by Zlatko on 12-Dec-17.
 */

public class CoinComponent implements CoinGameObject {
    /**
     * Slika komponente
     */
    private Drawable img;
    /**
     * Trenutna pozicija komponente
     */
    private Point position;
    /**
     * Vrijednost komponente
     */
    private int value;

    /**
     * Konstruktor.
     *
     * @param img      slika komponente
     * @param position pocetna pozicija
     * @param value    vrijednost koju predstavlja komponeneta
     */
    public CoinComponent(Drawable img, Point position, int value) {
        this.img = img;
        this.position = position;
        this.value = value;
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

    /**
     * Pomice poziciju komponente na danu tocku.
     *
     * @param point nova pozicija komponente
     */
    public void update(Point point) {
        position = new Point(point);
        img.setBounds(point.x - ConstantsUtil.COIN_SIZE / 2, point.y - ConstantsUtil.COIN_SIZE / 2, point.x + ConstantsUtil.COIN_SIZE / 2, point.y + ConstantsUtil.COIN_SIZE / 2);
    }

    @Override
    public boolean isSelected(int x, int y) {
        return img.getBounds().contains(x, y);
    }

    /**
     * Getter.
     *
     * @return slika komponente
     */
    public Drawable getImg() {
        return img;
    }

    /**
     * Getter.
     *
     * @return vrijednost komponente
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter.
     *
     * @return trenutna pozicija komponente
     */
    public Point getPosition() {
        return position;
    }
}
