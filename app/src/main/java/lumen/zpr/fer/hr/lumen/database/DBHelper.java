package lumen.zpr.fer.hr.lumen.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.DriverManager.println;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORIES_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORIES_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORIES_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORIES_VALUE;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORY_WORDS_PAIRS_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORY_WORD_PAIRS_CATEGORY;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORY_WORD_PAIRS_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.CATEGORY_WORD_PAIRS_WORD;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.DATABASE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.IMAGES_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.IMAGES_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.IMAGES_PATH;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.IMAGES_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTERS_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTERS_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTERS_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTERS_VALUE;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_IDSOUND;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_LANGUAGE;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_LETTER;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.LETTER_SOUND_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.PATH_TO_IMAGES;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.PATH_TO_SOUND_WORD;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.SOUND_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.SOUND_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.SOUND_PATH;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.SOUND_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.WORDS_FILENAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.WORDS_ID;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.WORDS_LANGUAGE;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.WORDS_TABLE_NAME;
import static lumen.zpr.fer.hr.lumen.database.DBConstants.WORDS_VALUE;

/**
 * Ovaj {@link SQLiteOpenHelper} objekt služi za komunikaciju s bazom. On stvara, puni, i dohvaća
 * podatke iz baze pomoću svojih javnih metoda.
 *
 * @author Darian Šarić, Matija Čavlović
 * @version 0.5
 */

public class DBHelper extends SQLiteOpenHelper {
    /**
     * {@link AssetManager} preko kojeg se dohvaćaju datoteke koje sadrže podatke koje treba upisati
     * u bazu podataka
     */
    private static AssetManager assetManager;

    /**
     * Konstruktor koji inicijalizira {@link #assetManager}.
     *
     * @param context      kontekst aktivnosti koji stvara {@link DBHelper}
     * @param assetManager korišteni {@link AssetManager}
     */
    public DBHelper(Context context, AssetManager assetManager) {
        super(context, DATABASE_NAME, null, 1);
        DBHelper.assetManager = assetManager;
    }

