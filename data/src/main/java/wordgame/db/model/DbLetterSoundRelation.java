package wordgame.db.model;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="letter_sound_relation",
primaryKeys = {"letterId","languageId"},
foreignKeys = {
        @ForeignKey(entity=DbLetter.class,parentColumns = "id",childColumns = "letterId"),
        @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
})
public class DbLetterSoundRelation {

    public int letterId;

    public int languageId;


    public String soundPath;

    public DbLetterSoundRelation(int letterId, int languageId, String soundPath) {
        this.letterId = letterId;
        this.languageId = languageId;
        this.soundPath = soundPath;
    }

}
