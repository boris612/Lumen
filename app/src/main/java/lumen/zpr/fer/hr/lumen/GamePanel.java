package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
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

    private GameInstance instance;

    private GameImage currentImage;
    private String currentWord;
    private CharactersFields charactersFields;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

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
        String language = "hrvatski";
        String category = "priroda";

        instance = new GameInstance(this.getContext(), language, category, 1);

        String currentWord = instance.getWord();
        //TODO: dodati kod koji određuje (uzimajući u obzir kategoriju/težinu) sljedecu rijec

        currentImage = loadImage(instance.getPath());
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
        thread = new MainThread(getHolder(),this);
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
        if(phase == GamePhase.PRESENTING_WORD) {
            return true;
        }
        //TODO dodati imoplementaciju za TYPING_WORD fazu
        return super.onTouchEvent(event);
    }

    public void update() {
        if(phase == GamePhase.PRESENTING_WORD) {
            updateWordPresentation();
            return;
        }
        //TODO dodati implementaciju za TYPING_WORD fazu
     }

     private void updateWordPresentation() {
        //TODO dodati implementaciju
     }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //TODO dodati crtanje objekata zajedničkih objema fazama

        canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);

        if(phase == GamePhase.PRESENTING_WORD) {
            //TODO dodati crtanje objekata karakterističnih za PRESENTING_WORD fazu
            return;
        }

        charactersFields.draw(canvas);
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

    }
}
