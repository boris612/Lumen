package lumen.zpr.fer.hr.lumen;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.nfc.NfcEvent;
import android.preference.PreferenceManager;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

import android.view.Display;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    public Thread spelling;

    private CharactersFields charactersFields;
    private List<CharacterField> fields;
    private List<CharacterField> hintFields;
    private CoinComponent coinComponent;
    private Drawable winImage;

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
    private LetterImage letterBeingDragged;
    private CharacterField letterBeingDraggedOutOfField;
    private SharedPreferences pref;

    private boolean moreLetters;
    private boolean greenOnCorrect;
    private Context context;
    public boolean paused = false;
    public boolean terminated=false;

    public GamePanel(Context context) {

        super(context);
        this.context=context;
        getHolder().addCallback(this);
        pref= this.getContext().getSharedPreferences(getResources().getString(R.string.preference_file),Context.MODE_PRIVATE);
        this.updateSettings();
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp=wm.getDefaultDisplay();
        Point p=new Point();
        disp.getSize(p);
        screenWidth=p.x;
        screenHeight=p.y;

        //TODO: promjeniti za finalnu verziju

        supply = new WordSupply(this.getContext(), "hrvatski",pref.getString("category","sve"));

        try {
            initNewWord();
            currentWord = currentWord.toLowerCase();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: pronaći odgovarajući postupak u ovoj situaciji
        }
        int initCoins=pref.getInt(getResources().getString(R.string.coins),0);
        //TODO: maknuti fixani coin
        coinComponent = new CoinComponent(getResources().getDrawable(R.drawable.coin),initCoins,getContext());

        initWinImage();
    }

    private void initWinImage() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int disp_width = dm.widthPixels;
        int disp_height = dm.heightPixels;
        int winImgWidth = (int)(disp_height*GameLayoutConstants.WIN_IMAGE_WIDTH_FACTOR);
        int winImgPosX = (int)(disp_width*GameLayoutConstants.WIN_IMAGE_X_CENTER_COORDINATE_FACTOR - winImgWidth/2);
        int winImgPosY = (int)(disp_height*GameLayoutConstants.WIN_IMAGE_Y_CENTER_COORDINATE_FACTOR - winImgWidth/2);
        winImage = getResources().getDrawable(R.drawable.win_image);
        winImage.setBounds(winImgPosX,winImgPosY,winImgPosX+winImgWidth,winImgPosY+winImgWidth);
    }

    private GameImage loadImage(String imageName) throws  IOException {
        return new GameImage(imageName,getContext());
    }

    private GameSound loadSound(String wordRecordingPath, Collection<String> lettersRecordingPath){
        return new GameSound(getContext(),wordRecordingPath,lettersRecordingPath,this,Thread.currentThread());
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
        int space;
        int startingSpace=getResources().getDisplayMetrics().widthPixels/50;
        if(moreLetters){
            space=getResources().getDisplayMetrics().widthPixels-(GameLayoutConstants.MAX_NUM_OF_LETTERS-1)*width-startingSpace;
            space/=GameLayoutConstants.MAX_NUM_OF_LETTERS;
        }
        else {
            space = getResources().getDisplayMetrics().widthPixels - (currentWord.length()-1) *width-startingSpace;
            space/=(int)(currentWord.length());
        }
        space-=space/10;


        List<LetterImage> list=new ArrayList<>();
        for(int i=0,len=currentWord.length();i<len;i++){
            Point center=new Point(startingSpace+i*(space+width) + GameLayoutConstants.RECT_SPACE_FACTOR, (int) (getResources().getDisplayMetrics().heightPixels*GameLayoutConstants.LETTER_IMAGE_Y_COORDINATE_FACTOR));
            int id=getContext().getResources().getIdentifier(LetterMap.letters.get(currentWord.charAt(i)),"drawable", this.getContext().getPackageName());
            img=getResources().getDrawable(id);
            list.add(new LetterImage(center,img,currentWord.toUpperCase().charAt(i),width,height));
        }

        if(moreLetters){
            while(list.size()<GameLayoutConstants.MAX_NUM_OF_LETTERS){
                String[] values=LetterMap.getRandom();
                Point center=new Point(startingSpace+ list.size()*(space+width) + GameLayoutConstants.RECT_SPACE_FACTOR, (int) (getResources().getDisplayMetrics().heightPixels*GameLayoutConstants.LETTER_IMAGE_Y_COORDINATE_FACTOR));
                int id=getContext().getResources().getIdentifier(values[1],"drawable", this.getContext().getPackageName());
                img=getResources().getDrawable(id);
                list.add(new LetterImage(center,img,values[0],width,height));
            }
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
        if (thread!=null && thread.isInterrupted()){
            thread.setRunning(true);
            return;
        }
        for (int i=0;i<10;i++)  System.err.println("STVORENA NOVA GLAVNA DRETVA................................................");
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
        updateAddingLettersToFields(false);
        for (LetterImage letter : listOfLetters) {
            letter.update();
        }

        if(phase == GamePhase.TYPING_WORD){
            if(hintActive) updateHint();
        }

        if (phase != GamePhase.ENDING) {
            checkIfInputComplete();
        }
    }

    private void updateAddingLettersToFields(boolean actionUpJustOccured) {
        outerLoop:  
        for(CharacterField field: fields) {
            LetterImage letterInside = field.getCharacterInsideField();
          
            for (LetterImage letter : listOfLetters) {
                if(field.collision(letter) && letter==letterBeingDragged && letterBeingDraggedOutOfField!=field){ //and to je onaj koji je beingDragged
                    Point newCenter = field.getCenterPoint();
                    if(letterInside!=null && letterInside!=letter) {
                        if(!actionUpJustOccured) {
                            continue;
                        }
                        if(letterBeingDraggedOutOfField!=null) {
                            letterInside.setCenter(letterBeingDraggedOutOfField.getCenterPoint());
                            letterBeingDraggedOutOfField.setCharacterInsideField(letterInside);
                            letterBeingDraggedOutOfField = null;
                        } else {
                            Point center = letterInside.getCenter();
                            letterInside.setCenter(findAPlaceToKickLetterOut(letterInside,field));
                        }
                    }

                    field.setCharacterInsideField(letter);
                    if(!letter.getCenter().equals(newCenter)) {
                        letter.setCenter(newCenter);
                        if(greenOnCorrect && field.getCorrectCharacter().equals(field.getCharacterInsideField().getLetter())){
                           field.setColorWithTime(Color.GREEN);
                           continue outerLoop;
                        }
                    }
                }
            }
            if(field!=hintField && phase!=GamePhase.ENDING)field.setColor(Color.RED);
        }
    }

    private Point findAPlaceToKickLetterOut(LetterImage letter, CharacterField field) {
        Point center = letter.getCenter();
        center.y = center.y+field.getHeight();
        int direction = 1; //smjer pomaka
        int jumpLength = fields.get(1).getCenterPoint().x - fields.get(0).getCenterPoint().x;
        int leftBorder = 0;
        int rightBorder = getContext().getResources().getDisplayMetrics().widthPixels - field.getWidth();
        int bottomBorder = getContext().getResources().getDisplayMetrics().heightPixels - field.getHeight();

        while(true) {
            boolean hasCollision = false;
            for(LetterImage limg: listOfLetters) {
                if(limg == letter) {
                    continue;
                }
                if(limg.getCenter().equals(center)) {
                    hasCollision = true;
                }
            }
          
            if(!hasCollision) {
                return center;
            }
            center.x += direction*jumpLength;
            if(center.x > rightBorder) {
                center.x = letter.getCenter().x;
                direction=-1;
            }
            if(center.x < leftBorder) {
                if(bottomBorder > center.y + jumpLength) {
                    //ovo se fizicki nebi trebalo moc dogodit jer se onda ni u samom pocetku ne bi
                    //gdje imala sva ta slova smjestit
                    return center;
                }
                center.y += jumpLength;
                direction=1;
            }
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
        if(hintField.hasCharacterInsideField() && hintField.getCharacterInsideField().getLetter().equals(hintField.getCorrectCharacter())){
            hintActive=false;
            hintField.setColor(Color.RED);
            hintField=null;
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
                return;
            }
          
            if(!f.getCharacterInsideField().getLetter().equals(f.getCorrectCharacter())) {
                correct = false;
            }
        }
      
        if(correct) {
            phase = GamePhase.ENDING;

            coinComponent.addCoins(2);
           SharedPreferences.Editor editor= pref.edit();
           editor.putInt(getResources().getString(R.string.coins),coinComponent.getCoins());
           editor.commit();
            endingTime = System.currentTimeMillis();

            //TODO: reproducirat glasovnu poruku s porukom tipa "Bravo! Tocan odgovor! Klikni na ekran kako bi presao na sljedecu rijec."

        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
     //   System.out.println(spelling.getState());
        canvas.drawColor(Color.WHITE); //zamjena za background

        canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);

        coinComponent.draw(canvas);


        if(phase == GamePhase.PRESENTING_WORD) {
            canvas.drawBitmap(currentImage.getBitmap(),null,currentImage.getRect(),null);
            canvas.drawBitmap(startingHint.getHintBitmap(),null,startingHint.getRect(),null);
            return;
        }


        charactersFields.draw(canvas);

        for (LetterImage letter:listOfLetters){
            letter.draw(canvas);
        }

        if(phase == GamePhase.ENDING) {
            charactersFields.setColor(Color.GREEN);
            winImage.draw(canvas);

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
                if(touchedLetter == null) return true;
                touchedLetter.setCenter(new Point(xTouch,yTouch));
                setLetterBeingDragged(touchedLetter);


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
                updateAddingLettersToFields(true);
                setLetterBeingDragged(null);
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

    public void setLetterBeingDragged(LetterImage letterBeingDragged) {
        if(letterBeingDragged == null) {
            letterBeingDraggedOutOfField = null;
            return;
        }
        this.letterBeingDragged = letterBeingDragged;
        
        for(CharacterField field: fields) {
            if(field.getCharacterInsideField()==letterBeingDragged) {
                letterBeingDraggedOutOfField = field;
                field.setCharacterInsideField(null);
                break;
            }
        }
    }

    private void executeCoinHint() {
        if( !hintActive && phase==GamePhase.TYPING_WORD && coinComponent.getCoins()>0) {
            coinComponent.addCoins(-1);
            SharedPreferences.Editor edit=pref.edit();
            edit.putInt(getResources().getString(R.string.coins),coinComponent.getCoins());
            edit.commit();
            int size=hintFields.size();
            for(int i=0;i<size;i++){
                CharacterField c=hintFields.get(i);
                if(c.getCharacterInsideField()==null || !c.getCharacterInsideField().getLetter().equals(c.getCorrectCharacter())){
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


    public void updateSettings(){
        moreLetters=pref.getBoolean(getResources().getString(R.string.add_more_letters),false);
        greenOnCorrect=pref.getBoolean(getResources().getString(R.string.green_on_correct),false);
    }

}