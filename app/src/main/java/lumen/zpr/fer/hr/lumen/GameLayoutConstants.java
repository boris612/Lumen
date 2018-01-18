package lumen.zpr.fer.hr.lumen;

/**
 * Sadrži sve konstante kojima se opisuje raspored layouta u igri. Sve konstante su opisane kao
 * faktori koji se pri slaganju GUI-a množe sa dimenzijama displaya
 */

public class GameLayoutConstants {
    static double IMAGE_MAX_WIDTH_FACTOR = 0.7;
    static double IMAGE_MAX_HEIGHT_FACTOR = 0.3;
    static double IMAGE_PRESENTING_MAX_WIDTH_FACTOR = 0.8;
    static double IMAGE_PRESENTING_MAX_HEIGHT_FACTOR = 0.5;
    static double IMAGE_Y_COORDINATE_FACTOR = 0.05;

    static double CHAR_FIELD_WIDTH_MAX_FACTOR = 0.13;
    static double CHAR_FIELDS_WIDTH_FACTOR = 0.8;
    static double CHAR_FIELDS_Y_COORDINATE_FACTOR = IMAGE_MAX_HEIGHT_FACTOR+IMAGE_Y_COORDINATE_FACTOR+0.05;
    static double LETTER_IMAGE_Y_COORDINATE_FACTOR=0.85;
    static double CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR = 0.1;

    static double COIN_IMAGE_WIDTH_FACTOR = 0.1;
    static double COIN_IMAGE_X_COORDINATE_FACTOR = 0;
    static double COIN_IMAGE_Y_COORDINATE_FACTOR = 0;
    static int COIN_NUMBER_FONT_SIZE = 150;
    static double COIN_IMAGE_AND_TEXT_GAP_WIDTH_FACTOR = 0.1;

    static double WIN_IMAGE_X_CENTER_COORDINATE_FACTOR = 0.5;
    static double WIN_IMAGE_Y_CENTER_COORDINATE_FACTOR = 0.4;
    static double WIN_IMAGE_WIDTH_FACTOR = 0.9; //u odnosu na height

    static int RECT_SPACE_FACTOR=50;
    static int DEFAULT_RECT_WIDTH=100;
    static int DEFAULT_RECT_HEIGHT=100;

    static double STARTING_HINT_HEIGHT_SCALE_FACTOR=0.25;
    static double LETTER_IMAGE_SCALE_FACTOR=0.7;

    static int SPACE_FROM_BORDER_TO_HINT=50;
    static int SPACE_FROM_LETTER_TO_LETTER=10;

    static int MAX_NUM_OF_LETTERS=9;
    static int GREEN_HINT_TIME=500;

    static double HAND_IMAGE_WIDTH_FACTOR = 0.15;
    static double HAND_IMAGE_HEIGHT_FACTOR = 1.4145*HAND_IMAGE_WIDTH_FACTOR;
    static double HAND_IMAGE_FOREFINGER_X_COORDINATE_FACTOR = 0.3615;
    static double HAND_IMAGE_FOREFINGER_Y_COORDINATE_FACTOR = 0.0736;
}
