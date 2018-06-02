package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import wordgame.db.model.DbLetterLanguageRelation;

@Dao
public interface LetterLanguageDao {

    @Insert
    long insert(DbLetterLanguageRelation relation);

    @Query("select * from letter_language_relation where letterId==(:letterId) and languageId==(:languageId)")
     DbLetterLanguageRelation findByLetterAndLanguage(int letterId,int languageId);
}
