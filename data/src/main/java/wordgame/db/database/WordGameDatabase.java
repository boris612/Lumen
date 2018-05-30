package wordgame.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import wordgame.db.dao.AgainMessageDao;
import wordgame.db.dao.CategoryDao;
import wordgame.db.dao.CorrectMessageDao;
import wordgame.db.dao.ImageDao;
import wordgame.db.dao.IncorrectMessagesDao;
import wordgame.db.dao.LanguageDao;
import wordgame.db.dao.LetterDao;
import wordgame.db.dao.LetterSoundRelationDao;
import wordgame.db.dao.VersionDao;
import wordgame.db.dao.WordCategoriesRelationDao;
import wordgame.db.dao.WordDao;
import wordgame.db.dao.WordImageRelationDao;
import wordgame.db.dao.WordSoundRelationDao;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbCorrectMessage;
import wordgame.db.model.DbImage;
import wordgame.db.model.DbIncorrectMessage;
import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterSoundRelation;
import wordgame.db.model.DbTryAgainMessage;
import wordgame.db.model.DbVersion;
import wordgame.db.model.DbWord;
import wordgame.db.model.DbWordCategoriesRelation;
import wordgame.db.model.DbWordImageRelation;
import wordgame.db.model.DbWordSoundRelation;

@Database(entities={DbCategory.class,
        DbWord.class,
        DbLanguage.class,
        DbLetter.class,
        DbWordCategoriesRelation.class,
        DbLetterSoundRelation.class,
        DbWordSoundRelation.class,
        DbImage.class,
        DbWordImageRelation.class,
        DbVersion.class,
        DbCorrectMessage.class,
        DbIncorrectMessage.class,
        DbTryAgainMessage.class,
        },
        version=3,
exportSchema = false)
public abstract class WordGameDatabase extends RoomDatabase {


    private static WordGameDatabase database;


    public abstract LetterDao letterDao();

    public abstract CategoryDao categoryDao();

    public abstract LanguageDao languageDao();

    public abstract LetterSoundRelationDao letterSoundRelationDao();

    public abstract WordSoundRelationDao wordSoundRelationDao();

    public abstract WordCategoriesRelationDao wordCategoriesRelationDao();

    public abstract WordDao wordDao();

    public abstract ImageDao imageDao();

    public abstract WordImageRelationDao wordImageRelationDao();

    public abstract VersionDao versionDao();

    public abstract CorrectMessageDao correctDao();

    public abstract IncorrectMessagesDao incorrectDao();

    public abstract AgainMessageDao againMessageDao();


    public static WordGameDatabase getInstance(Context context){
        if(database==null){
            database= Room.databaseBuilder(context,WordGameDatabase.class, "word_game_database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }

}
