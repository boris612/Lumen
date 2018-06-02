package hr.fer.zpr.lumen.ui.coingame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class CoinModel extends GameDrawable {

    private int value;

    public CoinModel(Bitmap image, Rect rect, int value) {
        super(image, rect);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
