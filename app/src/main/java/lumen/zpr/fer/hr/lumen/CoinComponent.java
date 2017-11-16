package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 16.11.2017..
 */

public class CoinComponent {
    private int coins = 0;
    private Label label;
    private Drawable coinImage;

    public CoinComponent(Drawable coinImage, int coins, Context context) {
        this.coinImage = coinImage;
        this.coins = coins;
        label = new Label(Integer.toString(coins), new Point(0,300), Color.BLACK,GameLayoutConstants.COIN_NUMBER_FONT_SIZE);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int display_width = dm.widthPixels;
        int coinWidth = (int)(display_width*GameLayoutConstants.COIN_IMAGE_WIDTH_FACTOR);
        coinImage.setBounds(0,0,coinWidth,coinWidth);
    }

    public void addCoins(int coinsToAdd) {
        coins+=coinsToAdd;
        label.setText(Integer.toString(coins));
    }

    public void draw(Canvas canvas) {
        coinImage.draw(canvas);
        label.draw(canvas);
    }
}
