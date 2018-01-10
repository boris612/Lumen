package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;

/**
 * Created by Alen on 10.1.2018..
 */

public class WordGameTutorial extends GameTutorial {
    private Drawable handImage;
    private Rect rectangle;
    private CharacterField field;
    private LetterImage letter;

    public WordGameTutorial(List<LetterImage> letters, CharacterField field, Context context) {
        this.letter = determineLetterToInsert(letters,field);
        this.field = field;
        this.handImage = context.getResources().getDrawable(R.drawable.hand);
        initHandPosition(context.getResources().getDisplayMetrics());
    }

    private void initHandPosition(DisplayMetrics dm) {
        int width = (int)(dm.widthPixels * GameLayoutConstants.HAND_IMAGE_WIDTH_FACTOR);
        int height = (int)(dm.widthPixels * GameLayoutConstants.HAND_IMAGE_HEIGHT_FACTOR);

        int forfX = (int)(width*GameLayoutConstants.HAND_IMAGE_FOREFINGER_X_COORDINATE_FACTOR);
        int forfY = (int) (height*GameLayoutConstants.HAND_IMAGE_FOREFINGER_Y_COORDINATE_FACTOR);
        Point forefingerPosOnImage = new Point(forfX,forfY);

        int x = letter.getCenter().x - forfX;
        int y = letter.getCenter().y - forfY;

        handImage.setBounds(x,y,x+width,y+height);
    }

    private LetterImage determineLetterToInsert(List<LetterImage> letters, CharacterField field) {
        for(LetterImage letter: letters) {
            if(field.getCorrectCharacter().equals(letter.getLetter())) {
                return letter;
            }
        }
        return null;
    }

    @Override
    public void updateTutorialAnimation() {

    }

    @Override
    public void drawTutorial(Canvas canvas) {
        handImage.draw(canvas);
    }
}
