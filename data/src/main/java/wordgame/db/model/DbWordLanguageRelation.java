package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_language_relation", foreignKeys = {
        @ForeignKey(entity = DbWord.class, childColumns = "wordId", parentColumns = "id"),
        @ForeignKey(entity = DbLanguage.class, childColumns = "languageId", parentColumns = "id")
})
public class DbWordLanguageRelation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int wordId;

    public int languageId;

    public String word;

    public DbWordLanguageRelation(int wordId, int languageId, String word) {
        this.wordId = wordId;
        this.languageId = languageId;
        this.word = word;
    }

}
