package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "images")
public class DbImage {
    @PrimaryKey
    public int id;

    public String path;

    public DbImage(int id, String path) {
        this.id = id;
        this.path = path;
    }
}
