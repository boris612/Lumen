package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.DisplayMetrics;
import android.util.Log;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 16.11.2017..
 */

public class CoinComponent {
    private static final double COMPONENT_X_AXIS = .022;
    private static final int COMPONENT_Y_AXIS = 300;
    private static double IMAGE_X_COORDINATE_FACTOR = GameLayoutConstants.COIN_IMAGE_X_COORDINATE_FACTOR;
    private static double IMAGE_Y_COORDINATE_FACTOR = GameLayoutConstants.COIN_IMAGE_Y_COORDINATE_FACTOR;
    private static double IMAGE_WIDTH_FACTOR = GameLayoutConstants.COIN_IMAGE_WIDTH_FACTOR;
    private static int NUMBER_FONT_SIZE = GameLayoutConstants.COIN_NUMBER_FONT_SIZE;
    private static double COIN_IMAGE_AND_TEXT_GAP_WIDTH_FACTOR = GameLayoutConstants.COIN_IMAGE_AND_TEXT_GAP_WIDTH_FACTOR;
    private int coins = 0;
    private Label label;
    private Drawable coinImage;
    private Rect rectangle;

    public CoinComponent(Drawable coinImage, int coins, Context context) {
        this.coinImage = coinImage;
        this.coins = coins;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();


        int display_width = dm.widthPixels;
        int display_height = dm.heightPixels;
        int coinWidth = (int)(display_width*IMAGE_WIDTH_FACTOR);
        int coinPosX = (int)(display_width*IMAGE_X_COORDINATE_FACTOR);
        int coinPosY = (int)(display_height*IMAGE_Y_COORDINATE_FACTOR);
        rectangle=new Rect(coinPosX,coinPosY,coinPosX+coinWidth,coinPosY+coinWidth);
        Log.d("COIN",""+coinPosX+" "+coinPosY);

        coinImage.setBounds(coinPosX,coinPosY,coinPosX+coinWidth,coinPosY+coinWidth);
        label = new Label( Integer.toString(coins),
                new Point(coinPosX+coinWidth/2,coinPosY+(int)(coinWidth*1.75) ),
                Color.BLACK, coinWidth);

    }

    public void addCoins(int coinsToAdd) {
        coins+=coinsToAdd;
        label.setText(Integer.toString(coins));
    }

    public void draw(Canvas canvas) {
        coinImage.draw(canvas);
        label.draw(canvas);
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public int getCoins(){
        return coins;
    }
}
