package coingame.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import coingame.model.DbCoin;

@Dao
public interface CoinDao {

    @Insert
    void insert(DbCoin coin);


    @Query("select * from coins where value==(:value)")
    DbCoin getForValue(int value);
}
