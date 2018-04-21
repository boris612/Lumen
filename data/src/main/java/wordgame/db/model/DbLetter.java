package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "letters")
public class DbLetter {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String value;

    public String imagePath;

    public DbLetter(String value, String imagePath) {
        this.value = value;
        this.imagePath = imagePath;
    }
}
