package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;
    private GameImage currentImage;
    private String currentWord;
    private CharactersFields charactersFields;
    private StartingHint startingHint;
    private int screenWidth;
    private int screenHeight;
    private long presentingTimeStart;
    //za potrebe demo inacice, slike treba dohvatiti iz baze ?
    private List<LetterImage> listOfLetters;
    private SparseArray<LetterImage> mLetterPointer = new SparseArray<LetterImage>();
    private int coins = 0;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp=wm.getDefaultDisplay();
        Point p=new Point();
        disp.getSize(p);
        screenWidth=p.x;
        screenHeight=p.y;


        phase = GamePhase.PRESENTING_WORD;
        presentingTimeStart=System.currentTimeMillis();
       // phase = GamePhase.TYPING_WORD;

        try {
            initNewWord();
            currentWord=currentWord.toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: pronaći odgovarajući postupak u ovoj situaciji
        }
    }

    private void initNewWord() throws  IOException{
        currentWord = "Motocikl";
        currentImage = loadImage("motocikl.jpg");
        startingHint=new StartingHint(currentWord,this,screenWidth,screenHeight);
        startingHint.setRect(currentImage.getRect());
        //TODO: dodati kod koji određuje (uzimajući u obzir kategoriju/težinu) sljedecu rijec


        //TODO: napraviti pozive metode (i tu metodu) preko koje ce se dohvatiti ime slike za zadanu rijec

        charactersFields = new CharactersFields(currentWord,getContext());

        listOfLetters=getLetters();
    }

    //TODO: povezat s bazom
    private List<LetterImage> getLetters() {
        List<LetterImage> listOfLetters = new ArrayList<>();
        Drawable img;
        LetterImage letter;
        Point center = new Point(150,450);

        //TODO: popraviti da radi automatsko skaliranje

       return createCroatianLetters();
       /* img = getResources().getDrawable(R.drawable.letter_0);
        letter = new LetterImage(center,img);
        listOfLetters.add(letter);

        center=new Point(250,450);
        img = getResources().getDrawable(R.drawable.letter_1);
        letter = new LetterImage(center,img);
        listOfLetters.add(letter);

        center=new Point(350,450);
        img = getResources().getDrawable(R.drawable.letter_2);
        letter = new LetterImage(center,img);
        listOfLetters.add(letter);


        center=new Point(450,450);
        img = getResources().getDrawable(R.drawable.letter_3);
        letter = new LetterImage(center,img);
        listOfLetters.add(letter); */

    }

    private List<LetterImage> createCroatianLetters() {
        List<LetterImage> listOfLetters = new ArrayList<>();
        Drawable img;
        LetterImage letter;
        currentWord=currentWord.toLowerCase();

        for(int i=0,len=currentWord.length();i<len;i++){
            Point center=new Point(100+i*50,450);
            if(i!=len-1 && Util.isCroatianSequence(currentWord.substring(i,i+2))){
                int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.substring(i,i+2)),"drawable",this.getContext().getPackageName());
                img=getResources().getDrawable(id);
                letter=new LetterImage(center,img);
                listOfLetters.add(letter);
                i++;
            }
            else{
                int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.substring(i,i+1)),"drawable", this.getContext().getPackageName());
                img=getResources().getDrawable(id);
                letter=new LetterImage(center,img);
                listOfLetters.add(letter);
            }
        }
        return listOfLetters;
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


        for (LetterImage letter : listOfLetters) {
            DropArea area = charactersFields.getFieldThatCollidesWith(letter);
            if (area != null) {
                letter.setCenter(area.getCenterPoint());//centar drop area
            }
            letter.update();
            //letterA.update(letterAPoint);
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
            coins++;
            //TODO: reproducirat glasovnu poruku s porukom tipa "Bravo! Tocan odgovor! Klikni na ekran kako bi presao na sljedecu rijec."

        }
    }

    private void updateWordPresentation() {
        if(System.currentTimeMillis()-presentingTimeStart>= GameConstants.PRESENTING_TIME){
            phase=GamePhase.TYPING_WORD;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //TODO dodati crtanje objekata zajedničkih objema fazama
      
        canvas.drawColor(Color.WHITE); //zamjena za background

        if(phase == GamePhase.PRESENTING_WORD) {
            //TODO dodati crtanje objekata karakterističnih za PRESENTING_WORD fazu
            canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);
            canvas.drawBitmap(startingHint.getHintBitmap(),null,startingHint.getRect(),null);

            return;
        }
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

        charactersFields.draw(canvas);
        canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);
        
        for (LetterImage letter:listOfLetters){
            letter.draw(canvas);
        }
    }

    private LetterImage getTouchedLetter( int x,  int y) {
        LetterImage touched = null;
        for (LetterImage letter : listOfLetters) {
            if (letter.insideRectangle(x,y)) {
                touched = letter;
                break;
            }
        }
        return touched;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
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
