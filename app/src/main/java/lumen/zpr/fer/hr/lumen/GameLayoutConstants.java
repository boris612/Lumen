package lumen.zpr.fer.hr.lumen;

/**
 * Sadrži sve konstante kojima se opisuje raspored layouta u igri. Sve konstante su opisane kao
 * faktori koji se pri slaganju GUI-a množe sa dimenzijama displaya
 */

public class GameLayoutConstants {
    static double IMAGE_MAX_WIDTH_FACTOR = 0.7;
    static double IMAGE_MAX_HEIGHT_FACTOR = 0.4;
    static double IMAGE_Y_COORDINATE_FACTOR = 0.1;
    static double CHAR_FIELD_WIDTH_FACTOR = 1/25.0;
    static double GAP_BETWEEN_CHAR_FIELDS_FACTOR = CHAR_FIELD_WIDTH_FACTOR/5.0;
    static double CHAR_FIELDS_Y_COORDINATE_FACTOR = 0.55;

    //work in progress
    static int DEFAULT_RECT_LEFT_VALUE = 100;
    static int DEFAULT_RECT_TOP_VALUE = 100;
    static double RECT_SPACE_FACTOR=1.2;

    static int DEFAULT_RECT_WIDTH=60;
    static int DEFAULT_RECT_HEIGHT=60;
}
