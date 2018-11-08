package hr.fer.zpr.lumen.ui.wordgame;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.util.SparseArray;
import android.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import android.widget.*;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.ui.viewmodels.CoinModel;
import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import static com.google.android.gms.internal.zzagr.runOnUiThread;


public class WordGameView extends SurfaceView implements SurfaceHolder.Callback {

    public static final int MILLIS_PER_FRAME = 33;
    @Inject
    WordGamePresenter presenter;
    @Inject
    WordGameManager manager;
    private Disposable gameLoopDisposable = Disposables.disposed();
    private List<LetterFieldModel> fields = new ArrayList<>();
    private List<LetterModel> letters = new ArrayList<>();
    private List<GameDrawable> drawables = new ArrayList<>();
    private List<LetterModel> listOutsideScroll = new ArrayList<>();
    private SparseArray<LetterModel> mLetterPointer = new SparseArray<>();
    private CoinModel coin;
    private Context context;
    private int screenHeight;
    private int screenWidth;
    private ImageModel image;
    private LetterModel draggedLetter;
    private LetterFieldModel fieldOfLetterDraggedOutOffield;
    private HorizontalScrollView scrollView;
    private Canvas canvas;

    public WordGameView(LumenApplication context) {
        super(context);
        this.context = context;
        context.getApplicationComponent().inject(this);
        getHolder().addCallback(this);
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setScrollView(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoopDisposable = Flowable.interval(MILLIS_PER_FRAME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> updateView(holder), x -> {
                    Log.d("error", Log.getStackTraceString(x));
                });
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameLoopDisposable.dispose();
    }

    private void updateView(SurfaceHolder holder) {
        updateAddingLettersToFields(false);
        Canvas canvas = holder.lockCanvas();
        this.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        List<GameDrawable> copy = new ArrayList<>(drawables);
        for (GameDrawable drawable : drawables) drawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!presenter.shouldHandleTouch()) return super.onTouchEvent(event);
        boolean handled = false;
        LetterModel touchedLetter;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLetterPointer.clear();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                if (coin.isTouched(xTouch, yTouch)) presenter.hintPressed();
                touchedLetter = getTouchedLetter(xTouch, yTouch);
                if (touchedLetter == null) return true;
                touchedLetter.setCenter(xTouch, yTouch);
                setLetterBeingDragged(touchedLetter);


                mLetterPointer.put(event.getPointerId(0), touchedLetter);

                invalidate();
                handled = true;
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                pointerId = event.getPointerId(actionIndex);


                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);


                touchedLetter = getTouchedLetter(xTouch, yTouch);
                if (touchedLetter == null) return true;

                mLetterPointer.put(pointerId, touchedLetter);
                touchedLetter.setCenter(xTouch, yTouch);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();


                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedLetter = mLetterPointer.get(pointerId);

