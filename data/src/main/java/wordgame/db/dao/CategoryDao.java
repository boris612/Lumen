package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbCategory;

@Dao
public interface CategoryDao {

    @Insert
    public void insert(DbCategory category);
}
