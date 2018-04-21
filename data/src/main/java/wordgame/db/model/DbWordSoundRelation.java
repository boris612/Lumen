package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_sound_relation",
        primaryKeys = {"wordId", "soundId"},
        foreignKeys = {
                @ForeignKey(entity = Word.class, parentColumns = "id", childColumns = "wordId"),
                @ForeignKey(entity = Sound.class, parentColumns = "id", childColumns = "soundId")
        })
public class WordSoundRelation {

    public int wordId;
    public int soundId;

    public WordSoundRelation(int wordId, int soundId) {
        this.wordId = wordId;
        this.soundId = soundId;
    }
}
