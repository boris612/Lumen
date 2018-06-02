package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbWordSoundRelation;

@Dao
public interface WordSoundRelationDao {

    @Insert
     void insert(DbWordSoundRelation dbWordSoundRelation);

    @Query("select * from word_sound_relation where wordLanguageId==(:wordLanguageId)")
     DbWordSoundRelation getByWordAndLanguage(int wordLanguageId);


}
