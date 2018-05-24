package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "images")
public class DbImage {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String path;

    public DbImage(String path) {
        this.path = path;
    }
}
