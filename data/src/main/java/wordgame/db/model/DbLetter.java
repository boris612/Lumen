package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "letters")
public class Letter {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String value;

    public String imagePath;

    public Letter(String value, String imagePath) {
        this.value = value;
        this.imagePath = imagePath;
    }
}
