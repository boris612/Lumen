package hr.fer.zpr.lumen.ui.wordgame.models;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import hr.fer.zpr.lumen.ui.viewmodels.GameDrawable;

public class Letter extends GameDrawable {
    private String value;

    public Letter(String value,Drawable image,Rect bounds){
        super(image,bounds);
        this.value=value;
    }

    public void move(MotionEvent event){
        rectangle.left=(int)(event.getX())-width/2;
        rectangle.top=(int)(event.getY())-height/2;
        rectangle.right=rectangle.left+width;
        rectangle.bottom=rectangle.top+height;
    }

    public String getValue(){
        return value;
    }
}
