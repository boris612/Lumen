package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "letter_language_relation",foreignKeys = {
        @ForeignKey(entity = DbLetter.class,parentColumns = "id",childColumns = "letterId"),
        @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
},
indices = @Index(name="dblr",value={"id","languageId","letterId"}))
public class DbLetterLanguageRelation {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int letterId;

    public int languageId;

    public DbLetterLanguageRelation(int letterId,int languageId){
        this.letterId=letterId;
        this.languageId=languageId;
    }
}
