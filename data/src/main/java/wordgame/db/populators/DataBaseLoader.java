package wordgame.db.populators;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import wordgame.db.database.WordGameDatabase;
import wordgame.db.model.DbCategory;

public class DataBaseLoader {

    WordGameDatabase instance;
    Context context;

    public DataBaseLoader(Context context) {

        this.context = context;
    }

    public void loadCategories() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("categories.txt")));
            while(reader.ready()){
                String line=reader.readLine();
                DbCategory category =new DbCategory(line);

            }
            reader.close();
        } catch (Exception e) {
        }

    }
}
