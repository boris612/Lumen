package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import wordgame.db.model.DbLetter;

@Dao
public interface LetterDao {

    @Insert
     void insertLetter(DbLetter letter);

    @Query("select * from letters where letters.value==(:letter)")
     DbLetter getByValue(String letter);

    @Query("select letters.* from letters left join letter_language_relation on letters.id=letter_language_relation.letterId where letter_language_relation.languageId==(:languageId)")
    List<DbLetter> getAllLettersForLanguage(int languageId);

    @Query("select * from letters")
     List<DbLetter> getAllLetters();
}
