package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbSound;

@Dao
public interface SoundDao {

    @Insert
    public void insert(DbSound sound);

}
