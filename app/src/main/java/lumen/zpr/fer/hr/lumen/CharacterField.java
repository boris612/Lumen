package lumen.zpr.fer.hr.lumen;

import android.graphics.Rect;

/**
 * Created by Alen on 14.11.2017..
 */

public class CharacterField extends DropArea {
    private String characterInsideField=null;
    private String correctCharacter;
    private long  timeStartGreen;

    public CharacterField(Rect rectangle, int color, String correctCharacter) {
        super(rectangle, color);
        this.correctCharacter=correctCharacter.toLowerCase();
    }

    public CharacterField(Rect rectangle, int color, Character correctCharacter) {
        this(rectangle,color,Character.toString(correctCharacter));
    }

    public void setCharacterInsideField(String characterInsideField) {
        this.characterInsideField = characterInsideField;
    }

    public void setCharacterInsideField(Character characterInsideField) {
       setCharacterInsideField(Character.toString(characterInsideField));
    }

    public String getCharacterInsideField() {
        return characterInsideField;
    }
    public String getCorrectCharacter(){return  correctCharacter;}

    public boolean hasCharacterInsideField() {
        return getCharacterInsideField()!=null;
    }

    public void setColor(int color) {
        if(timeStartGreen>0 && System.currentTimeMillis()-timeStartGreen<GameLayoutConstants.GREEN_HINT_TIME){
            return;
        }
        timeStartGreen=0;
        super.setColor(color);
    }


    public void setColorWithTime(int color){
        this.timeStartGreen=System.currentTimeMillis();
        super.setColor(color);
    }
}
