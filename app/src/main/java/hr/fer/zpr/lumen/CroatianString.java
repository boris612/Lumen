package hr.fer.zpr.lumen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 13.12.2017..
 */

public class CroatianString implements LangDependentString {
    private List<String> letters;

    public CroatianString(String string) {
        letters = new ArrayList<>();
        convertStringToLetters(string);
    }

    private void convertStringToLetters(String string) {
        int len = string.length();
        for (int i = 0; i < len; i++) {
            if (i != (len - 1) && isSpecialSequence(string.substring(i, i + 2))) {
                letters.add(string.substring(i, i + 2));
                i++;
            } else {
                letters.add(string.substring(i, i + 1));
            }
        }
    }

    @Override
    public int length() {
        return letters.size();
    }

    @Override
    public String charAt(int i) {
        return letters.get(i);
    }

    @Override
    public LangDependentString toLowerCase() {
        StringBuilder strb = new StringBuilder();
        for (String letter : letters) {
            strb.append(letter.toLowerCase());
        }
        return new CroatianString(strb.toString());
    }

    @Override
    public LangDependentString toUpperCase() {
        StringBuilder strb = new StringBuilder();
        for (String letter : letters) {
            strb.append(letter.toLowerCase());
        }
        return new CroatianString(strb.toString());
    }

    private boolean isSpecialSequence(String sequence) {
        if (sequence.toLowerCase().contains("lj") || sequence.toLowerCase().contains("dž") || sequence.toLowerCase().contains("nj")) {
            return true;
        }
        return false;
    }
}
