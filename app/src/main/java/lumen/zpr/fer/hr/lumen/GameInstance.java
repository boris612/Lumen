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

public class GameInstance {

    private List<Integer> wordIds;
    private DBHelper helper;

    private int solved;

    public GameInstance(Context context, String lang, String cat, int amount) {
        helper = new DBHelper(context);

        if (amount <= 0) {                          //Željeno ponašanje?
            wordIds = new ArrayList<Integer>();
            return;
        }

        wordIds = helper.getWordIds(lang, cat);

        int total = wordIds.size();
        if (amount != 0) {
            Random generator = new Random();

            while (total > amount) {
                int out = generator.nextInt(total);
                wordIds.remove(out);
                total--;
            }
        }

        solved = 0;
    }

    public String getWord() {
        return helper.getWord(wordIds.get(solved));
    }

    public String getPath() {
        return helper.getWordImagePath(wordIds.get(solved));
    }

    public String getWordRecordingPath() {
        return helper.getSoundPath(wordIds.get(solved));
    }

    public List<String> getLettersRecordingPath(String word, String language){
        String [] letters = word.split("");
       // System.out.println("to");
        for (String l:letters)
            System.out.println(l.toUpperCase());
        List<String> paths = new ArrayList<>();


        String letter = letters[1];
        System.out.println(letter.toUpperCase().trim());
        paths.add(helper.getLetterSoundPath("B",language));

     /*   for (String letter:letters){
            System.out.println(letter.toUpperCase());
            paths.add(helper.getLetterSoundPath(letter.toUpperCase(),language));

        }*/
       // paths.add(helper.getLetterSoundPath("A",language));
        return paths;
    }

    public boolean goToNext() {
        solved++;
        return solved < wordIds.size();
    }

}
