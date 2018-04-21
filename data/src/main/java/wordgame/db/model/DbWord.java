package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "words")
public class Word {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int word;

    @ForeignKey(entity = DBLanguage.class, parentColumns = "id", childColumns = "languageId")
    public int languageId;


}
