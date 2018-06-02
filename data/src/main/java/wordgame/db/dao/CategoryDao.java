package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import wordgame.db.model.DbCategory;

@Dao
public interface CategoryDao {

    @Insert
     void insert(DbCategory category);

    @Query("select * from categories where categories.category==(:value)")
     DbCategory findByValue(String value);

    @Query("select categories.category from categories")
     Single<List<String>> getAllCategories();
}
