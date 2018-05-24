package wordgame.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import wordgame.db.model.DbVersion;

@Dao
public interface VersionDao {

    @Query("delete from db_version")
    public void clearTable();

    @Insert
    public void insert(DbVersion version);

    @Query("select  version from db_version limit 1")
    public String getVersion();
}
