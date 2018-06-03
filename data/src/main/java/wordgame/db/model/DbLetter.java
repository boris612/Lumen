package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "letters",foreignKeys =
@ForeignKey(entity = DbImage.class,parentColumns = "id",childColumns = "imageId"))
public class DbLetter {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String value;

    public int imageId;

    public DbLetter(String value, int imageId) {
        this.value = value;
        this.imageId=imageId;
    }
}
