package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "word_categories_join",
primaryKeys = {"wordId","categoryId"},
foreignKeys = {
        @ForeignKey(entity=DbWord.class,
                    parentColumns = "id",
        childColumns = "wordId"),
        @ForeignKey(entity=DbCategory.class,
        parentColumns = "id",
        childColumns = "categoryId")
})
public class DbWordCategoriesRelation {

    public int wordId;
    public int categoryId;


    public DbWordCategoriesRelation(int wordId, int categoryId) {
        this.wordId = wordId;
        this.categoryId = categoryId;
    }
}
