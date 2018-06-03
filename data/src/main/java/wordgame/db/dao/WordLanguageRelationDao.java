package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbWordLanguageRelation;

@Dao
public interface WordLanguageRelationDao {

    @Insert
     void insert(DbWordLanguageRelation relation);

    @Query("select * from word_language_relation where languageId==(:languageId) ")
     List<DbWordLanguageRelation> getAllWordsForLanguage(int languageId);

    @Query("select distinct word_language_relation.*" +
            " from word_language_relation left join words on word_language_relation.wordId==words.id" +
            " left join word_categories_join on words.id==word_categories_join.wordId where word_categories_join.categoryId in (:dbCategoryIds) and languageId==(:languageId)")
     List<DbWordLanguageRelation> getWordsForCategories(List<Integer> dbCategoryIds, int languageId);


    @Query("select * from word_language_relation where wordId==(:wordId) and languageId==(:languageId)")
    DbWordLanguageRelation getByWordAndLanguage(int wordId, int languageId);
}
