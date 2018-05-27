package hr.fer.zpr.lumen.ui.wordgame.models;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import javax.inject.Inject;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;
import hr.fer.zpr.lumen.wordgame.model.Word;
import wordgame.db.database.WordGameDatabase;

/**
 * Object which models the starting hint for this word game.
 * It creates a bitmap of the whole word which is to be shown to the player
 * before being let to play.
 * Created by Marko on 7.11.2017..
 */

public class StartingHintModel extends GameDrawable {
    /* word to be created */
    private Word word;
    /*bitmap of final hint */
    private Bitmap hint;
    /* rectangle which defines location and size of drawing */
    private int numOfLetShown;

    private int screenWidth;
    private int screenHeight;

    private Bitmap[] parts;
    private int maxHintHeight;
    private int hintWidth;
    private int currLetterIndex = 0;
    private int currWidth;
    private Canvas cnv;

    private Context context;



    static int SPACE_FROM_BORDER_TO_HINT = 50;
    static double STARTING_HINT_HEIGHT_SCALE_FACTOR = 0.25;
    static int SPACE_FROM_LETTER_TO_LETTER = 10;



    /**
     * Constructor for the hint
     * Creates a new StartingHint object which automatically creates
     * a bitmap of the word
     *
     * @param word   to be created
     */
    public StartingHintModel(Word word, Rect rect,int screenWidth,int screenHeight,Context context) {
        this.word = word;
        this.context=context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        maxHintHeight = screenHeight - rect.top;
        hintWidth = 0;
        numOfLetShown = 1;
        parts = getParts();
        scaleParts();
        rect.top += SPACE_FROM_BORDER_TO_HINT;
        for (int i = 0; i < parts.length; i++) {
            hintWidth += parts[i].getWidth();
        }
        currWidth = SPACE_FROM_BORDER_TO_HINT;
        hint = Bitmap.createBitmap(hintWidth + (parts.length - 1) * SPACE_FROM_LETTER_TO_LETTER + SPACE_FROM_BORDER_TO_HINT * 2, maxHintHeight, Bitmap.Config.ARGB_8888);
        cnv = new Canvas(hint);
        this.image=hint;
        this.rectangle=rect;
    }

    public boolean showNextLetter() {
        if(numOfLetShown>word.letters.size()){
            return false;
        }
        numOfLetShown++;
        buildPicture();
        return true;
    }

    public boolean hasLettersToShow(){
        if(numOfLetShown<=word.letters.size()) return true;
        return false;
    }

    private void scaleParts() {
        int width = (int) ((screenWidth - (parts.length - 1) * SPACE_FROM_LETTER_TO_LETTER) / (parts.length + 1));
        for (int i = 0; i < parts.length; i++) {
            parts[i] = Bitmap.createScaledBitmap(parts[i], width, maxHintHeight, false);
        }
    }

    private void scaleHint() {
        int scaledHeight = (int) (screenHeight * STARTING_HINT_HEIGHT_SCALE_FACTOR);
        int scaledWidth = (int) (screenWidth);
        this.image = Bitmap.createScaledBitmap(hint, scaledWidth, scaledHeight, false);

    }

    private void buildPicture() {

        Paint paint = new Paint();
        cnv.drawBitmap(parts[currLetterIndex], currWidth, 0, paint);
        currWidth += parts[currLetterIndex].getWidth();
        currWidth += SPACE_FROM_LETTER_TO_LETTER;
        currLetterIndex++;
    }

    private Bitmap[] getParts() {
        int size = word.letters.size();
        Bitmap[] parts = new Bitmap[size];
        createBitmap(parts);
        return parts;
    }

    private void createBitmap(Bitmap[] parts) {
        int wordLen = word.letters.size();
        for (int i = 0; i < wordLen; i++) {
            String path=word.letters.get(i).image.path;
            try {
                parts[i] = BitmapFactory.decodeStream(context.getAssets().open(path));
            }catch(Exception e){}
        }
    }


    /**
     * Hint getter
     * returns the bitmap of the hint
     *
     * @return bitmap of hint
     */
    public Bitmap getHintBitmap() {
        return this.hint;
    }

    public void setRect(Rect otherRect) {
        this.rectangle = otherRect;
    }

    public Rect getRect() {
        return rectangle;
    }



    public String getCurrentLetterSoundPath(){
        return word.letters.get(numOfLetShown-1).sound.path;
    }
}

