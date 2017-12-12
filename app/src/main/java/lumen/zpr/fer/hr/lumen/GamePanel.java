package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import android.graphics.drawable.Drawable;
import android.nfc.NfcEvent;
import android.util.DisplayMetrics;
import android.util.SparseArray;

import android.view.Display;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;

    private WordSupply supply;

    private GameImage currentImage;
    private String currentWord;
    private GameSound currentSound;
    private Thread spelling;
    private CharactersFields charactersFields;

    private CoinComponent coinComponent;
    private Label winTextLabel;

    private StartingHint startingHint;
    private int screenWidth;
    private int screenHeight;
    private long presentingTimeStart;
    private long endingTime;

    private List<LetterImage> listOfLetters;
    private SparseArray<LetterImage> mLetterPointer = new SparseArray<LetterImage>();


    public GamePanel(Context context, int initCoins) {
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

        supply = new WordSupply(this.getContext(), "hrvatski", "priroda");

        try {
            initNewWord();
            currentWord=currentWord.toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: pronaći odgovarajući postupak u ovoj situaciji
        }

        coinComponent = new CoinComponent(getResources().getDrawable(R.drawable.coin),initCoins,getContext());

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int disp_width = dm.widthPixels;
        int disp_height = dm.heightPixels;
        int winTLPosX = (int)(disp_width*GameLayoutConstants.WIN_TEXT_LABEL_X_CENTER_COORDINATE_FACTOR);
        int winTLPosY = (int)(disp_height*GameLayoutConstants.WIN_TEXT_LABEL_Y_CENTER_COORDINATE_FACTOR);
        Point winTLPosition = new Point(winTLPosX,winTLPosY);
        winTextLabel = new Label("TOČNO!",winTLPosition, Color.GREEN, GameLayoutConstants.WIN_TEXT_LABEL_SIZE);
    }




    private GameImage loadImage(String imageName) throws  IOException {
        return new GameImage(imageName,getContext());
    }

    private GameSound loadSound(String wordRecordingPath, Collection<String> lettersRecordingPath){
        return new GameSound(getContext(),wordRecordingPath,lettersRecordingPath);
    }

    private enum GamePhase {
        //faza u kojoj igra prikazuje sliku, slovka i ispisuje riječ
        PRESENTING_WORD,
        //faza u kojoj igrač piše (drag and dropanjem slova) riječ
        TYPING_WORD,
        //faza u kojem se igraču ispisuje da je točno poslozio slova
        ENDING

    }

    private void initNewWord() throws  IOException{
        currentWord = supply.getWord();
        currentImage = loadImage(supply.getImagePath());
        currentSound = loadSound(supply.getWordRecordingPath(), supply.getLettersRecordingPaths());

        startingHint= new StartingHint(currentWord,this,screenWidth,screenHeight);
        startingHint.setRect(currentImage.getRect());

        phase = GamePhase.PRESENTING_WORD;
        presentingTimeStart=System.currentTimeMillis();

        spelling = new Thread(new Runnable() {
            @Override
            public void run() {
                currentSound.playSpelling();
            }
        });
        spelling.start();
        //TODO: uskladiti igru i slovkanje rijeci

        charactersFields = new CharactersFields(currentWord,getContext());
        listOfLetters=createCroatianLetters();

        supply.goToNext();
    }

    private List<LetterImage> createCroatianLetters() {
        List<LetterImage> listOfLetters = new ArrayList<>();
        Drawable img;
        LetterImage letter;
        currentWord=currentWord.toLowerCase();

        //TODO: shuffle slova (paziti na hrvatska slova) i raspored na ekranu
        //TODO: bolji nacin za generiranje slova (skaliranje!)

        for(int i=0,len=currentWord.length();i<len;i++){
            Point center=new Point(100+i*GameLayoutConstants.DEFAULT_RECT_WIDTH + GameLayoutConstants.RECT_SPACE_FACTOR,850);
            if(i!=len-1 && Util.isCroatianSequence(currentWord.substring(i,i+2))){
                int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.substring(i,i+2)),"drawable",this.getContext().getPackageName());
                img=getResources().getDrawable(id);
                listOfLetters.add(new LetterImage(center,img,currentWord.toUpperCase().charAt(i))); //TODO: prilagoditi za hrvatska slova
                i++;
            }
            else{
                int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.substring(i,i+1)),"drawable", this.getContext().getPackageName());
                img=getResources().getDrawable(id);
                listOfLetters.add(new LetterImage(center,img,currentWord.toUpperCase().charAt(i)));
            }
        }

        return listOfLetters;
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
            CharacterField area = charactersFields.getFieldThatCollidesWith(letter);
            if (area != null) {
                letter.setCenter(area.getCenterPoint());//centar drop area
                area.setCharacterInsideField(letter.getLetter());
                if(area.getCorrectCharacter().equals(letter.getLetter())){
                    area.setColor(Color.GREEN);
                }
            }


            letter.update();

            if(phase!=GamePhase.ENDING) {
                checkIfInputComplete();
            }
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

            if(!f.getCharacterInsideField().equals(Character.toUpperCase(currentWord.charAt(i)))) {
                correct = false;
            }
        }

        if(correct) {
            phase = GamePhase.ENDING;

            coinComponent.addCoins(1);
            endingTime = System.currentTimeMillis();

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

        canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);

        coinComponent.draw(canvas);


        if(phase == GamePhase.PRESENTING_WORD) {
            //TODO dodati crtanje objekata karakterističnih za PRESENTING_WORD fazu
            canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);
            canvas.drawBitmap(startingHint.getHintBitmap(),null,startingHint.getRect(),null);

            return;
        }
        //TODO dodati crtanje objekata karakterističnih za TYPING_WORD fazu

        charactersFields.draw(canvas);

        for (LetterImage letter:listOfLetters){
            letter.draw(canvas);
        }

        if(phase == GamePhase.ENDING) {
            charactersFields.setColor(Color.GREEN);
            winTextLabel.draw(canvas);

            if(System.currentTimeMillis()-endingTime>=GameConstants.ENDING_TIME) {
                try{
                initNewWord();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private LetterImage getTouchedLetter( int x,  int y) {
        for (LetterImage letter : listOfLetters) {
            if (letter.insideRectangle(x,y)) {
                return letter;
            }
        }
        return null;
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


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // it's the first pointer, so clear all existing pointers data
                clearLetterPointer();

                xTouch = (int) event.getX(0);
                yTouch = (int) event.getY(0);

                // check if we've touched inside some rect
                touchedLetter = getTouchedLetter(xTouch, yTouch);
                if(touchedLetter == null) return true; //ok?
                touchedLetter.setCenter(new Point(xTouch,yTouch));

                mLetterPointer.put(event.getPointerId(0), touchedLetter);

                invalidate(); //???
                handled = true;
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                // It secondary pointers, so obtain their ids and check rects
                pointerId = event.getPointerId(actionIndex);


                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                // check if we've touched inside some rect
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