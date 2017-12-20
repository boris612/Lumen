package lumen.zpr.fer.hr.lumen;

import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Alen on 14.11.2017..
 */

public class CharacterField extends DropArea {
    private LetterImage characterInsideField=null;
    private String correctCharacter;
    private Rect rectangle;

    public CharacterField(Rect rectangle, int color, String correctCharacter) {
        super(rectangle, color);
        this.rectangle = rectangle;
        this.correctCharacter=correctCharacter.toLowerCase();
    }

    public void setCharacterInsideField(LetterImage characterInsideField) {
        if(this.characterInsideField!=characterInsideField && characterInsideField!=null) {
            Log.d("SETT","POSTAVA SLOVA "+characterInsideField.getLetter()+", A TOČNO JE " + getCorrectCharacter());
        }
        this.characterInsideField = characterInsideField;
    }

    public LetterImage getCharacterInsideField() {
        return characterInsideField;
    }
    public String getCorrectCharacter(){return  correctCharacter;}

    public boolean hasCharacterInsideField() {
        return getCharacterInsideField()!=null;
    }

    public void setColor(int color) {
        super.setColor(color);
    }

    public int getHeight() {
        return rectangle.height();
    }

    public int getWidth() {
        return rectangle.width();
    }
}
