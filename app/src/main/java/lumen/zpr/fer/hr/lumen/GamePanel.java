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
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import lumen.zpr.fer.hr.lumen.menus.Letter;

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
    private List<Letter> letters;

    private int coins = 0;

    //inicijalizacijski blok samo za potrebe testiranja
    {
        letters = new ArrayList<>();
        Drawable img2 = getResources().getDrawable(R.drawable.image);
        Drawable img3 = getResources().getDrawable(R.drawable.image);
        Drawable img4 = getResources().getDrawable(R.drawable.image);
        letters.add(new Letter('M',new LetterImage(new Rect(100,100,200,200),img)));
        letters.add(new Letter('O',new LetterImage(new Rect(200,100,300,200),img2)));
        letters.add(new Letter('T',new LetterImage(new Rect(300,100,400,200),img3)));
        letters.add(new Letter('O',new LetterImage(new Rect(400,100,500,200),img4)));
    }

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
        currentWord = "MOTO";
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
        TYPING_WORD,
        //faza u kojem se igraču ispisuje da je točno poslozio slova
        ENDING

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
            case MotionEvent.ACTION_UP:
                checkIfInputComplete();
                break;
            case MotionEvent.ACTION_DOWN:
                for(Letter l: letters) {
                    LetterImage limg = l.getLetterImage();
                    if(limg.insideRectangle((int)event.getX(), (int)event.getY())) {
                        limg.setUpdateable(true);
                    }
                }

            case MotionEvent.ACTION_MOVE:
                for(Letter l: letters) {
                    LetterImage limg = l.getLetterImage();
                    if(limg.isUpdateable())
                        limg.update(new Point((int)event.getX(), (int)event.getY()));
                }

        }

        return true;
    }

    public void update() {
        if (phase == GamePhase.PRESENTING_WORD) {
            updateWordPresentation();
            return;
        }
        //letterA.update(letterAPoint);

        for(Letter l: letters) {
            LetterImage limg = l.getLetterImage();
            CharacterField area = charactersFields.getFieldThatCollidesWith(limg);
            if (area != null) {
                Point areaCenter = area.getCenterPoint();
                limg.update(areaCenter);
                area.setCharacterInsideField(l.getLetter());
            }
        }

        checkIfInputComplete();
    }

    private void checkIfInputComplete() {
        List<CharacterField> fields = charactersFields.getFields();

        boolean correct = true;
        for(int i = 0, n = fields.size(); i < n; i++) {
            CharacterField f = fields.get(i);
            if(!f.hasCharacterInsideField()) {
                return; //nije complete
            }

            if(!f.getCharacterInsideField().equals(currentWord.charAt(i))) {
                correct = false;
            }
        }

        if(correct) {
            phase = GamePhase.ENDING;
            coins++;
            //TODO: reproducirat glasovnu poruku s porukom tipa "Bravo! Tocan odgovor! Klikni na ekran kako bi presao na sljedecu rijec."
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
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

        charactersFields.draw(canvas);

        for(Letter l: letters) {
            l.getLetterImage().draw(canvas);
        }

        //za provjeru dropa
       // if(!dropArea.collision(letterA)){
        //    dropArea.draw(canvas);
       // }

        if(phase == GamePhase.ENDING) {
            charactersFields.setColor(Color.GREEN);
        }
    }
}
