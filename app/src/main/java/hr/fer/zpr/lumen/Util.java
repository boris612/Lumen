package hr.fer.zpr.lumen;

/**
 * Created by Marko on 16.11.2017..
 */

public class Util {

    public static int getCroatianLength(String word) {
        int len = word.length();
        word = word.toLowerCase();
        for (int i = 0, j = word.length() - 1; i < j; i++) {
            String sub = word.substring(i, i + 2);
            if (sub.equals("nj") || sub.equals("lj") || sub.equals("dž")) {
                len--;
            }
        }
        return len;
    }

    public static boolean isCroatianSequence(String sequence) {
        sequence = sequence.toLowerCase();
        if (sequence.equals("nj") || sequence.equals("lj") || sequence.equals("lj")) {
            return true;
        }
        return false;
    }
}
