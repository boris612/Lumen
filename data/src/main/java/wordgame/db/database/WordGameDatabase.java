package wordgame.db.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import wordgame.db.dao.CategoryDao;
import wordgame.db.dao.LanguageDao;
import wordgame.db.dao.LetterSoundRelationDao;
import wordgame.db.dao.SoundDao;
import wordgame.db.dao.WordCategoriesRelationDao;
import wordgame.db.dao.WordDao;
import wordgame.db.model.Category;
import wordgame.db.model.Image;
import wordgame.db.model.Language;
import wordgame.db.model.Letter;
import wordgame.db.model.LetterSoundRelation;
import wordgame.db.model.Sound;
import wordgame.db.model.Word;
import wordgame.db.model.WordCategoriesRelation;
import wordgame.db.model.WordSoundRelation;

@Database(entities={Category.class,Word.class,Sound.class,Language.class,Letter.class},
        version=1,
exportSchema = false)
public abstract class WordGameDatabase extends RoomDatabase {


    private static WordGameDatabase INSTANCE;

    public abstract CategoryDao categoryDao();

    public abstract LanguageDao languageDao();

    public abstract LetterSoundRelationDao letterSoundRelationDao();

    public abstract SoundDao soundDao();

    public abstract WordCategoriesRelationDao wordCategoriesRelationDao();

    public abstract WordDao wordDao();


}
