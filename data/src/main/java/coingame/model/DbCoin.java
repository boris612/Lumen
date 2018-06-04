package coingame.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "coins")
public class DbCoin {

    @PrimaryKey(autoGenerate=true)
    public int id;

    public int value;

    public String imagePath;

    public DbCoin(int value,String imagePath){
        this.value=value;
        this.imagePath=imagePath;
    }


}
