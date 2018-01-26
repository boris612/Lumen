package hr.fer.zpr.lumen.coingame.objects;

import android.graphics.Canvas;

/**
 * Sucelje koje specificira metode koje mora imati komponenta igre.
 * Created by Zlatko on 12-Dec-17.
 */

public interface CoinGameObject {
    /**
     * Crta komponentu na canvas.
     *
     * @param canvas canvas na koji se komponenta crta
     */
    void draw(Canvas canvas);

    /**
     * Osigurava da je komponenta "up to date".
     */
    void update();

    /**
     * Provjerava jesu li dane koordinate unutar komponente.
     *
     * @param x x koordinata
     * @param y y koordinata
     * @return true ako su dane koordinate unutar komponente, inace false
     */
    boolean isSelected(int x, int y);

    boolean getSelection();

    void setSelection(boolean selection);
}
