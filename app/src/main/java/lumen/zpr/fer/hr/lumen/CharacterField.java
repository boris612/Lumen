package lumen.zpr.fer.hr.lumen;

import android.graphics.Rect;

/**
 * Created by Alen on 14.11.2017..
 */

public class CharacterField extends DropArea {
    private Character characterInsideField;
    private Character correctCharacter;

    public CharacterField(Rect rectangle, int color, Character correctCharacter) {
        super(rectangle, color);
        this.correctCharacter=correctCharacter;
    }

    public void setCharacterInsideField(Character characterInsideField) {
        this.characterInsideField = characterInsideField;
    }

    public Character getCharacterInsideField() {
        return characterInsideField;
    }
    public Character getCorrectCharacter(){return  correctCharacter;}

    public boolean hasCharacterInsideField() {
        return getCharacterInsideField()!=null;
    }

    public void setColor(int color) {
        super.setColor(color);
    }

}
