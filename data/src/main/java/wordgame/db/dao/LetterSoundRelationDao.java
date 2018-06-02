package wordgame.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbLetterSoundRelation;

@Dao
public interface LetterSoundRelationDao {

    @Insert
     void insert(DbLetterSoundRelation relation);

    @Query("select * from letter_sound_relation where letterLanguageId==(:letterLanguageId)")
     DbLetterSoundRelation findByLetterAndLanguage(int letterLanguageId);


}
