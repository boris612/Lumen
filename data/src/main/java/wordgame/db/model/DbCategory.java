package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class category {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="category")
    public String value;


    public category(String value) {
        this.value = value;
    }
}
