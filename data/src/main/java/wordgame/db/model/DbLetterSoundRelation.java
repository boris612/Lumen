package wordgame.db.model;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="letter_sound_relation",
primaryKeys = {"letterId","soundId"},
foreignKeys = {
        @ForeignKey(entity=DbLetter.class,parentColumns = "id",childColumns = "letterId"),
        @ForeignKey(entity=DbSound.class,parentColumns = "id",childColumns = "soundId")
})
public class DbLetterSoundRelation {

    public int letterId;

    @Embedded
    public DbLanguage language;


    public int soundId;

    public DbLetterSoundRelation(int letterId, DbLanguage language, int soundId) {
        this.letterId = letterId;
        this.language = language;
        this.soundId = soundId;
    }

}
