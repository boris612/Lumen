package hr.fer.zpr.lumen.wordgame.wordsplitter;

import java.util.List;

import hr.fer.zpr.lumen.wordgame.model.Letter;

public interface WordSplitter {

    List<Letter> split(String word);
}