                    if (null != touchedLetter) {
                        touchedLetter.setCenter(xTouch, yTouch);
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                mLetterPointer.clear();
                invalidate();
                handled = true;
                updateAddingLettersToFields(true);
                setLetterBeingDragged(null);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                pointerId = event.getPointerId(actionIndex);

                mLetterPointer.remove(pointerId);
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

    private void updateAddingLettersToFields(boolean actionUpJustOccured) {
        outerLoop:
        for (LetterFieldModel field : fields) {
            LetterModel letterInside = field.getLetterInside();
            if (letterInside != null && !field.getRect().contains(letterInside.getRect().centerX(), letterInside.getRect().centerY())) {
                field.detachLetter();
                presenter.letterRemoved(field);
            }

            for (LetterModel letter : letters) {
                if (!actionUpJustOccured) {
                    continue;
                }
                if (field.getRect().contains(letter.getRect().centerX(), letter.getRect().centerY()) && letter == draggedLetter && fieldOfLetterDraggedOutOffield != field) {
                    Point newCenter = field.getCenter();
                    if (letterInside != null && letterInside != letter) {
                        if (draggedLetter.getRect().centerY() > field.getRect().centerY()) {
                            letterInside.setCenter(field.getRect().centerX(), field.getRect().top - letterInside.getHeight() / 2);
                        } else {
                            letterInside.setCenter(field.getRect().centerX(), field.getRect().bottom + letterInside.getHeight() / 2);
                        }

                    }
                    if (field.getLetterInside() != letter) {
                        field.attachLetter(letter);
                        presenter.letterInserted(letter, field);
                        letter.setCenter(newCenter.x, newCenter.y);
                    }

                }
            }
        }
    }

    public void setLetterBeingDragged(LetterModel letterBeingDragged) {
        if (letterBeingDragged == null) {
            fieldOfLetterDraggedOutOffield = null;
            return;
        }
        this.draggedLetter = letterBeingDragged;

        for (LetterFieldModel field : fields) {
            if (field.getLetterInside() == letterBeingDragged) {
                fieldOfLetterDraggedOutOffield = field;
                field.attachLetter(null);
                presenter.letterRemoved(field);
                break;
            }
        }
    }


    private LetterModel getTouchedLetter(int x, int y) {
        for (LetterModel letter : letters) {
            if (letter.isTouched(x, y)) return letter;
        }
        return null;
    }


    public void addDrawable(GameDrawable drawable) {
        drawables.add(drawable);
    }

    public void clearDrawables() {
        drawables.clear();
        drawables.add(coin);
    }

    public List<GameDrawable> getDrawables() {
        return drawables;
    }

    public void removeDrawable(GameDrawable drawable) {
        this.drawables.remove(drawable);
    }

    public void addDrawables(List<GameDrawable> drawables) {
        this.drawables.addAll(drawables);
    }

    public void addFields(List<LetterFieldModel> fields) {
        this.drawables.addAll(fields);
        this.fields = fields;
    }

    public void addAllLetters(List<LetterModel> letters) {

       /* Thread one = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                LinearLayout linearLayout = new LinearLayout(context);
                SurfaceView newView = new SurfaceView(context);
                linearLayout.addView(newView);
                List<GameDrawable> copy = new ArrayList<>(lettersToDrawables);
                for (GameDrawable letter : copy) {
                    newView.draw(canvas);
                    letter.draw(canvas);
                }
                scrollView.addView(linearLayout);
            }
        };
        one.start();*/
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setVisibility(ViewGroup.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView imageView = null;
        List<Letter> letterList = manager.getAllLetters().blockingGet();
        for (LetterModel letter : letters) {
            imageView = new ImageView(context);

            imageView.setImageBitmap(letter.getImage());

            imageView.setLayoutParams(layoutParams);
            imageView.getLayoutParams().height = context.getResources().getDisplayMetrics().heightPixels / 5;
            imageView.getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels / 12;
            linearLayout.addView(imageView);
            imageView.setBaselineAlignBottom(true);


            imageView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //letter.setRect(new Rect(50, 50 , 250, 250));

//                    boolean handled = false;
//                    LetterModel touchedLetter;
                    int xTouch;
                    int yTouch;
//                    int pointerId;
                    List<LetterFieldModel> letterFieldModels = presenter.getFields();
                    boolean flag = true;
                    if (!listOutsideScroll.isEmpty()) {
                        for (LetterFieldModel model : letterFieldModels) {
                            for (LetterModel outside : listOutsideScroll){
                                if (outside.equals(model.getLetterInside())) {
                                    flag = false;
                                }
                                if (!flag) {
                                    WordGameView.this.removeDrawable(outside);
                                    WordGameView.this.draw(canvas);
                                    break;
                                }
                            }
                        }
                    }


                    xTouch = (int) view.getWidth();
                    yTouch = (int) view.getHeight();
                    int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);
                    try {
                        Bitmap image = BitmapFactory.decodeStream(context.getAssets().open(letterList.get(letters.indexOf(letter)).image.path));
                        LetterModel letter1 = new LetterModel(new String(letter.getValue()), image, new Rect(letter.getRect()));
                        letters.add(letter1);
                        listOutsideScroll.add(letter1);
                        letter1.setCenter(coordinates[0], coordinates[1]);
                        WordGameView.this.addDrawable(letter1);
                        WordGameView.this.draw(canvas);
                    } catch (IOException ex) {
                        Log.d("error", ex.getMessage());
                    }

//                    if (coin.isTouched(xTouch, yTouch)) presenter.hintPressed();
//                    touchedLetter = getTouchedLetter(xTouch, yTouch);
//                    if (touchedLetter == null) return true;
//                    touchedLetter.setCenter(xTouch, yTouch);
//                    setLetterBeingDragged(touchedLetter);
//
//                    invalidate();
//                    handled = true;
                    return false;
                }
            });

            imageView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    letter.setCenter(50, 50);
                    return true;
                }
            });
        }
        ImageView finalImageView = imageView;
        runOnUiThread(() -> {
            scrollView.addView(linearLayout);
            scrollView.setSmoothScrollingEnabled(true);
            scrollView.setY(context.getResources().getDisplayMetrics().heightPixels - finalImageView.getLayoutParams().height - 10);
        });
        this.letters = letters;

    }

    public void addLetters(List<LetterModel> letters) {
        this.drawables.addAll(letters);
        this.letters = letters;
    }


    public void setCoin(CoinModel model) {
        this.coin = model;
        this.drawables.add(coin);
    }


}
