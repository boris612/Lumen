package hr.fer.zpr.lumen.wordgame.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import hr.fer.zpr.lumen.wordgame.wordsplitter.CroatianWordSplitter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.EnglishWordSplitter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.WordSplitter;

public class WordGameUtil{

    private static final Map<Language,WordSplitter> wordSplitterMap=new HashMap<>(2);

    public  WordGameUtil() {
        wordSplitterMap.put(Language.CROATIAN, new CroatianWordSplitter());
        wordSplitterMap.put(Language.ENGLISH, new EnglishWordSplitter());

    }
        public List<Letter> split(String word, Language language){
            return wordSplitterMap.get(language).split(word);
        }

        public static WordSplitter getWordBuilderFromLanguage(Language language){
        return wordSplitterMap.get(language);
        }


}
