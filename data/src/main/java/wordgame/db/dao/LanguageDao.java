package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbLanguage;

@Dao
public interface LanguageDao {

    @Insert
    public void insert(DbLanguage language);
}
