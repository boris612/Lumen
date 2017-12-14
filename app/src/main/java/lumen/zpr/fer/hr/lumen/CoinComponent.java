package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 16.11.2017..
 */

public class CoinComponent {
    private static final double COMPONENT_X_AXIS = .022;
    private static final int COMPONENT_Y_AXIS = 300;
    private int coins = 0;
    private Label label;
    private Drawable coinImage;

    public CoinComponent(Drawable coinImage, int coins, Context context) {
        this.coinImage = coinImage;
        this.coins = coins;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        label = new Label("Coins: " + Integer.toString(coins),
                new Point((int) (COMPONENT_X_AXIS * dm.widthPixels), COMPONENT_Y_AXIS),
                Color.BLACK, GameLayoutConstants.COIN_NUMBER_FONT_SIZE);
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
