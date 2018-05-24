package hr.fer.zpr.lumen.ui.wordgame;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.ComponentFactory;
import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Word;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import wordgame.db.database.WordGameDatabase;


public class WordGameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int MILLIS_PER_FRAME = 33;
    private Disposable gameLoopDisposable = Disposables.disposed();


    private List<GameDrawable> drawables = new ArrayList<>();

    private Context context;

    private int screenHeight;

    private int screenWidth;

    private ImageModel image;

    @Inject
    WordGamePresenter presenter;


    public WordGameView(LumenApplication context) {
        super(context);
        this.context = context;
        context.getApplicationComponent().inject(this);
        getHolder().addCallback(this);
        screenHeight=context.getResources().getDisplayMetrics().heightPixels;
        screenWidth=context.getResources().getDisplayMetrics().widthPixels;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoopDisposable = Flowable.interval(MILLIS_PER_FRAME, TimeUnit.MILLISECONDS)
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
        Canvas canvas = holder.lockCanvas();
        this.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        List<GameDrawable> copy=new ArrayList<>(drawables);
        for (GameDrawable drawable : copy) drawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return super.onDragEvent(event);
    }

    public void addDrawable(GameDrawable drawable) {
        drawables.add(drawable);
    }

    public void clearDrawables(){
        drawables.clear();
    }

    public void removeDrawable(GameDrawable drawable){
        this.drawables.remove(drawable);
    }


}
