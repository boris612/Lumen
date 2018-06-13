package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "try_again_messages",
        foreignKeys = @ForeignKey(entity = DbLanguage.class, parentColumns = "id", childColumns = "languageId"),
indices=@Index(name = "languageId",value={"id","languageId"}))
public class DbTryAgainMessage {

    @PrimaryKey(autoGenerate=true)
    public int id;
    @NonNull
    public String path;

    public int languageId;

    public DbTryAgainMessage(String path, int languageId) {
        this.path = path;
        this.languageId = languageId;
    }
}
