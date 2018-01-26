package hr.fer.zpr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * Komponenta koja reprezentira kovanicu. Sadrzi sliku, poziciju i vrijednost.
 * @author Zlatko Verk
 * @author Darian Šarić
 */

public class CoinGameComponent implements CoinGameObject {
    /**
     * Inicijalna pozicija komponente
     */
    private final Point initPosition;
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
     * Duljina/sirina komponente
     */
    private int size;
    private boolean selected;

    /**
     * Konstruktor.
     *
     * @param img      slika komponente
     * @param position pocetna pozicija
     * @param value    vrijednost koju predstavlja komponeneta
     * @param size     duljina/sirina komponente
     */
    public CoinGameComponent(Drawable img, Point position, int value, int size) {
        this.img = img;
        this.initPosition = position;
        this.position = position;
        this.value = value;
        this.size = size;
        img.setBounds(position.x - size / 2, position.y - size / 2, position.x + size / 2, position.y + size / 2);
    }

    @Override
    public void draw(Canvas canvas) {
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
        img.setBounds(position.x - size / 2, position.y - size / 2,
                position.x + size / 2, position.y + size / 2);
    }

    @Override
    public boolean isSelected(int x, int y) {
        return img.getBounds().contains(x, y);
    }

    @Override
    public boolean getSelection() {
        return selected;
    }

    @Override
    public void setSelection(boolean selected) {
        this.selected = selected;
    }

    /**
     * Getter.
     *
     * @return slika komponente
     */
    @Deprecated
    public Drawable getImg() {
        return img;
    }

    /**
     * Getter.
     *
     * @return vrijednost komponente
     */
    int getValue() {
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

    /**
     * Postavlja komponentu na poziciju na kojoj je bila pri stvaranju.
     */
    void resetPosition() {
        update(initPosition);
    }
}
