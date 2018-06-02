package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbCorrectMessage;

@Dao
public interface CorrectMessageDao {

    @Insert
    void insert(DbCorrectMessage message);

    @Query("select * from correct_messages where languageId==(:languageId)")
    List<DbCorrectMessage> getMessages(int languageId);
}
