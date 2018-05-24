package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbCategory;
import wordgame.db.model.DbWordCategoriesRelation;

@Dao
public interface WordCategoriesRelationDao {

    @Insert
    void insert(DbWordCategoriesRelation wordCategoriesRelation);

    @Query("select categories.id,categories.category from categories join word_categories_join on categories.id==word_categories_join.categoryId where wordId==(:wordId)")
    List<DbCategory> getCategoriesForWord(int wordId);
}
