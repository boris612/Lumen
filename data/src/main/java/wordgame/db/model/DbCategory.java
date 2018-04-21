package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories")
public class DbCategory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="category")
    public String value;


    public DbCategory(String value) {
        this.value = value;
    }
}
