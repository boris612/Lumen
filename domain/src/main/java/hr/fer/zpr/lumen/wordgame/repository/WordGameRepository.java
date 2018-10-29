package hr.fer.zpr.lumen.wordgame.repository;

import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import io.reactivex.Single;

public interface WordGameRepository {

    public Single<List<Word>> getAllWords(Language language);


    public Single<List<Word>> getWordsFromCategories(Set<Category> categories, Language language);

    public Single<List<Letter>> getRandomLettersForLanguage(Language language, int n);

    public Single<List<Letter>> getAllLettersForLanguage(Language language);

    public Single<List<Letter>> getLettersWithImages(List<Letter> letters, Language language);

    public Single<String> incorrectMessage(Language language);

    public Single<String> getCorrectMessage(Language language);
}
