package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.Set;

@Entity(tableName = "words")
public class DbWord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int word;

    @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
    public int languageId;


}
