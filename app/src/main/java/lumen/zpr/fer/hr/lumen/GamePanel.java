package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;
    private GameImage currentImage;
    private String currentWord;
    private CharactersFields charactersFields;

    //za potrebe demo inacice, slike treba dohvatiti iz baze ?
    private List<Letter> listOfLetters;
    private SparseArray<LetterImage> mLetterPointer = new SparseArray<LetterImage>();
    private CoinComponent coinComponent;
    private Label winTextLabel;


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

        coinComponent = new CoinComponent(getResources().getDrawable(R.drawable.coin),0,getContext());

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int display_width = dm.widthPixels;
        int display_height = dm.heightPixels;
        winTextLabel = new Label("TOČNO!",new Point((int)(dm.widthPixels*0.3),(int)(dm.heightPixels*0.4)), Color.GREEN, 300);
    }

    private void initNewWord() throws  IOException{
        currentWord = "ABCD";
        //TODO: dodati kod koji određuje (uzimajući u obzir kategoriju/težinu) sljedecu rijec

        currentImage = loadImage("motocikl.jpg");
        //TODO: napraviti pozive metode (i tu metodu) preko koje ce se dohvatiti ime slike za zadanu rijec

        charactersFields = new CharactersFields(currentWord,getContext());

        listOfLetters=getLetters();
    }

    //TODO: povezat s bazom
    private List<Letter> getLetters() {
        List<Letter> listOfLetters = new ArrayList<>();
        Drawable img;
        LetterImage letterImage;
        Point center = new Point(150,450);

        //TODO: popraviti da radi automatsko skaliranje

        img = getResources().getDrawable(R.drawable.a);
        letterImage = new LetterImage(center,img);
        listOfLetters.add(new Letter('A',letterImage));

        center=new Point(250,450);
        img = getResources().getDrawable(R.drawable.b);
        letterImage = new LetterImage(center,img);
        listOfLetters.add(new Letter('B',letterImage));

        center=new Point(350,450);
        img = getResources().getDrawable(R.drawable.c);
        letterImage = new LetterImage(center,img);
        listOfLetters.add(new Letter('C',letterImage));


        center=new Point(450,450);
        img = getResources().getDrawable(R.drawable.d);
        letterImage = new LetterImage(center,img);
        listOfLetters.add(new Letter('D',letterImage));

        return  listOfLetters;
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

    public void update() {
        if (phase == GamePhase.PRESENTING_WORD) {
            updateWordPresentation();
            return;
        }


        for (Letter letter : listOfLetters) {
            LetterImage letterImage = letter.getLetterImage();
            CharacterField area = charactersFields.getFieldThatCollidesWith(letterImage);
            if (area != null) {
                letterImage.setCenter(area.getCenterPoint());//centar drop area
                area.setCharacterInsideField(letter.getLetter());
            }
            letterImage.update();
            //letterA.update(letterAPoint);

        }

        if(phase!=GamePhase.ENDING) {
            checkIfInputComplete();
        }
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
            coinComponent.addCoins(1);
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

        coinComponent.draw(canvas);

        if(phase == GamePhase.PRESENTING_WORD) {
            //TODO dodati crtanje objekata karakterističnih za PRESENTING_WORD fazu
            return;
        }
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

        charactersFields.draw(canvas);
        
        for (Letter letter:listOfLetters){
            letter.getLetterImage().draw(canvas);
        }

        if(phase == GamePhase.ENDING) {
            charactersFields.setColor(Color.GREEN);
            winTextLabel.draw(canvas);
        }
    }

    private LetterImage getTouchedLetter( int x,  int y) {
        LetterImage touched = null;
        for (Letter letter : listOfLetters) {
            if (letter.getLetterImage().insideRectangle(x,y)) {
                touched = letter.getLetterImage();
                break;
            }
        }
        return touched;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(phase == GamePhase.ENDING) {
            //TODO dodat postupak kojim se igrač prebacuje na novu rijec
            return true;
        }

        boolean handled = false;

        LetterImage touchedLetter;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearLetterPointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some circle
                touchedLetter = getTouchedLetter(xTouch, yTouch);
                if(touchedLetter == null) return true; //ok?
                touchedLetter.setCenter(new Point(xTouch,yTouch));

                mLetterPointer.put(event.getPointerId(0), touchedLetter);

                invalidate(); //???
                handled = true;
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                // It secondary pointers, so obtain their ids and check circles
                pointerId = event.getPointerId(actionIndex);


                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some circle
                touchedLetter = getTouchedLetter(xTouch, yTouch);
                if(touchedLetter == null) return true;

                mLetterPointer.put(pointerId, touchedLetter);
                touchedLetter.setCenter(new Point(xTouch,yTouch));
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_MOVE:
                final int pointerCount = event.getPointerCount();


                for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                    // Some pointer has moved, search it by pointer id
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    touchedLetter = mLetterPointer.get(pointerId);

                    if (null != touchedLetter) {
                        touchedLetter.setCenter(new Point(xTouch,yTouch));
                    }
                }
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_UP:
                clearLetterPointer();
                invalidate();
                handled = true;
                checkIfInputComplete();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // not general pointer was up
                pointerId = event.getPointerId(actionIndex);

                mLetterPointer.remove(pointerId);
                invalidate();
                handled = true;
                break;

            case MotionEvent.ACTION_CANCEL:
                handled = true;
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    public void clearLetterPointer(){
        mLetterPointer.clear();
    }
}
