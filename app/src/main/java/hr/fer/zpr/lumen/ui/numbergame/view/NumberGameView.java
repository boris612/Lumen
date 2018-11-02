package hr.fer.zpr.lumen.ui.numbergame.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;

public class NumberGameView extends SurfaceView {

    public NumberGameView(LumenApplication context) {
        super(context);
        //       context.getApplicationComponent().inject(this);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
    }
}