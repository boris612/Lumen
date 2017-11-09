package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;
    private GameImage currentImage;
    private String currentWord;
    private CharactersFields charactersFields;

    private Drawable img = getResources().getDrawable(R.drawable.image);
    private LetterImage letterA = new LetterImage(new Rect(100,100,200,200),img);
    private Point letterAPoint= new Point(150,150); //centar rect

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);

        phase = GamePhase.PRESENTING_WORD;
        phase = GamePhase.TYPING_WORD;

        try {
            initNewWord();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: pronaći odgovarajući postupak u ovoj situaciji
        }
    }

    private void initNewWord() throws  IOException{
        String currentWord = "motocikl";
        //TODO: dodati kod koji određuje (uzimajući u obzir kategoriju/težinu) sljedecu rijec

        currentImage = loadImage("motocikl.jpg");
        //TODO: napraviti pozive metode (i tu metodu) preko koje ce se dohvatiti ime slike za zadanu rijec

        charactersFields = new CharactersFields(currentWord,getContext());
    }

    private GameImage loadImage(String imageName) throws  IOException {
        return new GameImage(imageName,getContext());
    }


    private enum GamePhase {
        //faza u kojoj igra prikazuje sliku, slovka i ispisuje riječ
        PRESENTING_WORD,
        //faza u kojoj igrač piše (drag and dropanjem slova) riječ
        TYPING_WORD;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        while(true) {
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
        if (phase == GamePhase.PRESENTING_WORD) {
            return true;
        }
        //TODO dodati imoplementaciju za TYPING_WORD fazu
        //return super.onTouchEvent(event);

        // bolji drag and drop alg, listener?
        switch (event.getAction()){ //provjeri ako smo pritisnuli na objekt!
            case MotionEvent.ACTION_DOWN:
                if(letterA.insideRectangle((int)event.getX(), (int)event.getY())) {
                    letterA.setUpdateable(true);
                }
            case MotionEvent.ACTION_MOVE:
                        if(letterA.isUpdateable())
                            letterAPoint.set((int)event.getX(), (int)event.getY());
        }


        return true;
    }

    public void update() {
        if (phase == GamePhase.PRESENTING_WORD) {
            updateWordPresentation();
            return;
        }
        letterA.update(letterAPoint);

        DropArea area = charactersFields.getFieldThatCollidesWith(letterA);
        if(area!=null){
            Point areaCenter = area.getCenterPoint();
            letterAPoint.set(areaCenter.x,areaCenter.y);//centar drop area
        }
    }

    private void updateWordPresentation() {
        //TODO dodati implementaciju
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //TODO dodati crtanje objekata zajedničkih objema fazama
      
        canvas.drawColor(Color.WHITE); //zamjena za background

        canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);

        if(phase == GamePhase.PRESENTING_WORD) {
            //TODO dodati crtanje objekata karakterističnih za PRESENTING_WORD fazu
            return;
        }

        charactersFields.draw(canvas);
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu
        letterA.draw(canvas);

        //za provjeru dropa
       // if(!dropArea.collision(letterA)){
        //    dropArea.draw(canvas);
       // }


    }
}
