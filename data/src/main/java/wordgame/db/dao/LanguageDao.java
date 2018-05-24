package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbLanguage;

@Dao
public interface LanguageDao {

    @Insert
    public void insert(DbLanguage language);

    @Query("select * from languages where languages.language==(:value)")
    public DbLanguage findByValue(String value);

    @Query("select * from languages")
    public List<DbLanguage> getAllLanguages();
}
