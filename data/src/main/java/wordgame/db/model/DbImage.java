package wordgame.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "images")
public class DbImage {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String path;

    public DbImage(String path) {
        this.path = path;
    }
}
