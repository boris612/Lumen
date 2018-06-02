package hr.fer.zpr.lumen.ui.viewmodels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class CoinModel extends GameDrawable {

    public static String coinImagePath = "database/misc/coin.png";

    private int coins;
    private boolean hintActive = false;

    public CoinModel(Bitmap image, Rect bounds, int coins) {
        super(image, bounds);
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void subtractCoins(int coins) {
        this.coins -= coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint pnt = new Paint();
        pnt.setTextSize((rectangle.right - rectangle.left) / 2);
        int stringWidth = (int) pnt.measureText(Integer.toString(coins));
        while ((rectangle.right - rectangle.left) / 2 - stringWidth / 2 < 0) {
            pnt.setTextSize(pnt.getTextSize() - 1);
            stringWidth = (int) pnt.measureText(Integer.toString(coins));
        }
        canvas.drawText(Integer.toString(coins), (float) (rectangle.right - rectangle.left) / 2 - stringWidth / 2, (float) rectangle.bottom + pnt.getTextSize(), pnt);
    }
}
