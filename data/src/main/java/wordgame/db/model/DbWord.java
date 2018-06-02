package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "words")
public class DbWord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int imageId;


    public DbWord(int imageId) {
        this.imageId = imageId;
    }


}
