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
    public void insert(DbCategory category);

    @Query("select * from categories where categories.category==(:value)")
    public DbCategory findByValue(String value);

    @Query("select categories.category from categories")
    public Single<List<String>> getAllCategories();
}
