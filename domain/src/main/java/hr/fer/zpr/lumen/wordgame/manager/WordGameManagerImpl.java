package hr.fer.zpr.lumen.wordgame.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.Util.LetterGenerator;
import hr.fer.zpr.lumen.wordgame.Util.WordGameUtil;
import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.LetterField;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import hr.fer.zpr.lumen.wordgame.wordsplitter.CroatianWordSplitter;
import hr.fer.zpr.lumen.wordgame.wordsplitter.EnglishWordSplitter;
import io.reactivex.Single;

public class WordGameManagerImpl implements WordGameManager {

    public static final int MAX_NUMBER_OF_LETTERS = 12;

    private List<Word> usedWords;
    private List<Word> unusedWords;
    private LetterField letterField;
    private List<Letter> letters;
    private Word currentWord;
    private Set<Category> currentCategories;
    private Language currentLanguage;
    private WordGamePhase phase;

    private boolean hintActive;

    private WordGameUtil wordGameUtil;

    private boolean createMoreLetters;

    private WordGameRepository repository;

    public WordGameManagerImpl(WordGameRepository repository) {
            this.repository=repository;
            wordGameUtil=new WordGameUtil();
    }



    @Override
    public Word startGame(List<Word> words) {
        phase = WordGamePhase.PRESENTING;
        unusedWords=words;
        currentWord=unusedWords.get(new Random().nextInt(unusedWords.size()));
        unusedWords.remove(currentWord);

        return currentWord;
    }

    @Override
    public void changePhase(WordGamePhase phase) {
        this.phase = phase;
    }

    @Override
    public void setHint(Boolean active) {
        hintActive=active;
    }

    @Override
    public void nextRound() {
        if (currentWord != null) usedWords.add(currentWord);
        phase = WordGamePhase.PRESENTING;
        currentWord = unusedWords.get(new Random().nextInt(unusedWords.size()));
        unusedWords.remove(currentWord);
        letterField = new LetterField(currentWord.stringValue.length());
        letters = new ArrayList<>(WordGameUtil.getWordBuilderFromLanguage(currentLanguage).split(currentWord.stringValue));
        if (createMoreLetters) {
            letters.addAll(new LetterGenerator().getRandomLetters(10-letters.size(),currentLanguage));
        }
    }

    @Override
    public Single<Boolean> isAnswerCorrect() {
        return Single.just(letterField.isWordCorrect(currentWord));
    }


    @Override
    public void insertLetterintoField(Letter letter, int position) {
        letterField.insertLetterIntoField(letter, position);
    }

    @Override
    public void removeLetterFromField(Letter letter) {
        letterField.removeLetterFromField(letter);
    }

    @Override
    public void changeCategories(Set<Category> categories) {
        currentCategories = categories;
        resetGame();
    }

    @Override
    public void changeLanguage(Language language) {
        currentLanguage = language;

    }

    @Override
    public void resetGame() {

        usedWords = new ArrayList<>();
        currentWord = null;
    }

    @Override
    public boolean isCreateMoreLettersActive() {
        return createMoreLetters;
    }

    @Override
    public void setCreateMoreLetters(boolean value) {
        this.createMoreLetters=value;
    }


}
