package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Point;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.nfc.NfcEvent;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

import android.view.Display;

import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lumen.zpr.fer.hr.lumen.guicomponents.Label;

/**
 * Created by Alen on 6.11.2017..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private GamePhase phase;

    private WordSupply supply;

    private GameImage currentImage;
    private LangDependentString currentWord;
    private GameSound currentSound;
    private Thread spelling;

    private CharactersFields charactersFields;
    private List<CharacterField> fields;
    private List<CharacterField> hintFields;
    private CoinComponent coinComponent;
    private Label winTextLabel;

    private StartingHint startingHint;
    private int screenWidth;
    private int screenHeight;
    private long presentingTimeStart;
    private long endingTime;

    private List<LetterImage> listOfLetters;
    private SparseArray<LetterImage> mLetterPointer = new SparseArray<LetterImage>();

    private boolean hintActive=false;
    private CharacterField hintField;
    private List<LetterImage> hintImageList;
    private long hintStart;



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

        //TODO: promjeniti za finalnu verziju
        supply = new WordSupply(this.getContext(), "hrvatski", "priroda");

        try {
            initNewWord();
            currentWord = currentWord.toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: pronaći odgovarajući postupak u ovoj situaciji
        }
        //TODO: maknuti fixani coin
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
        currentSound = loadSound(null,supply.getLettersRecordingPaths());
        startingHint= new StartingHint(currentWord,this,screenWidth,screenHeight,new Rect(0,currentImage.getRect().bottom,this.screenWidth,getResources().getDisplayMetrics().heightPixels-currentImage.getRect().top));

        phase = GamePhase.PRESENTING_WORD;
        presentingTimeStart=System.currentTimeMillis();
        charactersFields = new CharactersFields(currentWord,getContext());
        listOfLetters=createLetters();
        spelling = new Thread(new Runnable() {
            @Override
            public void run() {
                currentSound.playSpelling();
            }
        });
        currentSound.registerListener(new GameSoundListener() {
            @Override
            public void letterDone() {
                startingHint.showNextLetter();
                Log.d("LETT","show next letter");
            }

            @Override
            public void wholeSpellingDone() {
                phase=GamePhase.TYPING_WORD;
            }
        });
       spelling.start();
       currentSound.setPlaying(true);
        //TODO: uskladiti igru i slovkanje rijeci

        fields=charactersFields.getFields();
        hintFields=new ArrayList<>(fields);

        supply.goToNext();
    }

    private List<LetterImage> createLetters() {
         listOfLetters = new ArrayList<>();
        Drawable img;
        LetterImage letter;
        int width,height;
        width=(int)(charactersFields.getFieldDimension()*GameLayoutConstants.LETTER_IMAGE_SCALE_FACTOR);
        height=(int)(charactersFields.getFieldDimension()*GameLayoutConstants.LETTER_IMAGE_SCALE_FACTOR);
        //currentWord=currentWord.toLowerCase();
        //TODO: shuffle slova (paziti na hrvatska slova) i raspored na ekranu
        //TODO: bolji nacin za generiranje slova (skaliranje!)
        int space=getResources().getDisplayMetrics().widthPixels-currentWord.length()*GameLayoutConstants.DEFAULT_RECT_WIDTH;
        space/=(int)(currentWord.length());
        int spaceStart=getResources().getDisplayMetrics().widthPixels;
        spaceStart-=space*currentWord.length();
        spaceStart-=GameLayoutConstants.DEFAULT_RECT_WIDTH;
        spaceStart/=2;
        List<LetterImage> list=new ArrayList<>();
        for(int i=0,len=currentWord.length();i<len;i++){
            Point center=new Point(spaceStart+i*space + GameLayoutConstants.RECT_SPACE_FACTOR, (int) (getResources().getDisplayMetrics().heightPixels*GameLayoutConstants.LETTER_IMAGE_Y_COORDINATE_FACTOR));
            int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.charAt(i)),"drawable", this.getContext().getPackageName());
            img=getResources().getDrawable(id);
            list.add(new LetterImage(center,img,currentWord.toUpperCase().charAt(i),width,height));
        }
        List<Point> points=new ArrayList<>();
        for(int i=0,size=list.size();i<size;i++){
            points.add(list.get(i).getCenter());
        }
        Random rand=new Random();
        for(int i=0,size=list.size();i<size;i++){
            int index=rand.nextInt(points.size());
            list.get(i).setCenter(points.get(index));
            list.get(i).update();
            points.remove(index);
        }
        return list;
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
                 currentSound.setPlaying(false);
                 thread.join();
                 break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void update() throws InterruptedException {
        if(phase==GamePhase.TYPING_WORD){
            if(hintActive) updateHint();
        }

        Collections.shuffle(fields);
        for(CharacterField field: fields) {
           field.setCharacterInsideField((String)null);
           for (LetterImage letter : listOfLetters) {
               if(field.collision(letter)){
                   Point newCenter =field.getCenterPoint();
                   field.setCharacterInsideField(letter.getLetter());
                   if(!letter.getCenter().equals(newCenter)) {
                       letter.setCenter(newCenter);
                   }
               }

               letter.update();

           }
       }


        if (phase != GamePhase.ENDING) {
            checkIfInputComplete();
        }
    }

    private void updateHint() {
        if(System.currentTimeMillis()-hintStart>500){
            if(hintField.getColor()==Color.RED){
                hintField.setColor(Color.GREEN);
                for(LetterImage img:hintImageList){
                    img.getDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                }

            }
            else{
                hintField.setColor(Color.RED);
                for(LetterImage img:hintImageList){
                    img.getDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                }
            }

            hintStart=System.currentTimeMillis();
        }
        if(hintField.hasCharacterInsideField() && hintField.getCharacterInsideField().equals(hintField.getCorrectCharacter())){
            hintActive=false;
            hintField.setColor(Color.RED);
            for(LetterImage img:hintImageList){
                img.getDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void checkIfInputComplete() {
        boolean correct = true;
        for(int i = 0, n = fields.size(); i < n; i++) {
            CharacterField f = fields.get(i);
            if(!f.hasCharacterInsideField()) {
                return; //nije complete
            }

            Log.d("CHARS",f.getCharacterInsideField()+" : "+f.getCorrectCharacter());
            if(!f.getCharacterInsideField().equals(f.getCorrectCharacter())) {
                correct = false;
            }
        }

        Log.d("CORRECT",Boolean.toString(correct));
        if(correct) {
            phase = GamePhase.ENDING;

            coinComponent.addCoins(1);
            endingTime = System.currentTimeMillis();

            //TODO: reproducirat glasovnu poruku s porukom tipa "Bravo! Tocan odgovor! Klikni na ekran kako bi presao na sljedecu rijec."

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
                if(coinComponent.getRectangle().contains(xTouch,yTouch)){
                    executeCoinHint();
                }


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

    private void executeCoinHint() {
        if( !hintActive && phase==GamePhase.TYPING_WORD && coinComponent.getCoins()>0) {
            coinComponent.addCoins(-1);
            int size=hintFields.size();
            for(int i=0;i<size;i++){
                CharacterField c=hintFields.get(i);
                if(c.getCharacterInsideField()==null || !c.getCharacterInsideField().equals(c.getCorrectCharacter())){
                    hintField=c;
                    break;
                }
            }
            LetterImage image=null;
            size=listOfLetters.size();
            hintImageList=new ArrayList<>();
            for(int i=0;i<size;i++){
                if(listOfLetters.get(i).getLetter().equals(hintField.getCorrectCharacter())){
                    hintImageList.add(listOfLetters.get(i));
                }
            }
            hintActive=true;
            hintStart=System.currentTimeMillis();
        }

    }


}