    /**
     * Defaultni konstruktor.
     *
     * @param context kontekst aktivnosti koja stvara {@link DBHelper}
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("STVARANJE BAZE", "sdgs");
        //stvori tablicu "jezici"

//        db.execSQL("create table if not exists " + LANGUAGES_TABLE_NAME + "(" +
//                LANGUAGES_ID + " integer primary key autoincrement," +
//                LANGUAGES_VALUE + " varchar unique)");

        //stvori tablicu "kategorije"
        db.execSQL("create table if not exists " + CATEGORIES_TABLE_NAME + "(" +
                CATEGORIES_ID + " integer primary key autoincrement," +
                CATEGORIES_VALUE + " varchar unique)");

        //stvoir tablicu "slike"
        db.execSQL("create table if not exists " + IMAGES_TABLE_NAME + " (" +
                IMAGES_ID + " integer primary key autoincrement," +
                IMAGES_PATH + " varchar unique)");

        //stvori tablicu "rijeci"
        db.execSQL("create table if not exists " + WORDS_TABLE_NAME + "(" +
                WORDS_ID + " integer primary key autoincrement," +
                WORDS_LANGUAGE + " varchar ," +
                WORDS_VALUE + " varchar unique)");

        //stvori tablicu "zvukovi"
        db.execSQL("create table if not exists " + SOUND_TABLE_NAME + "(" +
                SOUND_ID + " integer primary key autoincrement," +
                SOUND_PATH + " varchar unique)");

        //stvori tablicu "slova"
        db.execSQL("create table if not exists " + LETTERS_TABLE_NAME + "(" +
                LETTERS_ID + " integer primary key autoincrement," +
                LETTERS_VALUE + " varchar unique)");

        db.execSQL("create table if not exists " + LETTER_SOUND_TABLE_NAME + "(" +
                LETTER_SOUND_ID + " integer primary key autoincrement," +
                LETTER_SOUND_LETTER + " varchar," +
                LETTER_SOUND_LANGUAGE + " varchar ," +
                LETTER_SOUND_IDSOUND + " varchar references " + SOUND_ID + ")");

        db.execSQL("create table if not exists " + CATEGORY_WORD_PAIRS_TABLE_NAME + "(" +
                CATEGORY_WORD_PAIRS_CATEGORY + " varchar references " + CATEGORIES_TABLE_NAME + " (" +
                CATEGORIES_VALUE + ")," +
                CATEGORY_WORD_PAIRS_WORD + " varchar references " + WORDS_TABLE_NAME + " (" +
                WORDS_VALUE + ")," +
                "primary key (" + CATEGORY_WORD_PAIRS_CATEGORY + "," + CATEGORY_WORD_PAIRS_WORD + "))");

        //popuni tablice
        try {
//            fillLanguageTable(db);
            fillCategoryTable(db);
            fillImageTable(db);
            fillWordsTable(db);
            fillSoundTable(db);
            fillLetterTable(db);
            fillLetterSoundTable(db);
            fillCategoryWordPairsTable(db);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //TODO: populiraj ovu tablicu
    private void fillCategoryWordPairsTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, CATEGORY_WORD_PAIRS_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(CATEGORY_WORDS_PAIRS_FILENAME)));

        while (reader.ready()) {
            insertCategoryWordEntity(db, reader.readLine());
        }

        reader.close();
    }

    private void insertCategoryWordEntity(SQLiteDatabase db, String line) {
        ContentValues values = new ContentValues();
        String[] attributes = line.split("\\s");
        if (attributes.length == 3) { //ako je kategorija prijevozno sredstvo
            values.put(CATEGORY_WORD_PAIRS_CATEGORY, attributes[0] + " " + attributes[1]);
            values.put(CATEGORY_WORD_PAIRS_WORD, attributes[2]);
        } else {
            values.put(CATEGORY_WORD_PAIRS_CATEGORY, attributes[0]);
            values.put(CATEGORY_WORD_PAIRS_WORD, attributes[1]);
        }

        db.insert(CATEGORY_WORD_PAIRS_TABLE_NAME, null, values);
    }
//    /**
//     * Puni tablicu "jezici" n-torkama.
//     *
//     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
//     * @throws IOException baca se ako dođe do greške pri čitanju prikladne datoteke
//     */
//    private void fillLanguageTable(SQLiteDatabase db) throws IOException {
//        if (tableFilled(db, LANGUAGES_TABLE_NAME)) {
//            return;
//        }
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                assetManager.open(LANGUAGES_FILENAME)));
//
//        while (reader.ready()) {
//            insertLanguageEntity(db, reader.readLine());
//        }
//
//        reader.close();
//    }
//    /**
//     * Zapisuje n-torku jezika s zadanim parametrima u tablicu "jezici".
//     *
//     * @param db   {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
//     * @param lang jezik koji zapisujemo u bazu
//     */
//    private void insertLanguageEntity(SQLiteDatabase db, String lang) {
//        ContentValues values = new ContentValues();
//        values.put(LANGUAGES_VALUE, lang);
//
//        db.insert(LANGUAGES_TABLE_NAME, null, values);
//    }

    /**
     * Puni tablicu "kategorije" n-torkama.
     *
     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @throws IOException baca se ako dođe do greške pri čitanju pripadne datoteke
     */
    private void fillCategoryTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, CATEGORIES_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(CATEGORIES_FILENAME)));

        while (reader.ready()) {
            insertCategoryEntity(db, reader.readLine());
        }

        reader.close();
    }

    /**
     * Zapisuje n-torku kategorije u tablicu "kategorije".
     *
     * @param db  {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param cat kategorija koju zapisujemo u bazu
     */
    private void insertCategoryEntity(SQLiteDatabase db, String cat) {
        ContentValues values = new ContentValues();
        values.put(CATEGORIES_VALUE, cat);

        db.insert(CATEGORIES_TABLE_NAME, null, values);
    }

    /**
     * Puni tablicu "slike" n-torkama.
     *
     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @throws IOException baca se ako dođe do greške pri čitanju pripadne datoteke
     */
    private void fillImageTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, IMAGES_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(IMAGES_FILENAME)));

        while (reader.ready()) {
            insertImageEntity(db, reader.readLine());
        }

        reader.close();
    }

    /**
     * Zapisuje n-torku slike u tablicu "slike".
     *
     * @param db        {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param imagePath staza do datoteke slike
     */
    private void insertImageEntity(SQLiteDatabase db, String imagePath) {
        ContentValues values = new ContentValues();
        values.put(IMAGES_PATH, imagePath);

        db.insert(IMAGES_TABLE_NAME, null, values);
    }

    /**
     * Puni tablicu "rijeci" n-torkama.
     *
     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @throws IOException baca se ako dođe do greške pri čitanju pripadne datoteke
     */
    private void fillWordsTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, WORDS_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(WORDS_FILENAME)));
        while (reader.ready()) {
            addWordEntity(db, reader.readLine());
        }

        reader.close();
    }

    /**
     * Zapisuje n-torku riječi u tablicu "rijeci".
     *
     * @param db        {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param wordEntry n-torka riječi koju unosimo u bazu podataka
     */
    private void addWordEntity(SQLiteDatabase db, String wordEntry) {
        String[] attributes = wordEntry.split(" ");
        ContentValues values = new ContentValues();
        values.put(WORDS_LANGUAGE, attributes[0]);
//        values.put(WORDS_IMAGE_PATH, attributes[1]);
//        values.put(WORDS_SOUND_ID, attributes[2]);
        values.put(WORDS_VALUE, attributes[1]);

        db.insert(WORDS_TABLE_NAME, null, values);
    }

    private void fillLetterTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, LETTERS_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(LETTERS_FILENAME)));
        while (reader.ready()) {
            addLetterEntity(db, reader.readLine());
        }
        reader.close();
    }

    private void addLetterEntity(SQLiteDatabase db, String letter) {
        ContentValues values = new ContentValues();
        values.put(LETTERS_VALUE, letter);
        db.insert(LETTERS_TABLE_NAME, null, values);
    }

    private void fillLetterSoundTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, LETTER_SOUND_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(LETTER_SOUND_FILENAME)));
        while (reader.ready()) {
            addLetterSoundEntity(db, reader.readLine());
        }
        reader.close();
    }

    private void addLetterSoundEntity(SQLiteDatabase db, String letterSound) {
        String[] attributes = letterSound.split(" ");
        Log.d("LSE", letterSound);
        ContentValues values = new ContentValues();
        values.put(LETTER_SOUND_LETTER, attributes[0]);
        values.put(LETTER_SOUND_LANGUAGE, attributes[1]);
        values.put(LETTER_SOUND_IDSOUND, attributes[2]);

        db.insert(LETTER_SOUND_TABLE_NAME, null, values);
    }

    /**
     * Puni tablicu "zvukovi" n-torkama
     */
    private void fillSoundTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, SOUND_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(SOUND_FILENAME)));
        while (reader.ready()) {
            addSoundEntity(db, reader.readLine());
        }

        reader.close();

    }

    /**
     * Zapisuje n-torku zvukovu u tablicu "zvukovi
     *
     * @param db        {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param soundPath Staza do datoteke zvučnih zapisa riječi
     */
    private void addSoundEntity(SQLiteDatabase db, String soundPath) {
        ContentValues values = new ContentValues();
        values.put(SOUND_PATH, soundPath);

        db.insert(SOUND_TABLE_NAME, null, values);
    }

    /**
     * Vraća <b>true</b> ako je tablica s predanim imenom prazna.
     *
     * @param db        {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param tableName ime tablice čiju praznost provjeravamo
     * @return <b>true</b> ako je tablica prazna, inače <b>false</b>
     */
    private boolean tableFilled(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(tableName, new String[]{"COUNT (*)"}, null, null,
                null, null, null);
        cursor.moveToFirst();

        boolean flag = cursor.getInt(0) > 0;
        cursor.close();

        return flag;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Za dani identifikator riječi vraća pripadnu riječ
     *
     * @param id identifikator riječi
     * @return pripadna riječ
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getWord(int id) {
        String word;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_VALUE}, WORDS_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        word = cursor.getString(0);
        cursor.close();

        return word;
    }

