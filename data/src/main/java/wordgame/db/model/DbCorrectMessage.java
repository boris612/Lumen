package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "correct_messages",
        foreignKeys = @ForeignKey(entity = DbLanguage.class, parentColumns = "id", childColumns = "languageId"),
indices = @Index(name = "id",value = {"id","languageId"}))
public class DbCorrectMessage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String path;

    public int languageId;

    public DbCorrectMessage(String path, int languageId) {
        this.path = path;
        this.languageId = languageId;
    }
}
