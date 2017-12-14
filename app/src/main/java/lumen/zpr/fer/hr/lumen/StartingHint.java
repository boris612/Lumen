package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.IOException;

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
    /**
     * Constructor for the hint
     * Creates a new StartingHint object which automatically creates
     * a bitmap of the word
     * @param word to be created
     * @param view from which method is called
     * @param width of screen
     * @param height of screen
     */
    public StartingHint(LangDependentString word,View view,int width,int height) {
        this.word = word.toLowerCase();
        this.view=view;
        this.screenWidth=width;
        this.screenHeight=height;
        numOfLetShown=1;
        buildPicture();
        scaleHint();
    }

    public void showNextLetter() {
        numOfLetShown++;
        if(numOfLetShown > word.length()) {
            numOfLetShown = word.length();
        }
        buildPicture();
        scaleHint();
    }

    private void scaleHint() {
        int scaledHeight=(int)(screenHeight*GameLayoutConstants.STARTING_HINT_HEIGHT_SCALE_FACTOR);
        int scaledWidth= (int) (screenWidth);
        hint=Bitmap.createScaledBitmap(hint,scaledWidth,scaledHeight,false);

    }

    private void buildPicture() {
        Bitmap[] parts = getParts();
        int maxh=0;
        int width=0;
        for(int i=0;i<parts.length;i++){
            if(parts[i].getHeight()>maxh){
                maxh=parts[i].getHeight();
            }
            width+=parts[i].getWidth();
        }
        width+=parts.length*100;

        hint=Bitmap.createBitmap(width,maxh,Bitmap.Config.ARGB_8888);
        Canvas cnv=new Canvas(hint);
        Paint paint=new Paint();
        int sumw=0;
        for(int i=0;i<parts.length;i++){
            if(i < numOfLetShown) {
                cnv.drawBitmap(parts[i], sumw, 0, paint);
            }
            sumw+=parts[i].getWidth();
            sumw+=100;
        }
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
        this.rect=new Rect();
        rect.top=otherRect.bottom+30;
        rect.left=(this.screenWidth-hint.getWidth())/2;
        rect.bottom=rect.top+hint.getHeight();
        rect.right=rect.left+hint.getWidth();
    }

    public Rect getRect(){
        return rect;
    }

}

