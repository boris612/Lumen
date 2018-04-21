package hr.fer.zpr.lumen.wordgame.wordbuilder;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zpr.lumen.wordgame.model.Letter;

public class CroatianWordSplitter implements WordSplitter {

    @Override
    public List<Letter> split(String word) {
        List<Letter> letters=new ArrayList<>();

        for(int i=0,len=word.length();i<len;i++){
            if(i!=len-1 && isSpecialSequence(word.substring(i,i+2)) ){
                letters.add(new Letter(word.substring(i,i+2)));
                i++;
            }
            else{
                letters.add(new Letter(word.substring(i,i+1)));
            }
        }
        return letters;
    }

    private static boolean isSpecialSequence(String s){
        s=s.toLowerCase();
        if(s.equals("nj") || s.equals("lj") || s.equals("dž")) return true;
        return false;
    }
}
