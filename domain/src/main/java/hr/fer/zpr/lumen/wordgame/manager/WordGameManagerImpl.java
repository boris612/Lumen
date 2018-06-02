package hr.fer.zpr.lumen.wordgame.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Coins;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.LetterField;
import hr.fer.zpr.lumen.wordgame.model.Word;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import io.reactivex.Single;

public class WordGameManagerImpl implements WordGameManager {

    public static final int MAX_NUMBER_OF_LETTERS = 12;

    public static final int COINS_TO_ADD = 2;

    private List<Word> usedWords = new ArrayList<>();
    private List<Word> unusedWords = new ArrayList<>();
    private LetterField letterField;
    private List<Letter> letters;
    private Word currentWord;
    private Set<Category> currentCategories;
    private Language currentLanguage = Language.CROATIAN;
    private WordGamePhase phase;
    private boolean hintOnCorrectLetter = true;

    private boolean hintActive;


    private boolean createMoreLetters;

    private WordGameRepository repository;

    private Coins coins;

    public WordGameManagerImpl(WordGameRepository repository) {
        this.repository = repository;

    }


    @Override
    public Single<Word> startGame(List<Word> words) {
        phase = WordGamePhase.PRESENTING;
        unusedWords = words;
        currentWord = unusedWords.get(new Random().nextInt(unusedWords.size()));
        unusedWords.remove(currentWord);
        letterField = new LetterField(currentWord.letters.size());
        return Single.just(currentWord);
    }

    @Override
    public void changePhase(WordGamePhase phase) {
        this.phase = phase;
    }

    @Override
    public Single<Boolean> setHint(Boolean active) {
        if (coins.getCoins() < 1) return Single.just(false);
        hintActive = active;
        if (active)
            coins.subtractCoins(1);
        return Single.just(true);
    }


    @Override
    public Single<Boolean> isAnswerCorrect() {
        boolean correct = letterField.isWordCorrect(currentWord);
        if (correct) {
            coins.addCoins(COINS_TO_ADD);
        }
        return Single.just(correct);
    }


    @Override
    public Single<Boolean> insertLetterIntoField(String letter, int position) {
        letterField.insertLetterIntoField(new Letter(letter), position);
        return Single.just(letterField.isLetterAtNCorrect(position, currentWord));
    }

    @Override
    public void removeLetterFromField(int index) {
        letterField.removeLetterFromField(index);
    }

    @Override
    public void changeCategories(Set<String> categories) {
        if (categories == null) {
            Set<Category> nCategories = new HashSet<>();
            nCategories.addAll(Arrays.asList(Category.values()));
            this.currentCategories = nCategories;
            resetGame();
            return;
        }
        Set<Category> nc = new HashSet<>();
        for (String s : categories) {
            nc.add(Category.valueOf(s.toUpperCase()));
        }
        this.currentCategories = nc;
        resetGame();
    }

    @Override
    public void changeLanguage(String language) {
        currentLanguage = Language.valueOf(language.toUpperCase());

    }

    @Override
    public void resetGame() {

        usedWords = new ArrayList<>();
        unusedWords = repository.getWordsFromCategories(currentCategories, currentLanguage).blockingGet();
        currentWord = null;
    }

    @Override
    public Single<Boolean> isCreateMoreLettersActive() {
        return Single.just(createMoreLetters);
    }

    @Override
    public void setCreateMoreLetters(boolean value) {
        this.createMoreLetters = value;
    }

    @Override
    public Single<Boolean> isGamePhasePlaying() {
        if (phase == WordGamePhase.PLAYING) return Single.just(true);
        return Single.just(false);
    }

    @Override
    public Single<Boolean> isHintOnCorrectOn() {
        return Single.just(hintOnCorrectLetter);
    }

    @Override
    public Single<Boolean> areAllFieldsFull() {
        return Single.just(letterField.isFull());
    }

    @Override
    public Single<String> getIncorrectMessage() {
        return repository.incorrectMessage(currentLanguage);
    }

    @Override
    public Single<String> getCorrectMessage() {
        return repository.getCorrectMessage(currentLanguage);
    }

    @Override
    public Single<String> getCorrectLetterForIndex(int index) {
        return Single.just(currentWord.letters.get(index).value);
    }

    @Override
    public Single<Integer> getIndexOfFirstIncorrectField() {
        return Single.just(letterField.indexOfFirstIncorrect(currentWord));
    }

    @Override
    public Single<Boolean> isHintActive() {
        return Single.just(hintActive);
    }

    @Override
    public Single<Integer> getCoins() {
        if (coins == null) return Single.just(0);
        return Single.just(coins.getCoins());
    }

    @Override
    public void setCoins(int n) {
        if (coins == null) coins = new Coins(n);
        else coins.setCoins(n);
    }

    @Override
    public Single<Word> nextWord() {
        if (unusedWords == null)
            unusedWords = repository.getWordsFromCategories(currentCategories, currentLanguage).blockingGet();
        currentWord = unusedWords.get(new Random().nextInt(unusedWords.size()));
        unusedWords.remove(currentWord);
        usedWords.add(currentWord);
        if (unusedWords.size() == 0) {
            unusedWords = usedWords;
            usedWords = new ArrayList<>();
        }
        letterField = new LetterField(currentWord.letters.size());
        return Single.just(currentWord);
    }

    @Override
    public Single<List<Letter>> getRandomLetters(int n) {
        return repository.getRandomLettersForLanguage(currentLanguage, n);

    }

    @Override
    public void setGreenOnCorrect(boolean active) {
        this.hintOnCorrectLetter = active;
    }

    @Override
    public void setLanguage(String language) {
        this.currentLanguage = Language.valueOf(language.toUpperCase());
        unusedWords = null;
        usedWords.clear();
    }
}
