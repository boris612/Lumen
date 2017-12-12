package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI komponenta koja prikazuje prazna polja znakova za odredenu riječ. Sama se zna pozicionirati
 * na displayu igre.
 */

public class CharactersFields {
    private static int COLOR = Color.RED;
    private List<CharacterField> fields;
    private static double FIELD_WIDTH_MAX_FACTOR = GameLayoutConstants.CHAR_FIELD_WIDTH_MAX_FACTOR;
    private static double WHOLE_ARRAY_WIDTH_FACTOR = GameLayoutConstants.CHAR_FIELDS_WIDTH_FACTOR;
    private static double Y_COORDINATE_FACTOR = GameLayoutConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR;
    private static double GAP_WIDTH_TO_FIELD_WIDTH_FACTOR = GameLayoutConstants.CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR;

    public CharactersFields(String word, Context context) {
        fields = new ArrayList<>();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //int fieldWidthHeight = (int)(dm.widthPixels*FIELD_WIDTH_FACTOR);
        int fieldWidthHeight = (int)(dm.widthPixels/(word.length()*(1+GAP_WIDTH_TO_FIELD_WIDTH_FACTOR)) * WHOLE_ARRAY_WIDTH_FACTOR);
        if (fieldWidthHeight > dm.widthPixels*FIELD_WIDTH_MAX_FACTOR) {
            fieldWidthHeight = (int)(dm.widthPixels*FIELD_WIDTH_MAX_FACTOR);
        }

        int gapWidth = (int)(fieldWidthHeight*GAP_WIDTH_TO_FIELD_WIDTH_FACTOR);
        int x = determineStartingX(word.length(),context,fieldWidthHeight,gapWidth);

        for(int i = 0, n = word.length(); i < n; i++) {
            Rect r = new Rect();
            r.left = x+i*(fieldWidthHeight+gapWidth);
            r.top = (int)(dm.heightPixels*Y_COORDINATE_FACTOR);
            r.right = r.left + fieldWidthHeight;
            r.bottom = r.top + fieldWidthHeight;
            fields.add(new CharacterField(r,COLOR, Character.toUpperCase(word.charAt(i))));
        }
    }

    /**
     * Određuje x koordinatu prvog polja.
     * @param wordLength
     * @param context
     * @return
     */
    private int determineStartingX(int wordLength, Context context, int fieldWidthHeight, int gapWidth) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int componentWidth = fieldWidthHeight*wordLength+(wordLength-1)*gapWidth;
        Log.d("COMP WIDTH",Integer.toString(componentWidth));
        return dm.widthPixels/2-componentWidth/2;
    }

    public void draw(Canvas canvas) {
        for(DropArea field: fields) {
            field.draw(canvas);
        }
    }

    public CharacterField getFieldThatCollidesWith(LetterImage letterImage) {
        for(CharacterField field: fields) {
            if(field.collision(letterImage)) {
                /*if(field.getCharacterInsideField().equals(letterImage.getLetter())){
                    field.setColor(CORRECT_COLOR);
                }*/
                return field;
            }
        }
        return null;
    }

    public List<CharacterField> getFields() {
        return fields;
    }

    public void setColor(int color) {
        for(CharacterField f: fields) {
            f.setColor(color);
        }
    }
}
