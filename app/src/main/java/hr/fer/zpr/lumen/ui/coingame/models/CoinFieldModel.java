package hr.fer.zpr.lumen.ui.coingame.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.HashSet;
import java.util.Set;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class CoinFieldModel extends GameDrawable {

    private Set<CoinModel> coins = new HashSet<>();

    public CoinFieldModel(Rect rect) {
        super(null, rect);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.rgb(239, 235, 112));
        canvas.drawRect(rectangle, paint);
    }

    public void addCoin(CoinModel coin) {
        coins.add(coin);
    }

    public void removeCoin(CoinModel coin) {
        coins.remove(coin);
    }

    public boolean containsCoin(CoinModel coin) {
        return coins.contains(coin);
    }

}
