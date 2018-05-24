package wordgame.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word_image_relation",
        primaryKeys = {
        "wordId","imageId"
},
        foreignKeys = {
        @ForeignKey(entity = DbWord.class,parentColumns = "id",childColumns = "wordId"),
        @ForeignKey(entity = DbImage.class,parentColumns = "id",childColumns = "imageId")
})
public class DbWordImageRelation {

    public int wordId;
    public int imageId;

    public DbWordImageRelation(int wordId,int imageId){
        this.wordId=wordId;
        this.imageId=imageId;
    }
}
