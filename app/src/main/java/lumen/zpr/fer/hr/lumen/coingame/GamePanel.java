package lumen.zpr.fer.hr.lumen.coingame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.coingame.objects.CoinComponent;
import lumen.zpr.fer.hr.lumen.coingame.objects.ContainerComponent;

/**
 * Created by Zlatko on 12-Dec-17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private CoinComponent coin;
    private ContainerComponent container;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        coin = new CoinComponent(getResources().getDrawable(R.drawable.coin_value_1), new Point(getHeight() / 2, getWidth() / 2), 1);
        container = new ContainerComponent(new Rect(getWidth() / 2, 20, getWidth() - 20, getHeight() / 2), new Point(getWidth() / 4, getHeight() / 2));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (coin.isSelected((int) event.getX(), (int) event.getY())) {
                    coin.update(new Point((int) event.getX(), (int) event.getY()));
                }
        }

        return true;
    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        container.draw(canvas);

        coin.draw(canvas);

    }
}
