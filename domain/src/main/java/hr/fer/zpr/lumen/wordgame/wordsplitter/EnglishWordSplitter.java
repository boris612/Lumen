package hr.fer.zpr.lumen.wordgame.wordsplitter;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zpr.lumen.wordgame.model.Letter;

public class EnglishWordSplitter implements WordSplitter {

    @Override
    public List<Letter> split(String word) {
        List<Letter> letters = new ArrayList<>();
        String[] splitLetters = word.split("");

        for (String str : splitLetters) {
            letters.add(new Letter(str));
        }

        return letters;
    }
}
