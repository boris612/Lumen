package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbWord;

@Dao
public interface WordDao {

    @Insert
     long insert(DbWord word);


}
