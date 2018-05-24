package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbImage;
import wordgame.db.model.DbWordImageRelation;

@Dao
public interface WordImageRelationDao {


    @Insert
    public void insert(DbWordImageRelation relation);

    @Query("select images.id,images.path from word_image_relation,images where word_image_relation.imageId==images.id and wordId==(:wordId)")
    public DbImage getImageForWord(int wordId);
}
