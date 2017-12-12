package lumen.zpr.fer.hr.lumen.coingame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import lumen.zpr.fer.hr.lumen.R;
import lumen.zpr.fer.hr.lumen.coingame.objects.CoinComponent;
import lumen.zpr.fer.hr.lumen.coingame.objects.ContainerComponent;

/**
 * Razred koji sadrzi trenutno stanje igre i brine se o njenom toku. Zna iscrtavati trenutno stanje igre.
 * Created by Zlatko on 12-Dec-17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * Dretva koja osvjezava stanje ove igre
     */
    private MainThread thread;
    /**
     * Lista novcica koji se koriste
     */
    private List<CoinComponent> coins = new ArrayList<>();
    /**
     * Container u koji se dovlace selektirani novcici
     */
    private ContainerComponent container;

    /**
     * Konstruktor.
     *
     * @param context kontekst
     */
    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //Todo relativno postavljanje containera
        container = new ContainerComponent(new Rect(400, 100, 1800, 800), new Point(100, 300));

        //Todo automatsko generiranje kovanica
        //Todo dodati novcanice
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_1), new Point(400, 1100), 1));
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_1), new Point(600, 1100), 1));
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_1), new Point(800, 1100), 1));
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_2), new Point(1000, 1100), 2));
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_2), new Point(1200, 1100), 2));
        coins.add(new CoinComponent(getResources().getDrawable(R.drawable.coin_value_2), new Point(1400, 1100), 2));

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
                for (CoinComponent coin : coins) {
                    if (coin.isSelected((int) event.getX(), (int) event.getY())) {
                        coin.update(new Point((int) event.getX(), (int) event.getY()));
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                for (CoinComponent coin : coins) {
                    Point position = coin.getPosition();
                    if (container.isSelected(position.x, position.y)) {
                        container.addCoin(coin);
                    }
                }
        }
        return true;
    }

    /**
     * Osigurava da elementi igre budu "up to date".
     */
    public void update() {
        container.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        container.draw(canvas);

        for (CoinComponent coin : coins) {
            coin.draw(canvas);
        }
    }
}
