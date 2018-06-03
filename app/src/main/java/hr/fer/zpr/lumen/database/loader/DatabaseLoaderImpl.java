package hr.fer.zpr.lumen.database.loader;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import hr.fer.zpr.lumen.ui.DebugUtil;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbCorrectMessage;
import wordgame.db.model.DbImage;
import wordgame.db.model.DbIncorrectMessage;
import wordgame.db.model.DbLanguage;
import wordgame.db.model.DbLetter;
import wordgame.db.model.DbLetterLanguageRelation;
import wordgame.db.model.DbLetterSoundRelation;
import wordgame.db.model.DbTryAgainMessage;
import wordgame.db.model.DbVersion;
import wordgame.db.model.DbWord;
import wordgame.db.model.DbWordCategoriesRelation;
import wordgame.db.model.DbWordLanguageRelation;
import wordgame.db.model.DbWordSoundRelation;

public class DatabaseLoaderImpl implements DatabaseLoader {

    private Context context;

    private List<String> image_extensions = Arrays.asList(".jpeg", ".jpg", ".png", ".bmp", ".gif", ".webp");

    private WordGameDatabase database;

    public DatabaseLoaderImpl(Context context, WordGameDatabase database) {
        this.context = context;
        this.database = database;
    }

    private void loadCategories() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.CATEGORIES_FILE_PATH), "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (database.categoryDao().findByValue(line) == null) {
                    database.categoryDao().insert(new DbCategory(line));
                }

            }
            reader.close();
        } catch (Exception e) {
        }

    }

    private void loadLanguages() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.LANGUAGES_FILE_PATH), "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (database.languageDao().findByValue(line) == null) {
                    database.languageDao().insert(new DbLanguage(line));
                }
            }
        } catch (Exception e) {

        }
    }

    private void loadLetters() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.LETTERS_TEXT_FILE), "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (database.letterDao().getByValue(line) == null) {
                    String path = Constants.LETTER_IMAGES_FOLDER_PATH + line + ".png";
                    DbImage image=new DbImage(path);
                    int imageId=(int)database.imageDao().insert(image);
                    database.letterDao().insertLetter(new DbLetter(line, imageId));
                }
            }
            reader.close();
            List<DbLanguage> languages=database.languageDao().getAllLanguages();
            for(DbLanguage language:languages){
                reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.LETTERS_FOLDER+language.value+".txt"), "UTF-8"));
                while(reader.ready()){
                    String letter=reader.readLine();
                    int letterId=database.letterDao().getByValue(letter).id;
                    DbLetterLanguageRelation dblr=new DbLetterLanguageRelation(letterId,language.id);
                    database.letterLanguageDao().insert(dblr);
                }
            }
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
    }


    private void loadLetterSounds() throws IOException {

        List<DbLanguage> languages = database.languageDao().getAllLanguages();
        for (DbLanguage language : languages) {
            List<DbLetter> letters = database.letterDao().getAllLettersForLanguage(language.id);
            for (DbLetter letter : letters) {
                String[] files=context.getAssets().list(Constants.LETTER_SOUNDS_FOLDER_PATH + language.value);
                String path=null;
                for(String s:files){
                    if(s.split("\\.")[0].equals(letter.value)){
                        path=s;
                        break;
                    }
                }
                DbLetterLanguageRelation dblr=database.letterLanguageDao().findByLetterAndLanguage(letter.id,language.id);
                DbLetterSoundRelation relation;
                if(path==null){

                    relation = new DbLetterSoundRelation(dblr.id, Constants.LETTER_SOUNDS_FOLDER_PATH + language.value + "/" + letter.value + Constants.SOUND_EXTENSION);
                }
                else{
                    relation=new DbLetterSoundRelation(dblr.id,Constants.LETTER_SOUNDS_FOLDER_PATH+language.value+"/"+path);
                }
                    database.letterSoundRelationDao().insert(relation);
                }
            }
        }


    private void loadWords() {
        List<DbLanguage> languages = database.languageDao().getAllLanguages();
        try {
            String[] folders = context.getAssets().list(Constants.WORDS_FOLDER);
            for (String folder : folders) {

                String imgPath = null;
                String[] files = context.getAssets().list(Constants.WORDS_FOLDER + "/" + folder);
                loop:
                for (String file : files) {
                    for (String extension : image_extensions) {
                        if (file.toLowerCase().endsWith(extension)) {
                            imgPath = file;
                            break loop;
                        }
                    }
                }
                DbImage image;
                if (imgPath != null) {
                    image = new DbImage(Constants.WORDS_FOLDER + "/" + folder + "/" + imgPath);
                } else {
                    image = new DbImage(Constants.WORDS_FOLDER + "/" + folder + "/" + folder + ".jpg");
                }

                DbWord word = new DbWord((int) database.imageDao().insert(image));
                int wordId = (int) database.wordDao().insert(word);

                BufferedReader categoryReader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.WORDS_FOLDER + "/" + folder + "/" + Constants.CATEGORIES_FILE_NAME), "UTF-8"));
                while (categoryReader.ready()) {
                    String category = categoryReader.readLine();
                    int categoryId = (int) (database.categoryDao().findByValue(category).id);
                    DbWordCategoriesRelation wordCategoriesRelation = new DbWordCategoriesRelation(wordId, categoryId);
                    database.wordCategoriesRelationDao().insert(wordCategoriesRelation);
                }

                Properties properties = new Properties();
                BufferedReader propReader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.WORDS_FOLDER + "/" + folder + "/" + Constants.SPELLING_FILE_NAME), "UTF-8"));
                properties.load(propReader);
                for (DbLanguage language : languages) {
                    String spelling = properties.getProperty(language.value);
                    DbWordLanguageRelation dbWordLanguageRelation = new DbWordLanguageRelation(wordId, language.id, spelling);
                    String path = null;
                    for(String p:files){
                        if(p.contains(language.value)){
                            path=Constants.WORDS_FOLDER+"/"+folder+"/"+p;
                            break;
                        }
                    }
                    if(path!=null) {
                        int dbWordLanguageRelationId = (int) database.wordLanguageRelationDao().insert(dbWordLanguageRelation);
                        DbWordSoundRelation wordSoundRelation = new DbWordSoundRelation(dbWordLanguageRelationId, path);
                        database.wordSoundRelationDao().insert(wordSoundRelation);
                    }
                }


            }
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
    }

    @Override
    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.VERSION_FILE), "UTF-8"));
            String version = database.versionDao().getVersion();
            String fileVersion = reader.readLine().trim();
            if (version == null || !version.equals(fileVersion)) {
                DbVersion newVersion = new DbVersion(fileVersion);
                database.clearAllTables();
                database.versionDao().insert(newVersion);
                loadLanguages();
                loadCategories();
                loadLetters();
                loadLetterSounds();
                loadWords();
                loadMessages();
            }
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }

    }

    public void loadMessages() {
        try {
            List<DbLanguage> languages = database.languageDao().getAllLanguages();
            for (DbLanguage language : languages) {
                for (String file : context.getAssets().list(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.CORRECT_MESSAGES_FOLDER)) {
                    DbCorrectMessage message = new DbCorrectMessage(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.CORRECT_MESSAGES_FOLDER + "/" + file, language.id);
                    database.correctDao().insert(message);
                }

                for (String file : context.getAssets().list(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.INCORRECT_MESSAGES_FOLDER)) {
                    DbIncorrectMessage message = new DbIncorrectMessage(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.INCORRECT_MESSAGES_FOLDER + "/" + file, language.id);
                    database.incorrectDao().insert(message);
                }

                for (String file : context.getAssets().list(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.TRY_AGAIN_MESSAGES_FOLDER)) {
                    DbTryAgainMessage message = new DbTryAgainMessage(Constants.MESSAGES_FOLDER + language.value + "/" + Constants.TRY_AGAIN_MESSAGES_FOLDER + "/" + file, language.id);
                    database.againMessageDao().insert(message);
                }
            }
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }
    }


}
