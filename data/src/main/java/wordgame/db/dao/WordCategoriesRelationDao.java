package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbWordCategoriesRelation;

@Dao
public interface WordCategoriesRelationDao {

    @Insert
    void insert(DbWordCategoriesRelation wordCategoriesRelation);
}
