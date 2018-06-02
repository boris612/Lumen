package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "word_sound_relation",
        primaryKeys = {"wordLanguageId"},
        foreignKeys = {
                @ForeignKey(entity = DbWordLanguageRelation.class, parentColumns = "id", childColumns = "wordLanguageId"),
        })
public class DbWordSoundRelation {

    public int wordLanguageId;
    public String path;

    public DbWordSoundRelation(int wordLanguageId, String path) {
        this.wordLanguageId = wordLanguageId;
        this.path = path;
    }
}
