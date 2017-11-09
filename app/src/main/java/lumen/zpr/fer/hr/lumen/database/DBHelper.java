package lumen.zpr.fer.hr.lumen.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Ovaj {@link SQLiteOpenHelper} objekt služi za komunikaciju s bazom. On stvara, puni, i dohvaća
 * podatke iz baze pomoću svojih javnih metoda.
 *
 * @author Darian Šarić
 * @version 0.5
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

    /**
     * naziv tablice "rijeci"
     */
    private static final String WORDS_TABLE_NAME = "rijeci";
    /**
     * naziv atributa "idrijec"
     */
    private static final String WORDS_ID = "idrijec";
    /**
     * naziv atributa jezik
     */
    private static final String WORDS_LANGUAGE = "jezik";
    /**
     * naziv atributa "kategorija"
     */
    private static final String WORDS_CATEGORY = "kategorija";
    /**
     * naziv atributa "idslika"
     */
    private static final String WORDS_IMAGE_ID = "idslika";
    /**
     * naziv atributa "rijec"
     */
    private static final String WORDS_VALUE = "rijec";
    /**
     * naziv datoteke koja sadrži zapise o riječima
     */
    private static final String WORDS_FILENAME = "rijeci.txt";

    /**
     * naziv tablice "slova"
     */
    private static final String LETTERS_TABLE_NAME = "slova";
    /**
     * naziv atributa "idslovo"
     */
    private static final String LETTERS_ID = "idslovo";
    /**
     * naziv atributa "slovo"
     */
    private static final String LETTERS_VALUE = "slovo";
    /**
     * naziv datoteke iz kojih se čitaju slova
     */
    private static final String LETTERS_FILENAME = "slova.txt";

    /**
     * naziv tablice "zvukovislova"
     */
    private static final String LETTER_SOUND_TABLE_NAME = "zvukovislova";
    /**
     * naziv atributa "idzvucnizapis"
     */
    private static final String LETTER_SOUND_ID = "idzvucnizapis";
    /**
     * naziv atributa "slovo"
     */
    private static final String LETTER_SOUND_LETTER = "slovo";
    /**
     * naziv atributa "jezik"
     */
    private static final String LETTER_SOUND_LANGUAGE = "jezik";
    /**
     * naziv atributa "idzvuk"
     */
    private static final String LETTER_SOUND_IDSOUND = "idzvuk";
    /**
     * naziv datoteke iz koje se čitaju vrijednosti n-torki
     */
    private static final String LETTER_SOUND_FILENAME = "zvukovislova.txt";

    /**
     * naziv tablice "zvukovi"
     */
    private static final String SOUND_TABLE_NAME = "zvukovi";
    /**
     * naziv atributa "idzvuk"
     */
    private static final String SOUND_ID = "idzvuk";
    /**
     * naziv atributa "staza zvuka"
     */
    private static final String SOUND_PATH = "stazaZvuk";
    /**
     * naziv datoteke iz koje se čitaju zvučni zapisi izgovora riječi
     */
    private static final String SOUND_FILENAME = "zvukovi.txt";

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
                LETTER_SOUND_LANGUAGE + " varchar references " + LANGUAGES_TABLE_NAME + "(" +
                LANGUAGES_VALUE + ")," +
                LETTER_SOUND_IDSOUND + " varchar references " + SOUND_ID + ")");


        //popuni tablice
        try {
            fillLanguageTable(db);
            fillCategoryTable(db);
            fillImageTable(db);
            fillWordsTable(db);
            fillSoundTable(db);
            fillLetterTable(db);
            fillLetterSoundTable(db);
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
        values.put(WORDS_CATEGORY, attributes[1]);
        values.put(WORDS_IMAGE_ID, attributes[2]);
        values.put(WORDS_VALUE, attributes[3]);

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
}
