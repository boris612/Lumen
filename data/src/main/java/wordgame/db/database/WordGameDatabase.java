package wordgame.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import wordgame.db.dao.CategoryDao;
import wordgame.db.dao.LanguageDao;
import wordgame.db.dao.LetterSoundRelationDao;
import wordgame.db.dao.SoundDao;
import wordgame.db.dao.WordCategoriesRelationDao;
import wordgame.db.dao.WordDao;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbImage;
import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterSoundRelation;
import wordgame.db.model.DbSound;
import wordgame.db.model.DbWord;
import wordgame.db.model.DbWordCategoriesRelation;
import wordgame.db.model.DbWordSoundRelation;

@Database(entities={DbCategory.class,
        DbWord.class,
        DbSound.class,
        DbLanguage.class,
        DbLetter.class,
        DbWordCategoriesRelation.class,
        DbLetterSoundRelation.class,
        DbWordSoundRelation.class,
        DbImage.class
        },
        version=1,
exportSchema = false)
public abstract class WordGameDatabase extends RoomDatabase {


    public abstract CategoryDao categoryDao();

    public abstract LanguageDao languageDao();

    public abstract LetterSoundRelationDao letterSoundRelationDao();

    public abstract SoundDao soundDao();

    public abstract WordCategoriesRelationDao wordCategoriesRelationDao();

    public abstract WordDao wordDao();


}
