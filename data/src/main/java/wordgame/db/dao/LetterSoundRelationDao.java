package wordgame.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterSoundRelation;

@Dao
public interface LetterSoundRelationDao {

    @Insert
    public void insert(DbLetterSoundRelation relation);

    @Query("select * from letter_sound_relation where letterId==(:letterID) and languageId==(:languageId)")
    public DbLetterSoundRelation findByLetterAndLanguage(int letterID,int  languageId);


}
