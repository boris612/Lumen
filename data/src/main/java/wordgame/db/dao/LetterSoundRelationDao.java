package wordgame.db.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import wordgame.db.model.DbLetterSoundRelation;

@Dao
public interface LetterSoundRelationDao {

    @Insert
    public void insert(DbLetterSoundRelation relation);


}
