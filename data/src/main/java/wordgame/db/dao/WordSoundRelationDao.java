package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbWordSoundRelation;

@Dao
public interface WordSoundRelationDao {

    @Insert
    public void insert(DbWordSoundRelation dbWordSoundRelation);

    @Query("select  word_sound_relation.wordId,word_sound_relation.path,word_sound_relation.languageId" +
            " from word_sound_relation where word_sound_relation.wordId==(:wordId) and word_sound_relation.languageId==(:languageId)")
    public DbWordSoundRelation getByWordAndLanguage(int wordId,int languageId);
}
