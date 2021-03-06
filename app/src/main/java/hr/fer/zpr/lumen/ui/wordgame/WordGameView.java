package hr.fer.zpr.lumen.ui.wordgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    private SparseArray<LetterModel> mLetterPointer = new SparseArray<>();
    private CoinModel coin;
    private Context context;
    private int screenHeight;
    private int screenWidth;
    private ImageModel image;
    private LetterModel draggedLetter;
    private LetterFieldModel fieldOfLetterDraggedOutOffield;
    private HorizontalScrollView scrollView;
    private ImageButton buttonLeft;
    private ImageButton buttonRight;
    private Canvas canvas;
    private LinearLayout linearLayout;
    private Map<TextView, LetterModel> mapModel = new HashMap<>();
    private Map<TextView, Letter> mapLetter = new HashMap<>();
    private List<LetterModel> listOutsideScroll = new ArrayList<>();

    public WordGameView(LumenApplication context) {
        super(context);
        this.context = context;
        this.linearLayout = new LinearLayout(context);
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

    public Map<TextView, LetterModel> getMapModel() {
        return mapModel;
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
        List<GameDrawable> copy = new CopyOnWriteArrayList<>(drawables);
        for (GameDrawable drawable : copy) drawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!presenter.shouldHandleTouch()) return super.onTouchEvent(event);
        boolean handled = false;
        LetterModel touchedLetter = null;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLetterPointer.clear();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                if (coin.isTouched(xTouch, yTouch)) {
                    presenter.hintPressed();
                }

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

                    if (yTouch + letters.get(0).getHeight() >= this.getHeight() - scrollView.getHeight()) {
                        return true;
                    }
                    touchedLetter = mLetterPointer.get(pointerId);

                    if (null != touchedLetter) {
                        touchedLetter.setCenter(xTouch, yTouch);
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                if (!listOutsideScroll.isEmpty()) {
                    for (LetterModel out : listOutsideScroll) {
                        if (drawables.contains(out)){
                            boolean isInField = false;
                            for (LetterFieldModel field : fields) {
                                if (field.getRect().contains(out.getCenter().x, out.getCenter().y)){
                                    isInField = true;
                                    break;
                                }
                            }
                            if (!isInField) {
                                WordGameView.this.removeDrawable(out);
                            }
                        }
                    }
                }
                mLetterPointer.clear();
                invalidate();
                handled = true;
                updateAddingLettersToFields(true);
                setLetterBeingDragged(null);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (!listOutsideScroll.isEmpty()) {
                    for (LetterModel out : listOutsideScroll) {
                        if (drawables.contains(out)){
                            boolean isInField = false;
                            for (LetterFieldModel field : fields) {
                                if (field.getRect().contains(out.getCenter().x, out.getCenter().y)){
                                    isInField = true;
                                    break;
                                }
                            }
                            if (!isInField) {
                                WordGameView.this.removeDrawable(out);
                            }
                        }
                    }
                }
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

    public void updateAddingLettersToFields(boolean actionUpJustOccured) {
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
                        letterInside.setCenter(field.getRect().centerX(), field.getRect().top - letterInside.getHeight() / 2);
                    }
                    if (field.getLetterInside() != letter) {
                        field.attachLetter(letter);
                        presenter.letterInserted(letter, field);
                        letter.setCenter(newCenter.x, newCenter.y);
                    }

                    if (!listOutsideScroll.isEmpty()) {
                        for (LetterModel out : listOutsideScroll) {
                            if (drawables.contains(out)){
                                boolean isInField = false;
                                for (LetterFieldModel field1 : fields) {
                                    if (field1.getRect().contains(out.getCenter().x, out.getCenter().y)){
                                        isInField = true;
                                        break;
                                    }
                                }
                                if (!isInField) {
                                    WordGameView.this.removeDrawable(out);
                                }
                            }
                        }
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

    public HorizontalScrollView getScrollView() {
        return scrollView;
    }

    public void setLinearLayout(LinearLayout layout){this.linearLayout=layout;}


    public void addDrawable(GameDrawable drawable) {
        drawables.add(drawable);
    }

    public void clearDrawables() {
        drawables.clear();
        drawables.add(coin);
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
        this.letters = letters;

        scrollView.smoothScrollTo(0, 0);

        linearLayout.setVisibility(ViewGroup.VISIBLE);
        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView textView = null;
        List<Letter> letterList = manager.getAllLetters().blockingGet();

        OnClickListener onClickListenerButtonLeft = new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(scrollView.getScrollX()-scrollView.getWidth()+letters.get(0).getWidth()+60,0);
            }
        };

        OnClickListener onClickListenerButtonRight = new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(scrollView.getScrollX()+scrollView.getWidth()-letters.get(0).getWidth()-60,0);
            }
        };

        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
