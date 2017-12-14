package lumen.zpr.fer.hr.lumen;

/**
 * Sadrži sve konstante kojima se opisuje raspored layouta u igri. Sve konstante su opisane kao
 * faktori koji se pri slaganju GUI-a množe sa dimenzijama displaya
 */

public class GameLayoutConstants {
    static double IMAGE_MAX_WIDTH_FACTOR = 0.7;
    static double IMAGE_MAX_HEIGHT_FACTOR = 0.4;
    static double IMAGE_Y_COORDINATE_FACTOR = 0.1;

    static double CHAR_FIELD_WIDTH_MAX_FACTOR = 0.08;
    static double CHAR_FIELDS_WIDTH_FACTOR = 0.9;
    static double CHAR_FIELDS_Y_COORDINATE_FACTOR = 0.55;
    static double LETTER_IMAGE_Y_COORDINATE_FACTOR=0.85;
    static double CHAR_FIELD_GAP_WIDTH_TO_FIELD_WIDTH_FACTOR = 0.1;

    static double COIN_IMAGE_WIDTH_FACTOR = 0.1;
    static double COIN_IMAGE_X_COORDINATE_FACTOR = 0;
    static double COIN_IMAGE_Y_COORDINATE_FACTOR = 0;
    static int COIN_NUMBER_FONT_SIZE = 150;
    static double COIN_IMAGE_AND_TEXT_GAP_WIDTH_FACTOR = 0.1;

    static int WIN_TEXT_LABEL_SIZE = 300;
    static double WIN_TEXT_LABEL_X_CENTER_COORDINATE_FACTOR = 0.5;
    static double WIN_TEXT_LABEL_Y_CENTER_COORDINATE_FACTOR = 0.4;

    static int RECT_SPACE_FACTOR=50;
    static int DEFAULT_RECT_WIDTH=100;
    static int DEFAULT_RECT_HEIGHT=100;

    static double STARTING_HINT_HEIGHT_SCALE_FACTOR=0.25;
    static double LETTER_IMAGE_SCALE_FACTOR=0.9;

    static int SPACE_FROM_PIC_TO_HINT=20;
    static int SPACE_FROM_BORDER_TO_HINT=50;
    static int SPACE_FROM_LETTER_TO_LETTER=50;

}
