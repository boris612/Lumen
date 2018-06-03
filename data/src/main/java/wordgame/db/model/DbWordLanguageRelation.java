package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_language_relation",
        primaryKeys = {"wordId","languageId"},
        foreignKeys = {
        @ForeignKey(entity = DbWord.class, childColumns = "wordId", parentColumns = "id"),
        @ForeignKey(entity = DbLanguage.class, childColumns = "languageId", parentColumns = "id")
})
public class DbWordLanguageRelation {

    public int wordId;

    public int languageId;

    public String word;

    public String soundPath;

    public DbWordLanguageRelation(int wordId, int languageId, String word,String soundPath) {
        this.wordId = wordId;
        this.languageId = languageId;
        this.word = word;
        this.soundPath=soundPath;
    }

}
