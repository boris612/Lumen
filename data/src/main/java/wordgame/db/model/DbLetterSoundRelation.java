package wordgame.db.model;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "letter_sound_relation",
        primaryKeys = {"letterId", "soundId"},
        foreignKeys = {
                @ForeignKey(entity = DBLetter.class, parentColumns = "id", childColumns = "letterId"),
                @ForeignKey(entity = Sound.class, parentColumns = "id", childColumns = "soundId")
        })
public class LetterSoundRelation {

    public int letterId;

    @Embedded
    public DBLanguage DBLanguage;


    public int soundId;

    public LetterSoundRelation(int letterId, DBLanguage DBLanguage, int soundId) {
        this.letterId = letterId;
        this.DBLanguage = DBLanguage;
        this.soundId = soundId;
    }

}