//                if (!listOutsideScroll.isEmpty()) {
//                    for (LetterModel out : listOutsideScroll) {
//                        boolean isInField = false;
//                        for (LetterFieldModel field : fields) {
//                            if (field.containsLetter() && out.isAttachedTo(field)) {
//                                isInField = true;
//                                break;
//                            }
//                        }
//                        if (!isInField) {
//                            if (drawables.contains(out)) WordGameView.this.removeDrawable(out);
//                        }
//                    }
//                }
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                return true;
            }
        };

        DoubleClickListener doubleClickListener = new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                if (!listOutsideScroll.isEmpty()) {
                    for (LetterModel out : listOutsideScroll) {
                        boolean isInField = false;
                        for (LetterFieldModel field : fields) {
                            if (field.containsLetter() && out.isAttachedTo(field)) {
                                isInField = true;
                                break;
                            }
                        }
                        if (!isInField) {
                            if (drawables.contains(out)) WordGameView.this.removeDrawable(out);
                        }
                    }
                }
                if(manager.isHintActive().blockingGet()){
                    LetterModel lPressed = mapModel.get(v);
//                    LetterModel l = presenter.getHintLetter();
                    LetterFieldModel f = presenter.getHintField();
//                    if(lPressed.getValue().equals(l.getValue())){
                        if(f.containsLetter()) {
                            LetterModel letterInside = f.getLetterInside();
                            letterInside.setCenter(f.getRect().centerX(), f.getRect().top - letterInside.getHeight() / 2);
                        }
                        LetterModel letter1 = new LetterModel(lPressed.getValue(), lPressed.getImage(), new Rect(lPressed.getRect()));
                        letter1.setCenter(f.getCenter().x, f.getCenter().y);
                        letters.add(letter1);
                        listOutsideScroll.add(letter1);
                        setLetterBeingDragged(letter1);
                        WordGameView.this.addDrawable(letter1);
                        WordGameView.this.draw(canvas);
                        updateAddingLettersToFields(true);

//                    }
                }else {
                    for(LetterFieldModel field: fields){
                        if(!field.containsLetter()){
                            LetterModel letter1 = new LetterModel(mapModel.get(v).getValue(), mapModel.get(v).getImage(), new Rect(mapModel.get(v).getRect()));
                            letters.add(letter1);
                            listOutsideScroll.add(letter1);
                            letter1.setCenter(field.getCenter().x, field.getCenter().y);
                            setLetterBeingDragged(letter1);
                            WordGameView.this.addDrawable(letter1);
                            WordGameView.this.draw(canvas);
                            updateAddingLettersToFields(true);
                            break;

                        }
                    }
                }
            }
        };

        OnLongClickListener onLongClickListener = view -> {
            if (!listOutsideScroll.isEmpty()) {
                for (LetterModel out : listOutsideScroll) {
                    boolean isInField = false;
                    for (LetterFieldModel field : fields) {
                        if (field.containsLetter() && out.isAttachedTo(field)) {
                            isInField = true;
                            break;
                        }
                    }
                    if (!isInField) {
                        if (drawables.contains(out)) WordGameView.this.removeDrawable(out);
                    }
                }
            }
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
            return false;
        };

        for (LetterModel letter : letters) {
            textView = new TextView(context);
            textView.setAllCaps(true);
            textView.setTextSize(90);
            textView.setTextColor(Color.BLACK);
            textView.setText(letter.getValue());
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setPadding(30,0,30,0);
            mapModel.put(textView, letter);
            mapLetter.put(textView, letterList.get(letters.indexOf(letter)));
            textView.setLayoutParams(layoutParams);
            textView.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().heightPixels / 3.5);
            linearLayout.setBaselineAligned(true);
            linearLayout.addView(textView);

            //textView.setOnClickListener(doubleClickListener);

            //textView.setOnLongClickListener(onLongClickListener);

            textView.setOnTouchListener(onTouchListener);
        }
        TextView finalTextView = textView;

        WordGameView.this.setOnDragListener((v, dragEvent) -> {
            TextView draggedView = (TextView) dragEvent.getLocalState();
            WordGameView dropTarget = (WordGameView) v;
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    Point touchPosition = getTouchPositionFromDragEvent(v, dragEvent);
                    for(LetterFieldModel field: fields){
                        if ((touchPosition.y < context.getResources().getDisplayMetrics().heightPixels - finalTextView.getLayoutParams().height - 10) && field.getRect().contains(touchPosition.x, touchPosition.y) ) {
                            LetterModel letter1 = new LetterModel(mapModel.get(draggedView).getValue(), mapModel.get(draggedView).getImage(), new Rect(mapModel.get(draggedView).getRect()));
                            letters.add(letter1);
                            listOutsideScroll.add(letter1);
                            letter1.setCenter(touchPosition.x, touchPosition.y);
                            setLetterBeingDragged(letter1);
                            dropTarget.addDrawable(letter1);
                            dropTarget.draw(canvas);
                            updateAddingLettersToFields(true);
                        }
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        });

        runOnUiThread(() -> {
            buttonLeft.setOnClickListener(onClickListenerButtonLeft);
            buttonRight.setOnClickListener(onClickListenerButtonRight);

            buttonLeft.setVisibility(ViewGroup.VISIBLE);
            buttonRight.setVisibility(ViewGroup.VISIBLE);

            ViewGroup.LayoutParams params = buttonLeft.getLayoutParams();
            params.width=letters.get(0).getWidth()-2;
            params.height=300;
            buttonRight.setPadding(5,0,5,0);

            buttonLeft.setLayoutParams(params);
            buttonRight.setX(screenWidth-letters.get(0).getWidth());
            buttonRight.setLayoutParams(params);
            buttonRight.setY(context.getResources().getDisplayMetrics().heightPixels - mapLetter.entrySet().iterator().next().getKey().getLayoutParams().height);
            buttonLeft.setY(context.getResources().getDisplayMetrics().heightPixels - mapLetter.entrySet().iterator().next().getKey().getLayoutParams().height);
            resizeView(scrollView,screenWidth-2*letters.get(0).getWidth(), 350);
            scrollView.addView(linearLayout);
            scrollView.setY(context.getResources().getDisplayMetrics().heightPixels - mapLetter.entrySet().iterator().next().getKey().getLayoutParams().height-50);
            scrollView.setX(letters.get(0).getWidth());
            scrollView.setScrollbarFadingEnabled(false);
        });


    }

    private static Point getTouchPositionFromDragEvent(View item, DragEvent event) {
        Rect rItem = new Rect();
        item.getGlobalVisibleRect(rItem);
        return new Point(rItem.left + Math.round(event.getX()), rItem.top + Math.round(event.getY()));
    }

    public void addLetters(List<LetterModel> letters) {
        this.drawables.addAll(letters);
        this.letters = letters;
    }


    public void setCoin(CoinModel model) {
        this.coin = model;
        this.drawables.add(coin);
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setButtonRight(ImageButton buttonRight) {this.buttonRight=buttonRight; }

    public void setButtonLeft(ImageButton buttonLeft) {this.buttonLeft=buttonLeft; }

    public ImageButton getButtonLeft() {return this.buttonLeft;}

    public ImageButton getButtonRight() {return this.buttonRight;}

    private abstract class DoubleClickListener implements OnClickListener {
        private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds

        long lastClickTime = 0;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            }
            lastClickTime = clickTime;
        }

        public abstract void onDoubleClick(View v);
    }

    private void resizeView(View view, int newWidth, int newHeight) {
        try {
            Constructor<? extends ViewGroup.LayoutParams> ctor = view.getLayoutParams().getClass().getDeclaredConstructor(int.class, int.class);
            view.setLayoutParams(ctor.newInstance(newWidth, newHeight));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
