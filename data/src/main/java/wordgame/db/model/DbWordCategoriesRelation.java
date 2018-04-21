package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "word_categories_join",
        primaryKeys = {"wordId", "categoryId"},
        foreignKeys = {
                @ForeignKey(entity = Word.class,
                        parentColumns = "id",
                        childColumns = "wordId"),
                @ForeignKey(entity = DBCategory.class,
                        parentColumns = "id",
                        childColumns = "categoryId")
        })
public class WordCategoriesRelation {

    public int wordId;
    public int categoryId;


    public WordCategoriesRelation(int wordId, int categoryId) {
        this.wordId = wordId;
        this.categoryId = categoryId;
    }
}
