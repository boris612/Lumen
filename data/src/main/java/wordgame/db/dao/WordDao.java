package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbWord;

@Dao
public interface WordDao {

    @Insert
    public long insert(DbWord word);

    @Query("SELECT words.id,words.word,words.languageId FROM words inner join word_categories_join on words.id=word_categories_join.wordId" +
            " inner join categories on categories.id==word_categories_join.categoryId WHERE categories.id in (:categoryIds)" +
            "and lower(words.languageId)=lower(:languageId)")
    public List<DbWord> getWordsForCategories(List<Integer> categoryIds, int languageId);


    @Query("SELECT * FROM words where words.word=:value and words.languageId=:languageId")
    public DbWord getWordForValueAndLanguage(String value, int languageId);

    @Query("select * from words where words.languageId==(:languageId)")
    public List<DbWord> getAllWordsForLanguage(int languageId);
}
