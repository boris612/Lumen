package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(),this);

        setFocusable(true);


        phase = GamePhase.PRESENTING_WORD;

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
        StartingHint hint=new StartingHint("konj",this);
        Bitmap bithint=hint.getHintBitmap();

        //TODO dodati crtanje objekata zajedničkih objema fazama

        if(phase == GamePhase.PRESENTING_WORD) {
            canvas.drawBitmap(bithint,10,10,new Paint());
            return;
        }

        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

        super.draw(canvas);
    }
}
