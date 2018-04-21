package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_sound_relation",
primaryKeys = {"wordId","soundId"},
foreignKeys = {
        @ForeignKey(entity=DbWord.class, parentColumns = "id",childColumns = "wordId"),
        @ForeignKey(entity=DbSound.class,parentColumns = "id",childColumns = "soundId")
})
public class DbWordSoundRelation {

    public int wordId;
    public int soundId;

    public DbWordSoundRelation(int wordId, int soundId) {
        this.wordId = wordId;
        this.soundId = soundId;
    }
}
