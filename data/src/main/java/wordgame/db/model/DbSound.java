package wordgame.db.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DbSound {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name="path")
    public String pathToSound;
}
