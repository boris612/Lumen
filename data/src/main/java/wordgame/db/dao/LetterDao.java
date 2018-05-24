package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import wordgame.db.model.DbLetter;

@Dao
public interface LetterDao {

    @Insert
    public void insertLetter(DbLetter letter);

    @Query("select * from letters where letters.value==(:letter)")
    public DbLetter getByValue(String letter);

    @Query("select * from letters")
    public List<DbLetter> getAllLetters();
}
