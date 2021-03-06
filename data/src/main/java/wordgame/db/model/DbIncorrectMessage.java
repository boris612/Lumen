package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "incorrect_messages", foreignKeys = @ForeignKey(entity = DbLanguage.class, parentColumns = "id", childColumns = "languageId"))
public class DbIncorrectMessage {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String path;
    @NonNull
    public int languageId;

    public DbIncorrectMessage(String path, int languageId) {
        this.path = path;
        this.languageId = languageId;
    }
}
