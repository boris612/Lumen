package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_sound_relation",
primaryKeys = {"wordId","languageId"},
foreignKeys = {
        @ForeignKey(entity=DbWord.class, parentColumns = "id",childColumns = "wordId"),
        @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
})
public class DbWordSoundRelation {

    public int wordId;
    public String path;
    public int languageId;

    public DbWordSoundRelation(int wordId,String path,int languageId ) {
        this.wordId = wordId;
        this.path=path;
        this.languageId=languageId;
    }
}