//    /**
//     * Za dani identifikator jezika vraća pripadni jezik
//     *
//     * @param id identifikator jezika
//     * @return pripadni jezik
//     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
//     */
//    public String getLanguage(int id) {
//        String lang;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(LANGUAGES_TABLE_NAME, new String[]{LANGUAGES_VALUE}, LANGUAGES_ID + " = ?",
//                new String[]{Integer.toString(id)}, null, null, null, null);
//
//        cursor.moveToFirst();
//        lang = cursor.getString(0);
//
//        cursor.close();
//
//        return lang;
//    }

    /**
     * Za dani identifikator kategorije vraća pripadnu kategoriju
     *
     * @param id identifikator kategorije
     * @return pripadna kategorija
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getCategory(int id) {
        String cat;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CATEGORIES_TABLE_NAME, new String[]{CATEGORIES_VALUE}, CATEGORIES_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        cat = cursor.getString(0);

        cursor.close();

        return cat;
    }

    /**
     * Za dani identifikator slova vraća pripadno slovo
     *
     * @param id identifikator riječi
     * @return pripadna riječ
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getLetter(int id) {
        String letter;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LETTERS_TABLE_NAME, new String[]{LETTERS_VALUE}, LETTERS_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        letter = cursor.getString(0);

        cursor.close();

        return letter;
    }

//    /**
//     * Za dani jezik vraca njegov identifikator
//     *
//     * @param language jezik
//     * @return pripadni identifikator
//     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani jezik
//     */
//    public int getLanguageId(String language) {
//        int langId;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(LANGUAGES_TABLE_NAME, new String[]{LANGUAGES_ID}, LANGUAGES_VALUE + " = ?",
//                new String[]{language}, null, null, null, null);
//
//        cursor.moveToFirst();
//        langId = Integer.parseInt(cursor.getString(0));
//
//        cursor.close();
//
//        return langId;
//    }

    /**
     * Za danu kategoriju vraca njezin identifikator
     *
     * @param category kategorija
     * @return pripadni identifikator
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dana kategorija
     */
    public int getCategoryId(String category) {
        int catId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CATEGORIES_TABLE_NAME, new String[]{CATEGORIES_ID}, CATEGORIES_VALUE + " = ?",
                new String[]{category}, null, null, null, null);

        cursor.moveToFirst();
        catId = Integer.parseInt(cursor.getString(0));

        cursor.close();

        return catId;
    }

    public Collection<String> getAllCategories() {
        Collection<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(true, CATEGORIES_TABLE_NAME, new String[]{CATEGORIES_VALUE},
                null, null, null, null, null, null);
//        System.out.println("pozvano");
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(0));
            System.out.println(cursor.getString(0));
        }

        cursor.close();
        return categories;
    }

    /**
     * Za dano slovo vraca njegov identifikator
     *
     * @param letter slovo
     * @return pripadni identifikator
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dano slovo
     */
    public int getLetterId(String letter) {
        int letterId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LETTERS_TABLE_NAME, new String[]{LETTERS_ID}, LETTERS_VALUE + " = ?",
                new String[]{letter}, null, null, null, null);

        cursor.moveToFirst();
        letterId = Integer.parseInt(cursor.getString(0));

        cursor.close();

        return letterId;
    }

    /**
     * Za dani identifikator slike vraca putanju do slike
     *
     * @param id identifikator slike
     * @return putanja do pripadne slike
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getImagePath(int id) {
        String imgPath;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(IMAGES_TABLE_NAME, new String[]{IMAGES_PATH}, IMAGES_PATH + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        imgPath = cursor.getString(0);

        cursor.close();

        return imgPath;
    }

    /**
     * Za dani identifikator zvučnog zapisa vraca putanju do zapisa
     *
     * @param id identifikator zvučnog zapisa
     * @return putanja do pripadnog zvučnog zapisa
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getSoundPath(int id) {
        String sndPath;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SOUND_TABLE_NAME, new String[]{SOUND_PATH}, SOUND_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        sndPath = cursor.getString(0);

        cursor.close();

        return sndPath;
    }


    /**
     * Vraća listu identifikatora svih riječi danog jezika i kategorije.
     *
     * @param language jezik
     * @param category kategorija
     * @return lista identifikatora svih odgovaraućih riječi u bazi
     */
    public List<Integer> getWords(String language, String category) {
        List<Integer> ids = new ArrayList<>();
        String joinQuery = "select * from " +
                WORDS_TABLE_NAME + " natural join " + CATEGORY_WORD_PAIRS_TABLE_NAME +
                " where " + WORDS_LANGUAGE + "='" + language + "' and " + CATEGORY_WORD_PAIRS_CATEGORY +
                "='" + category + "'";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if (category.equals("sve")) {
            cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_ID},
                    WORDS_LANGUAGE + "= ?",
                    new String[]{language}, null, null, null, null);
        } else {
            cursor = db.rawQuery(joinQuery, null);
//            cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_ID},
//                    WORDS_LANGUAGE + "= ? AND " + WORDS_CATEGORY + "= ?",
//                    new String[]{language, category}, null, null, null, null);
        }

        if (cursor.moveToFirst()) {
            do {
                ids.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return ids;
    }

//    /**
//     * Vraća listu identifikatora svih riječi danog jezika i kategorije.
//     *
//     * @param langId identifikator jezika
//     * @param catId  idetifikator kategorije
//     * @return lista identifikatora svih odgovaraućih riječi u bazi
//     */
//    public List<Integer> getWordIds(int langId, int catId) {
//        String language = getLanguage(langId);
//        String category = getCategory(catId);
//
//        return getWordIds(language, category);
//    }

    /**
     * Za dani identifikator riječi vraća path do pripadne slike.
     *
     * @param id identifikator riječi
     * @return path do slike riječi
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getWordImagePath(int id) {
        String imagePath;

        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_IMAGE_PATH}, WORDS_ID + " = ?",
//                new String[]{Integer.toString(id)}, null, null, null, null);
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_VALUE}, WORDS_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null);

        cursor.moveToFirst();
        cursor = db.query(IMAGES_TABLE_NAME, new String[]{IMAGES_PATH}, IMAGES_PATH + " like ?",
                new String[]{PATH_TO_IMAGES + cursor.getString(0) + "%"},
                null, null, null);
        cursor.moveToFirst();
//        imageId = Integer.parseInt(cursor.getString(0));
        imagePath = cursor.getString(0);

        cursor.close();

        return imagePath;
    }

    /**
     * Za dani identifikator riječi vraća path do pripadnog zvučnog zapisa.
     *
     * @param id identifikator riječi
     * @return path do zvučnog zapisa riječi
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getWordSoundPath(int id) {
        int soundId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_VALUE}, WORDS_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        cursor = db.query(SOUND_TABLE_NAME, new String[]{SOUND_ID}, SOUND_PATH + " like ?",
                new String[]{PATH_TO_SOUND_WORD + cursor.getString(0) + "%"}, null, null, null);
        cursor.moveToFirst();
        soundId = Integer.parseInt(cursor.getString(0));

        cursor.close();

        return getSoundPath(soundId);
    }


    public String getLetterSoundPath(String letter, String language) {
        int soundId;
        println(letter);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(LETTER_SOUND_TABLE_NAME, new String[]{LETTER_SOUND_IDSOUND},
                LETTER_SOUND_LETTER + " = ? AND " + LETTER_SOUND_LANGUAGE + "= ?",
                new String[]{letter, language}, null, null, null, null);

        cursor.moveToFirst();
        Log.d("LETTERSOUND", letter);
        soundId = Integer.parseInt(cursor.getString(0));

        cursor.close();

        return getSoundPath(soundId);
    }

    public String getLetterSoundPath(int letterId, int langId) {
        String letter = getLetter(letterId);
//        String language = getLanguage(langId);
        //trenutno hardkodirano radi jednostavnosti
        return getLetterSoundPath(letter, "hrvatski");
    }

}
