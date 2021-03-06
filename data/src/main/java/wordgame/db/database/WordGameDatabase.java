package wordgame.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import coingame.dao.CoinDao;
import coingame.model.DbCoin;
import wordgame.db.dao.AgainMessageDao;
import wordgame.db.dao.CategoryDao;
import wordgame.db.dao.CorrectMessageDao;
import wordgame.db.dao.ImageDao;
import wordgame.db.dao.IncorrectMessagesDao;
import wordgame.db.dao.LanguageDao;
import wordgame.db.dao.LetterDao;
import wordgame.db.dao.LetterLanguageDao;
import wordgame.db.dao.VersionDao;
import wordgame.db.dao.WordCategoriesRelationDao;
import wordgame.db.dao.WordDao;
import wordgame.db.dao.WordLanguageRelationDao;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbCorrectMessage;
import wordgame.db.model.DbImage;
import wordgame.db.model.DbIncorrectMessage;
import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterLanguageRelation;
import wordgame.db.model.DbTryAgainMessage;
import wordgame.db.model.DbVersion;
import wordgame.db.model.DbWord;
import wordgame.db.model.DbWordCategoriesRelation;
import wordgame.db.model.DbWordLanguageRelation;

@Database(entities = {DbCategory.class,
        DbWord.class,
        DbLanguage.class,
        DbLetter.class,
        DbWordCategoriesRelation.class,
        DbImage.class,
        DbVersion.class,
        DbCorrectMessage.class,
        DbIncorrectMessage.class,
        DbTryAgainMessage.class,
        DbWordLanguageRelation.class,
        DbLetterLanguageRelation.class,
        DbCoin.class
},
        version = 1,
        exportSchema = false)
public abstract class WordGameDatabase extends RoomDatabase {

    public abstract CoinDao coinDao();
    
    public abstract WordLanguageRelationDao wordLanguageRelationDao();

    public abstract LetterDao letterDao();

    public abstract CategoryDao categoryDao();

    public abstract LanguageDao languageDao();

    public abstract WordCategoriesRelationDao wordCategoriesRelationDao();

    public abstract WordDao wordDao();

    public abstract ImageDao imageDao();

    public abstract VersionDao versionDao();

    public abstract CorrectMessageDao correctDao();

    public abstract IncorrectMessagesDao incorrectDao();

    public abstract AgainMessageDao againMessageDao();

    public abstract LetterLanguageDao letterLanguageDao();

}
