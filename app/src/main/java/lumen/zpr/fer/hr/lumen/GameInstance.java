package lumen.zpr.fer.hr.lumen;


import android.content.Context;

import java.util.List;
import java.util.Random;

import lumen.zpr.fer.hr.lumen.database.DBHelper;


/**
 * Created by Kristijan on 8.11.2017.
 */

public class GameInstance {

    private List<Integer> wordIds;
    private DBHelper helper;

    private int solved;

    public GameInstance(Context context, String lang, String cat, int amount) {
        helper = new DBHelper(context);

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
        return helper.getWordPath(wordIds.get(solved));
    }

    public boolean goToNext() {
        solved++;
        return solved < wordIds.size();
    }

}
