package hr.fer.zpr.lumen.database.loader;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import wordgame.db.database.WordGameDatabase;
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

public class DatabaseLoaderImpl implements DatabaseLoader {

    private Context context;

    private List<String> image_extensions= Arrays.asList(".jpeg",".jpg",".png",".bmp",".gif",".webp");

    private WordGameDatabase database;

    public DatabaseLoaderImpl(Context context,WordGameDatabase database){
        this.context=context;
        this.database=database;
    }

     private void loadCategories() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.CATEGORIES_FILE_PATH),"UTF-8"));
            while(reader.ready()){
                String line=reader.readLine().trim();
                if(database.categoryDao().findByValue(line)==null){
                    database.categoryDao().insert(new DbCategory(line));
                }

            }
            reader.close();
        } catch (Exception e) {
        }

    }

    private void loadLanguages() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.LANGUAGES_FILE_PATH),"UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if(database.languageDao().findByValue(line)==null){
                    database.languageDao().insert(new DbLanguage(line));
                }
            }
        }catch(Exception  e){

        }
    }

    private void loadLetters(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.LETTERS_TEXT_FILE),"UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if(database.letterDao().getByValue(line)==null){
                    String path=Constants.LETTER_IMAGES_FOLDER_PATH+line+".png";
                    database.letterDao().insertLetter(new DbLetter(line,path));
                }
            }
        }catch(Exception  e){

        }
    }


    private void loadLetterSounds() {
        List<DbLetter> letters = database.letterDao().getAllLetters();
        List<DbLanguage> languages = database.languageDao().getAllLanguages();
            for (DbLanguage language : languages) {
                for (DbLetter letter : letters) {
                    DbLetterSoundRelation relation = database.letterSoundRelationDao().findByLetterAndLanguage(letter.id, language.id);
                    if (relation == null) {
                        relation=new DbLetterSoundRelation(letter.id,language.id,Constants.LETTER_SOUNDS_FOLDER_PATH+language.value+"/"+letter.value+Constants.SOUND_EXTENSION);
                        database.letterSoundRelationDao().insert(relation);
                    }
                }
            }
        }


        private void loadWords(){
        List<DbLanguage> languages=database.languageDao().getAllLanguages();
        try {
            String[] folders=context.getAssets().list(Constants.WORDS_FOLDER);
            for(String folder:folders){
                Properties properties=new Properties();
                BufferedReader propReader=new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.WORDS_FOLDER+"/"+folder+"/"+Constants.SPELLING_FILE_NAME),"UTF-8"));
                properties.load(propReader);
                for(DbLanguage language:languages){
                    try {
                        String value = properties.get(language.value).toString();
                        DbWord word = new DbWord(value, language.id);
                        DbWord oldWord = database.wordDao().getWordForValueAndLanguage(value, language.id);

                        if (oldWord == null) {
                            long idOfInsertedWOrd = database.wordDao().insert(word);
                            String path = Constants.WORDS_FOLDER + "/" + folder + "/" + language.value + Constants.SOUND_EXTENSION;

                            DbWordSoundRelation relation = new DbWordSoundRelation((int) idOfInsertedWOrd, path, language.id);
                            database.wordSoundRelationDao().insert(relation);
                            String[] files = context.getAssets().list(Constants.WORDS_FOLDER + "/" + folder);
                            String imgpath = null;
                            petlja:
                            for (String file : files) {
                                for (String extension : image_extensions) {
                                    if (file.toLowerCase().endsWith(extension)) {
                                        imgpath = file;
                                        break petlja;
                                    }
                                }
                            }
                            DbImage image;
                            if (imgpath != null) {
                                image = new DbImage(Constants.WORDS_FOLDER + "/" + folder + "/" + imgpath);
                            } else {
                                image = new DbImage(Constants.WORDS_FOLDER + "/" + folder + "/" + folder + ".jpg");
                            }

                            long imageId = database.imageDao().insert(image);
                            DbWordImageRelation imageRelation = new DbWordImageRelation((int) idOfInsertedWOrd, (int) imageId);
                            database.wordImageRelationDao().insert(imageRelation);

                            BufferedReader categoriesReader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.WORDS_FOLDER + "/" + folder + "/" + Constants.CATEGORIES_FILE_NAME), "UTF-8"));
                            while (categoriesReader.ready()) {
                                String category = categoriesReader.readLine();
                                DbCategory dbCategory = database.categoryDao().findByValue(category.trim());
                                database.wordCategoriesRelationDao().insert(new DbWordCategoriesRelation((int) idOfInsertedWOrd, dbCategory.id));
                            }

                        }
                    }catch(Exception e){}
                }

            }
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }
        }

        @Override
        public void load(){
            //TODO:load messages
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.VERSION_FILE),"UTF-8"));
            String version=database.versionDao().getVersion();
            String fileVersion=reader.readLine().trim();
            if(version==null || !version.equals(fileVersion)){
                DbVersion newVersion=new DbVersion(fileVersion);
                database.clearAllTables();
                database.versionDao().insert(newVersion);
                loadLanguages();
                loadCategories();
                loadLetters();
                loadLetterSounds();
                loadWords();
                loadMessages();
            }
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }

        }

        public void loadMessages(){
        try {
            List<DbLanguage> languages = database.languageDao().getAllLanguages();
            for (DbLanguage language : languages) {
                for(String file:context.getAssets().list(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.CORRECT_MESSAGES_FOLDER)){
                    DbCorrectMessage message=new DbCorrectMessage(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.CORRECT_MESSAGES_FOLDER+"/"+file,language.id);
                    database.correctDao().insert(message);
                }

                for(String file:context.getAssets().list(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.INCORRECT_MESSAGES_FOLDER)){
                    DbIncorrectMessage message=new DbIncorrectMessage(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.INCORRECT_MESSAGES_FOLDER+"/"+file,language.id);
                    database.incorrectDao().insert(message);
                }

                for(String file:context.getAssets().list(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.TRY_AGAIN_MESSAGES_FOLDER)){
                    DbTryAgainMessage message=new DbTryAgainMessage(Constants.MESSAGES_FOLDER+language.value+"/"+Constants.TRY_AGAIN_MESSAGES_FOLDER+"/"+file,language.id);
                    database.againMessageDao().insert(message);
                }
            }
        }catch(Exception e){
            Log.d("error",e.getMessage());
        }
        }





}
