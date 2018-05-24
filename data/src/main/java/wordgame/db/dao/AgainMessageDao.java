package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbTryAgainMessage;

@Dao
public interface AgainMessageDao {

    @Insert
    public void insert(DbTryAgainMessage message);

    @Query("select * from try_again_messages where languageId==(:languageId)")
    public List<DbTryAgainMessage> getMessages(int languageId);
}
