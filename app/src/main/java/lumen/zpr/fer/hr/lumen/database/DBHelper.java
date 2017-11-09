package lumen.zpr.fer.hr.lumen.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovaj {@link SQLiteOpenHelper} objekt služi za komunikaciju s bazom. On stvara, puni, i dohvaća
 * podatke iz baze pomoću svojih javnih metoda.
 *
 * @author Darian Šarić
 * @version 0.1
 */

public class DBHelper extends SQLiteOpenHelper {
    /**
     * naziv datoteke baze podataka
     */
    private static final String DATABASE_NAME = "lumeni.db";

    /**
     * naziv tablice "jezici"
     */
    private static final String LANGUAGES_TABLE_NAME = "jezici";
    /**
     * naziv atributa "idjezik"
     */
    private static final String LANGUAGES_ID = "idjezik";
    /**
     * naziv atributa "jezik"
     */
    private static final String LANGUAGES_VALUE = "jezik";
    /**
     * naziv datoteke koja sadrži dostupne jezike
     */
    private static final String LANGUAGES_FILENAME = "jezici.txt";

    /**
     * naziv tablice "kategorije"
     */
    private static final String CATEGORIES_TABLE_NAME = "kategorije";
    /**
     * naziv atributa "idkategorija"
     */
    private static final String CATEGORIES_ID = "idkategorija";
    /**
     * naziv atributa "kategorija"
     */
    private static final String CATEGORIES_VALUE = "kategorija";
    /**
     * naziv datoteke iz koje se čitaju dostupne kategorije
     */
    private static final String CATEGORIES_FILENAME = "kategorije.txt";

    /**
     * naziv tablice "slike"
     */
    private static final String IMAGES_TABLE_NAME = "slike";
    /**
     * naziv atributa "idslika
     */
    private static final String IMAGES_ID = "idslika";
    /**
     * naziv atributa "stazaslike"
     */
    private static final String IMAGES_PATH = "stazaSlike";
    /**
     * naziv datoteke iz koje se čitaju dostupne slike
     */
    private static final String IMAGES_FILENAME = "slike.txt";

    private static final String WORDS_TABLE_NAME = "rijeci";
    private static final String WORDS_ID = "idrijec";
    private static final String WORDS_LANGUAGE = "jezik";
    private static final String WORDS_CATEGORY = "kategorija";
    private static final String WORDS_IMAGE_ID = "idslika";
    private static final String WORDS_VALUE = "rijec";
    private static final String WORDS_FILENAME = "rijeci.txt";

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
        //stvori tablicu "jezici"

        db.execSQL("create table if not exists " + LANGUAGES_TABLE_NAME + "(" +
                LANGUAGES_ID + " integer primary key autoincrement," +
                LANGUAGES_VALUE + " varchar unique)");

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
                WORDS_LANGUAGE + " varchar references " + LANGUAGES_TABLE_NAME +
                "(" + LANGUAGES_VALUE + ")," +
                WORDS_CATEGORY + " varchar references " + CATEGORIES_TABLE_NAME +
                "(" + CATEGORIES_VALUE + ")," +
                WORDS_IMAGE_ID + " integer references " + IMAGES_TABLE_NAME + "," +
                WORDS_VALUE + " varchar unique)");

        //popuni tablice
        try {
            fillLanguageTable(db);
            fillCategoryTable(db);
            fillImageTable(db);
            fillWordsTable(db);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Puni tablicu "jezici" n-torkama.
     *
     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @throws IOException baca se ako dođe do greške pri čitanju prikladne datoteke
     */
    private void fillLanguageTable(SQLiteDatabase db) throws IOException {
        if (tableFilled(db, LANGUAGES_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(LANGUAGES_FILENAME)));

        while (reader.ready()) {
            insertLanguageEntity(db, reader.readLine());
        }

        reader.close();
    }

    /**
     * Zapisuje n-torku jezika s zadanim parametrima u tablicu "jezici".
     *
     * @param db   {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param lang jezik koji zapisujemo u bazu
     */
    private void insertLanguageEntity(SQLiteDatabase db, String lang) {
        ContentValues values = new ContentValues();
        values.put(LANGUAGES_VALUE, lang);

        db.insert(LANGUAGES_TABLE_NAME, null, values);
    }

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
        if(tableFilled(db, WORDS_TABLE_NAME)) {
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                assetManager.open(WORDS_FILENAME)));
        while(reader.ready()) {
            addWordEntity(db, reader.readLine());
        }

        reader.close();
    }

    /**
     * Zapisuje n-torku riječi u tablicu "rijeci".
     *
     * @param db {@linkplain SQLiteDatabase} objekt za izvršavanje SQL naredbi
     * @param wordEntry n-torka riječi koju unosimo u bazu podataka
     */
    private void addWordEntity(SQLiteDatabase db, String wordEntry) {
        String[] attributes = wordEntry.split(" ");
        ContentValues values = new ContentValues();
        values.put(WORDS_LANGUAGE, attributes[0]);
        values.put(WORDS_CATEGORY, attributes[1]);
        values.put(WORDS_IMAGE_ID, attributes[2]);
        values.put(WORDS_VALUE, attributes[3]);

        db.insert(WORDS_TABLE_NAME, null, values);
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
     * Vraća listu identifikatora svih riječi danog jezika i kategorije.
     *
     * @param language jezik
     * @param category kategorija
     * @return lista identifikatora svih odgovaraućih riječi u bazi
     */
    public List<Integer> getWordIds (String language, String category) {
        List<Integer> words = new ArrayList<Integer>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[] {WORDS_ID},
                WORDS_LANGUAGE + "= ? AND " + WORDS_CATEGORY + "= ?",
                new String[] {language, category}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                words.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        return words;
    }

    /**
     * Za dani id vraća pripadnu riječ
     *
     * @param id identifikator riječi
     * @return pripadna riječ
     * @throws android.database.CursorIndexOutOfBoundsException du bazi ne postoji dani identifikator
     */
    public String getWord(int id) {
        String word;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[]{WORDS_VALUE}, WORDS_ID + " = ?",
                new String[]{Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        word = cursor.getString(0);

        return word;
    }

    /**
     * Za dani identifikator riječi vraća path do pripadne slike.
     *
     * @param id identifikator riječi
     * @return path do slike riječi
     * @throws android.database.CursorIndexOutOfBoundsException u bazi ne postoji dani identifikator
     */
    public String getWordPath(int id) {
        String path;
        String imageId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORDS_TABLE_NAME, new String[] {WORDS_IMAGE_ID}, WORDS_ID + " = ?",
                new String[] {Integer.toString(id)}, null, null, null, null);

        cursor.moveToFirst();
        imageId = cursor.getString(0);

        cursor = db.query(IMAGES_TABLE_NAME, new String[] {IMAGES_PATH}, IMAGES_ID + " = ?",
                new String[] {imageId}, null, null, null , null);

        cursor.moveToFirst();
        path = cursor.getString(0);

        return path;
    }
}
