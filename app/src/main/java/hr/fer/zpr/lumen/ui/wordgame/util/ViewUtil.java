package hr.fer.zpr.lumen.ui.wordgame.util;

public class ViewUtil {

    public static int calculateStartingX(int screenWidth, int wordLength, int fieldDimension, int gapWidth) {
        int componentWidth = fieldDimension * wordLength + (wordLength - 1) * gapWidth;
        return screenWidth / 2 - componentWidth / 2;
    }
}
