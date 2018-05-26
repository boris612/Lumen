package wordgame.db.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "languages")
public class DbLanguage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "language")
    public String value;

    public DbLanguage(String value) {
        this.value = value;
    }
}