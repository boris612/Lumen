package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

public class WordGameView extends SurfaceView implements SurfaceHolder.Callback {

    private Disposable gameLoopDisposable = Disposables.disposed();
    private WordGamePresenter presenter;
    private Context context;
    List<GameDrawable> drawables = new ArrayList<>();

    public WordGameView(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);
        presenter = new WordGamePresenter(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoopDisposable = Flowable.interval(33, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignore -> updatePanel(getHolder()));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameLoopDisposable.dispose();
    }

    private void updatePanel(SurfaceHolder holder) {
        presenter.update();
        Canvas canvas = holder.lockCanvas();
        this.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        for (GameDrawable drawable : drawables) drawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public void addDrawable(GameDrawable drawable) {
        drawables.add(drawable);
    }
}
