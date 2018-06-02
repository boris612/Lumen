package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbImage;

@Dao
public interface ImageDao {


    @Insert
     long insert(DbImage image);

    @Query("select images.id" +
            ",images.path from words left join images on words.imageId==images.id where words.id==(:wordId) ")
     DbImage getImageForWord(int wordId);
}
