package lumen.zpr.fer.hr.lumen.coingame.objects;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by Zlatko on 12-Dec-17.
 */

public interface CoinGameObject {
    void draw(Canvas canvas);

    void update();

    boolean isSelected(int x, int y);
}
