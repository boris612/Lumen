package hr.fer.zpr.lumen.ui.numbergame.models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class DigitModel extends GameDrawable {

    private String value;

    public DigitModel(String value, Bitmap image, Rect bounds){
        super(image, bounds);
        this.value = value;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, null, rectangle, new Paint());
        super.draw(canvas);
    }

}
