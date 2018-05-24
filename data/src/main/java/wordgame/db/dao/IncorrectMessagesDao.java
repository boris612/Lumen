package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbIncorrectMessage;

@Dao
public interface IncorrectMessagesDao {

    @Insert
    public void insert(DbIncorrectMessage message);

    @Query("select * from incorrect_messages where languageId==(:languageId)")
    public List<DbIncorrectMessage> getMessages(int languageId);
}
