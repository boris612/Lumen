package lumen.zpr.fer.hr.lumen;

import android.graphics.Rect;

/**
 * Created by Alen on 14.11.2017..
 */

public class CharacterField extends DropArea {
    private Character characterInsideField;

    public CharacterField(Rect rectangle, int color) {
        super(rectangle, color);
    }

    public void setCharacterInsideField(Character characterInsideField) {
        this.characterInsideField = characterInsideField;
    }

    public Character getCharacterInsideField() {
        return characterInsideField;
    }

    public boolean hasCharacterInsideField() {
        return getCharacterInsideField()!=null;
    }

    public void setColor(int color) {
        super.setColor(color);
    }
}
