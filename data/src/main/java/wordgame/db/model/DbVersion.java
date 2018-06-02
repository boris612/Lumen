package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "db_version")
public class DbVersion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String version;

    public DbVersion(String version) {
        this.version = version;
    }
}
