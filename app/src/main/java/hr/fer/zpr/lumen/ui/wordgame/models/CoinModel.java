package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class CoinModel extends GameDrawable {

    public static String coinImagePath="database/misc/coin.png";

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



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint pnt = new Paint();
        pnt.setTextSize((rectangle.right - rectangle.left) / 2);
        canvas.drawText(Integer.toString(coins), (float) rectangle.left, (float) rectangle.bottom + pnt.getTextSize(), pnt);
    }
}
