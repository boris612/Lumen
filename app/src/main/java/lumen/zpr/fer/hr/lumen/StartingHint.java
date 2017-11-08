package lumen.zpr.fer.hr.lumen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.io.IOException;

/**
 * Created by Marko on 7.11.2017..
 */

public class StartingHint {

    private String word;
    private Bitmap hint;
    private View view;

    public StartingHint(String word,View view) {
        this.word = word.toLowerCase();
        this.view=view;
        buildPicture();
    }

    private void buildPicture() {
        Bitmap[] parts = getParts();
        int maxh=0;
        int width=0;
        for(int i=0;i<parts.length;i++){
            if(parts[i].getHeight()>maxh){
                maxh=parts[i].getHeight();
                width+=parts[i].getWidth();
            }
        }

        hint=Bitmap.createBitmap(width,maxh,Bitmap.Config.ARGB_8888);
        Canvas cnv=new Canvas(hint);
        Paint paint=new Paint();
        int sumw=0;
        for(int i=0;i<parts.length;i++){
            cnv.drawBitmap(parts[i],sumw,0,paint);
            sumw+=parts[i].getWidth();
        }
    }


    private Bitmap[] getParts() {
        int size = word.length();
        if (word.toLowerCase().contains("lj")) size--;
        if (word.toLowerCase().contains("nj")) size--;
        if (word.toLowerCase().contains("dž")) size--;
        Bitmap[] parts = new Bitmap[size];

        int wordLen=word.length();
        for (int i = 0; i < wordLen; i++) {
            if(i!=(wordLen-1) && isSpecialSequence(word.substring(i,i+2))){
                try {
                    parts[i] = BitmapFactory.decodeStream(view.getResources().getAssets().open("letters/"+word.substring(i, i + 2).toUpperCase()+".png"));
                    i++;
                }catch(IOException exc){
                    System.out.println("File not found!");
                }
            }
            else{
                try {
                    parts[i]=BitmapFactory.decodeStream(view.getResources().getAssets().open("letters/"+word.substring(i,i+1).toUpperCase()+".png"));
                } catch (IOException e) {
                    System.out.println("File not found!");
                }
            }
        }
    return parts;
    }
    private boolean isSpecialSequence(String sequence) {
        if(sequence.toLowerCase().contains("lj") ||sequence.toLowerCase().contains("dž") ||sequence.toLowerCase().contains("nj")){
            return true;
        }
        return false;
    }

    public Bitmap getHintBitmap(){
        return this.hint;
    }
}

