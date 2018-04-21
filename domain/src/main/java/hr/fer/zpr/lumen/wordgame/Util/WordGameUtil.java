package hr.fer.zpr.lumen.wordgame.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.CroatianWordSplitter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.EnglishWordSplitter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.WordSplitter;

public class WordGameUtil{

    private final Map<Language,WordSplitter> wordSplitterMap=new HashMap<>(2);

    public  WordGameUtil(final CroatianWordSplitter croatianWordSplitter,final EnglishWordSplitter englishWordSplitter) {
        wordSplitterMap.put(Language.CROATIAN, croatianWordSplitter);
        wordSplitterMap.put(Language.ENGLISH, englishWordSplitter);

    }
        public List<Letter> split(String word,Language language){
            return wordSplitterMap.get(language).split(word);
        }


}
