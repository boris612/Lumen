package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "letter_sound_relation",
        primaryKeys = {"letterLanguageId"},
        foreignKeys = {
                @ForeignKey(entity = DbLetterLanguageRelation.class, parentColumns = "id", childColumns = "letterLanguageId")
        })
public class DbLetterSoundRelation {

    public int letterLanguageId;

    public String soundPath;

    public DbLetterSoundRelation(int letterLanguageId, String soundPath) {
        this.letterLanguageId=letterLanguageId;
        this.soundPath = soundPath;
    }

}
