package hr.fer.zpr.lumen.ui.coingame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.ui.DebugUtil;
import hr.fer.zpr.lumen.ui.coingame.models.CoinFieldModel;
import hr.fer.zpr.lumen.ui.coingame.models.CoinModel;
import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class CoinGameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int MILLIS_PER_FRAME = 33;
    @Inject
    CoinGamePresenter presenter;
    private Disposable loopDisposable;
    private SparseArray<CoinModel> coinPointers = new SparseArray<>();
    private List<CoinModel> coins = new ArrayList<>();
    private CoinFieldModel field;
    private CoinModel draggedCoin;
    private List<GameDrawable> drawables;

    public CoinGameView(LumenApplication context) {
        super(context);
        getHolder().addCallback(this);
        context.getApplicationComponent().inject(this);
        drawables = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        loopDisposable = Flowable.interval(MILLIS_PER_FRAME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> updateView(holder), x -> {
                    DebugUtil.LogDebug(x);
                });
    }

    private void updateView(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        this.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private void update() {
        List<CoinModel> copy = new ArrayList<>(coins);
        for (CoinModel coin : copy) {
            if (field.containsCoin(coin) && !field.getRect().contains(coin.getRect().centerX(), coin.getRect().centerY())) {
                presenter.coinRemoved(coin);
            } else if (!field.containsCoin(coin) && field.getRect().contains(coin.getRect().centerX(), coin.getRect().centerY())) {
                presenter.coinInserted(coin);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (loopDisposable != null) loopDisposable.dispose();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        List<GameDrawable> copy = new ArrayList<>(drawables);
        canvas.drawColor(Color.WHITE);
        for (GameDrawable gd : copy) {
            gd.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!presenter.shouldHandleTouch()) return super.onTouchEvent(event);

        boolean handled = false;
        CoinModel touchedCoin = null;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                coinPointers.clear();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                for (int i = coins.size() - 1; i >= 0; i--) {
                    if (coins.get(i).isTouched(xTouch, yTouch)) {
                        touchedCoin = coins.get(i);
                        break;
                    }
                }
                if (touchedCoin == null) return true;
                touchedCoin.setCenter(xTouch, yTouch);
                this.draggedCoin = touchedCoin;
                drawables.remove(draggedCoin);
                drawables.add(drawables.size(), draggedCoin);
                coins.remove(draggedCoin);
                coins.add(coins.size(), draggedCoin);


                coinPointers.put(event.getPointerId(0), touchedCoin);

                invalidate();
                handled = true;
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                pointerId = event.getPointerId(actionIndex);


                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);


                touchedCoin = null;
                for (int i = coins.size() - 1; i >= 0; i--) {
                    if (coins.get(i).isTouched(xTouch, yTouch)) {
                        touchedCoin = coins.get(i);
                        break;
                    }
                }
                if (touchedCoin == null) return true;
                drawables.remove(touchedCoin);
                drawables.add(drawables.size(), touchedCoin);
                coins.remove(touchedCoin);
                coins.add(coins.size(), touchedCoin);

                coinPointers.put(pointerId, touchedCoin);
                touchedCoin.setCenter(xTouch, yTouch);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();


                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedCoin = coinPointers.get(pointerId);

                    if (touchedCoin != null) {
                        touchedCoin.setCenter(xTouch, yTouch);
                        drawables.remove(touchedCoin);
                        drawables.add(drawables.size(), touchedCoin);
                        coins.remove(touchedCoin);
                        coins.add(coins.size(), touchedCoin);
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                coinPointers.clear();
                update();
                invalidate();
                handled = true;
                draggedCoin = null;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                update();
                pointerId = event.getPointerId(actionIndex);
                coinPointers.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    public void setCoins(List<CoinModel> coins) {
        this.coins = coins;
    }

    public void setField(CoinFieldModel field) {
        this.field = field;
        drawables.add(field);
    }

    public void addDrawable(GameDrawable drawable) {
        this.drawables.add(drawable);
    }

    public void clearCoins() {
        drawables.removeAll(coins);
    }

    public void removeDrawable(GameDrawable drawable) {
        drawables.remove(drawable);
    }

}
