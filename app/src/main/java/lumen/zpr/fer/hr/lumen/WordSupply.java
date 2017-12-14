package lumen.zpr.fer.hr.lumen;


import android.content.Context;

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

    public String getWord() {
        return helper.getWord(wordIds.get(current));
    }

    public String getImagePath() {
        return helper.getWordImagePath(wordIds.get(current));
    }

    public String getWordRecordingPath() {
        return helper.getWordSoundPath(wordIds.get(current));
    }

    public List<String> getLettersRecordingPaths() {
        String [] letters = getWord().split(""); //TODO hrvatska slova

        List<String> paths = new ArrayList<>();

        for (int i=1;i<letters.length;i++){
            paths.add(helper.getLetterSoundPath(letters[i].toUpperCase(),language));
        }

        return paths;
    }

    public void goToNext() { //TODO algoritam za odabir
        Random rand = new Random();
        current = rand.nextInt(wordIds.size());
    }

}
