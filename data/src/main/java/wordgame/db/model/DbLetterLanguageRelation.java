package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(tableName = "letter_language_relation",
        primaryKeys = {"letterId","languageId"},
        foreignKeys = {
        @ForeignKey(entity = DbLetter.class,parentColumns = "id",childColumns = "letterId"),
        @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
})
public class DbLetterLanguageRelation {


    public int letterId;

    public int languageId;
    @NonNull
    public String soundPath;

    public DbLetterLanguageRelation(int letterId,int languageId,String soundPath){
        this.letterId=letterId;
        this.languageId=languageId;
        this.soundPath=soundPath;
    }
}
