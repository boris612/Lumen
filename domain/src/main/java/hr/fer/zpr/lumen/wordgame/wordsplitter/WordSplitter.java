package hr.fer.zpr.lumen.wordgame.wordsplitter;

import java.util.List;

import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;

public interface WordSplitter {

    List<Letter> split(String word);
}
