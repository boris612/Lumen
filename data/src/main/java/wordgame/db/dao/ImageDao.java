package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbImage;

@Dao
public interface ImageDao {


    @Insert
    public long insert(DbImage image);
}
