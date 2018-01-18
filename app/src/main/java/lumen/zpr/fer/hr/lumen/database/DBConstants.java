package lumen.zpr.fer.hr.lumen.database;

/**
 * Ova klasa sadrži sve potrebne konstante za {@link DBHelper}.
 *
 * @author Darian Šarić
 */

final class DBConstants {
    /**
     * naziv datoteke baze podataka
     */
    static final String DATABASE_NAME = "lumeni.db";
//    /**
//     * naziv tablice "jezici"
//     */
//    static final String LANGUAGES_TABLE_NAME = "jezici";
//    /**
//     * naziv atributa "idjezik"
//     */
//    static final String LANGUAGES_ID = "idjezik";
//    /**
//     * naziv atributa "jezik"
//     */
//    static final String LANGUAGES_VALUE = "jezik";
//    /**
//     * naziv datoteke koja sadrži dostupne jezike
//     */
//    static final String LANGUAGES_FILENAME = "jezici.txt";
    /**
     * naziv tablice "kategorije"
     */
    static final String CATEGORIES_TABLE_NAME = "kategorije";
    /**
     * naziv atributa "idkategorija"
     */
    static final String CATEGORIES_ID = "idkategorija";
    /**
     * naziv atributa "kategorija"
     */
    static final String CATEGORIES_VALUE = "kategorija";
    /**
     * naziv datoteke iz koje se čitaju dostupne kategorije
     */
    static final String CATEGORIES_FILENAME = "kategorije.txt";

    /**
     * naziv tablice "slike"
     */
    static final String IMAGES_TABLE_NAME = "slike";
    /**
     * naziv atributa "idslika
     */
    static final String IMAGES_ID = "idslika";
    /**
     * naziv atributa "stazaslike"
     */
    static final String IMAGES_PATH = "stazaSlike";
    /**
     * naziv datoteke iz koje se čitaju dostupne slike
     */
    static final String IMAGES_FILENAME = "slike.txt";
    static final String PATH_TO_IMAGES = "slike/";

    /**
     * naziv tablice "rijeci"
     */
    static final String WORDS_TABLE_NAME = "rijeci";
    /**
     * naziv atributa "idrijec"
     */
    static final String WORDS_ID = "idrijec";
    /**
     * naziv atributa jezik
     */
    static final String WORDS_LANGUAGE = "jezik";
//    /**
//     * naziv atributa "kategorija"
//     */
//    static final String WORDS_CATEGORY = "kategorija";
//    /**
//     * naziv atributa "idslika"
//     */
//    static final String WORDS_IMAGE_PATH = "stazaslika";
//    /**
//     * naziv atributa "idzvuk"
//     */
//    static final String WORDS_SOUND_ID = "idzvuk";
    /**
     * naziv atributa "rijec"
     */
    static final String WORDS_VALUE = "rijec";
    /**
     * naziv datoteke koja sadrži zapise o riječima
     */
    static final String WORDS_FILENAME = "rijeci.txt";

    /**
     * naziv tablice "slova"
     */
    static final String LETTERS_TABLE_NAME = "slova";
    /**
     * naziv atributa "idslovo"
     */
    static final String LETTERS_ID = "idslovo";
    /**
     * naziv atributa "slovo"
     */
    static final String LETTERS_VALUE = "slovo";
    /**
     * naziv datoteke iz kojih se čitaju slova
     */
    static final String LETTERS_FILENAME = "slova.txt";

    /**
     * naziv tablice "zvukovislova"
     */
    static final String LETTER_SOUND_TABLE_NAME = "zvukovislova";
    /**
     * naziv atributa "idzvucnizapis"
     */
    static final String LETTER_SOUND_ID = "idzvucnizapis";
    /**
     * naziv atributa "slovo"
     */
    static final String LETTER_SOUND_LETTER = "slovo";
    /**
     * naziv atributa "jezik"
     */
    static final String LETTER_SOUND_LANGUAGE = "jezik";
    /**
     * naziv atributa "idzvuk"
     */
    static final String LETTER_SOUND_IDSOUND = "idzvuk";
    /**
     * naziv datoteke iz koje se čitaju vrijednosti n-torki
     */
    static final String LETTER_SOUND_FILENAME = "zvukoviSlova.txt";

    /**
     * naziv tablice "zvukovi"
     */
    static final String SOUND_TABLE_NAME = "zvukovi";
    /**
     * naziv atributa "idzvuk"
     */
    static final String SOUND_ID = "idzvuk";
    /**
     * naziv atributa "staza zvuka"
     */
    static final String SOUND_PATH = "stazaZvuk";
    /**
     * naziv datoteke iz koje se čitaju zvučni zapisi izgovora riječi
     */
    static final String SOUND_FILENAME = "zvukovi.txt";
    static final String PATH_TO_SOUND_WORD = "zvucniZapisi/rijeci/";
    static final String PATH_TO_SOUND_LETTER = "zvucniZapisi/hrvatskaAbeceda/";

    static final String CATEGORY_WORD_PAIRS_TABLE_NAME = "kategorijerijeci";

    static final String CATEGORY_WORD_PAIRS_CATEGORY = "kategorija";

    static final String CATEGORY_WORD_PAIRS_WORD = "rijec";

    static final String CATEGORY_WORDS_PAIRS_FILENAME = "kategorijerijeci.txt";
}
