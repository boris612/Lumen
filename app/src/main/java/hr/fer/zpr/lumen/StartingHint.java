package hr.fer.zpr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Object which models the starting hint for this word game.
 * It creates a bitmap of the whole word which is to be shown to the player
 * before being let to play.
 * Created by Marko on 7.11.2017..
 */

public class StartingHint {
    /* word to be created */
    private LangDependentString word;
    /*bitmap of final hint */
    private Bitmap hint;
    /* view from which this object is called */
    private View view;
    /* rectangle which defines location and size of drawing */
    private Rect rect;
    private int numOfLetShown;

    private int screenWidth;
    private int screenHeight;

    private Bitmap[] parts;
    private int maxHintHeight;
    private int hintWidth;
    private int currLetterIndex=0;
    private int currWidth;
    private Canvas cnv;
    /**
     * Constructor for the hint
     * Creates a new StartingHint object which automatically creates
     * a bitmap of the word
     * @param word to be created
     * @param view from which method is called
     * @param width of screen
     * @param height of screen
     */
    public StartingHint(LangDependentString word,View view,int width,int height,Rect rect) {
        this.word = word.toLowerCase();
        this.view=view;
        this.screenWidth=width;
        this.rect=rect;
        this.screenHeight=height;
        maxHintHeight=screenHeight-rect.top;
        hintWidth=0;
        numOfLetShown=1;
        parts=getParts();
        scaleParts();
        rect.top+=GameLayoutConstants.SPACE_FROM_BORDER_TO_HINT;
        for(int i=0;i<parts.length;i++){
            hintWidth+=parts[i].getWidth();
        }
        currWidth=GameLayoutConstants.SPACE_FROM_BORDER_TO_HINT;
        hint=Bitmap.createBitmap(hintWidth+(parts.length-1)*GameLayoutConstants.SPACE_FROM_LETTER_TO_LETTER+GameLayoutConstants.SPACE_FROM_BORDER_TO_HINT*2,maxHintHeight,Bitmap.Config.ARGB_8888);
        cnv=new Canvas(hint);
    }

    public void showNextLetter() {
        numOfLetShown++;
        if(numOfLetShown > word.length()) {
            numOfLetShown = word.length();
        }
        buildPicture();
    }

    private void scaleParts(){
        int width=(int)((screenWidth-(parts.length-1)*GameLayoutConstants.SPACE_FROM_LETTER_TO_LETTER)/(parts.length+1));
        for(int i=0;i<parts.length;i++){
            parts[i]=Bitmap.createScaledBitmap(parts[i],width,maxHintHeight,false);
        }
    }

    private void scaleHint() {
        int scaledHeight=(int)(screenHeight*GameLayoutConstants.STARTING_HINT_HEIGHT_SCALE_FACTOR);
        int scaledWidth= (int) (screenWidth);
        hint=Bitmap.createScaledBitmap(hint,scaledWidth,scaledHeight,false);

    }

    private void buildPicture() {

        Paint paint=new Paint();
        cnv.drawBitmap(parts[currLetterIndex], currWidth, 0, paint);
        currWidth+=parts[currLetterIndex].getWidth();
        currWidth+=GameLayoutConstants.SPACE_FROM_LETTER_TO_LETTER;
        currLetterIndex++;
    }

    private Bitmap[] getParts() {
        int size = word.length();
        Bitmap[] parts = new Bitmap[size];
        Context context=view.getContext();
        createBitmap(parts,context);
        return parts;
    }

    private void createBitmap(Bitmap[] parts,Context context) {
        int wordLen = word.length();
        for (int i = 0; i < wordLen; i++) {
            String letter = word.charAt(i);
            int id=context.getResources().getIdentifier(LetterMap.letters.get(word.charAt(i)),"drawable",context.getPackageName());
            parts[i] = BitmapFactory.decodeResource(context.getResources(),id);
        }
    }



    /**
     * Hint getter
     * returns the bitmap of the hint
     * @return bitmap of hint
     */
    public Bitmap getHintBitmap(){
        return this.hint;
    }

    public void setRect(Rect otherRect){
        this.rect=otherRect;
    }

    public Rect getRect(){
        return rect;
    }

}

