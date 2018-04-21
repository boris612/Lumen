package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class ImageModel extends GameDrawable {

    public ImageModel(Bitmap image, Rect bounds) {
        super(image, bounds);
    }

    @Override
    public void handleTouch(MotionEvent event) {

    }
}
