package lumen.zpr.fer.hr.lumen;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lumen.zpr.fer.hr.lumen.database.DBHelper;

import static java.sql.DriverManager.println;


/**
 * Created by Kristijan on 8.11.2017.
 */

public class WordSupply {

    private DBHelper helper;
    private List<Integer> wordIds;
    private String language;

    int current;

    public WordSupply(Context context, String lang, String cat) {
        helper = new DBHelper(context);
        wordIds = helper.getWordIds(lang, cat);
        language = lang;

        Random rand = new Random();
        current = rand.nextInt(wordIds.size());
   }

    public LangDependentString getWord() {
        return new CroatianString(helper.getWord(wordIds.get(current)));
    }

    public String getImagePath() {
        return helper.getWordImagePath(wordIds.get(current));
    }

    public String getWordRecordingPath() {
        return helper.getWordSoundPath(wordIds.get(current));
    }

    public List<String> getLettersRecordingPaths() {
        LangDependentString letters = getWord().toUpperCase();

        List<String> paths = new ArrayList<>();

        for (int i=0;i<letters.length();i++){
            paths.add(helper.getLetterSoundPath(letters.charAt(i).toUpperCase(),language));
        }
        Log.d("PATHS",paths.toString());
        return paths;
    }

    public void goToNext() { //TODO algoritam za odabir
        Random rand = new Random();
        current = rand.nextInt(wordIds.size());
    }

}
