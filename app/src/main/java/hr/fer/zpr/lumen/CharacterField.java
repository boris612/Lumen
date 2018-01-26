package hr.fer.zpr.lumen;

import android.graphics.Rect;

/**
 * Created by Alen on 14.11.2017..
 */

public class CharacterField extends DropArea {
    private LetterImage characterInsideField=null;
    private String correctCharacter;
    private Rect rectangle;
    private long  timeStartGreen;

    public CharacterField(Rect rectangle, int color, String correctCharacter) {
        super(rectangle, color);
        this.rectangle = rectangle;
        this.correctCharacter=correctCharacter.toLowerCase();
    }

    public void setCharacterInsideField(LetterImage characterInsideField) {
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

    public int getHeight() {
        return rectangle.height();
    }

    public int getWidth() {
        return rectangle.width();
    }
}
