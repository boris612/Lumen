package lumen.zpr.fer.hr.lumen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private List<Rect> fields;
    private static double FIELD_WIDTH_FACTOR = GameLayoutConstants.CHAR_FIELD_WIDTH_FACTOR;
    private static double GAP_BETWEEN_FIELDS_FACTOR = GameLayoutConstants.GAP_BETWEEN_CHAR_FIELDS_FACTOR;
    private static double Y_COORDINATE_FACTOR = GameLayoutConstants.CHAR_FIELDS_Y_COORDINATE_FACTOR;

    public CharactersFields(String word, Context context) {
        fields = new ArrayList<>();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int fieldWidthHeight = (int)(dm.widthPixels*FIELD_WIDTH_FACTOR);
        int gapWidth = (int)(dm.widthPixels*GAP_BETWEEN_FIELDS_FACTOR);
        int x = determineStartingX(word.length(),context);

        for(int i = 0, n = word.length(); i < n; i++) {
            Rect r = new Rect();
            r.left = x+i*(fieldWidthHeight+gapWidth);
            r.top = (int)(dm.heightPixels*Y_COORDINATE_FACTOR);
            r.right = r.left + fieldWidthHeight;
            r.bottom = r.top + fieldWidthHeight;
            fields.add(r);
        }
    }

    /**
     * Određuje X koordinatu prvog polja.
     * @param wordLength
     * @param context
     * @return
     */
    private int determineStartingX(int wordLength, Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int componentWidth = (int)((wordLength*FIELD_WIDTH_FACTOR+(wordLength-1)*GAP_BETWEEN_FIELDS_FACTOR)*dm.widthPixels);
        Log.d("COMP WIDTH",Integer.toString(componentWidth));
        return dm.widthPixels/2-componentWidth/2;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        for(Rect field: fields) {
            canvas.drawRect(field, paint);
        }
    }
}
