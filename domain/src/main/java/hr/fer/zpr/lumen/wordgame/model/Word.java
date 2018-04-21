package hr.fer.zpr.lumen.wordgame.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.Util.WordGameUtil;


public final class Word  {

    public final List<Letter> letters;
    public final Language language;
    public final Set<Category> categories;
    public final String stringValue;
    public final Image wordImage;

    public Word(String word, Language language, Collection<Category> categories,Image image){
    this.language=language;
    this.categories=new HashSet<>(categories);
    this.stringValue=word;
    this.wordImage=image;
    this.letters= WordGameUtil.getWordBuilderFromLanguage(language).split(word);

    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Word)) return false;
        Word word=(Word)o;
        if(!this.stringValue.equals(word.stringValue)) return false;
        if(this.language!=word.language) return false;
        return true;
    }
}
