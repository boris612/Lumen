package hr.fer.zpr.lumen.wordgame.manager;

import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import io.reactivex.Single;

public interface WordGameManager {

    Single<Word> startGame(List<Word> words);


    Single<Boolean> isAnswerCorrect();

    Single<Boolean> isCreateMoreLettersActive();

    Single<Boolean> isCreateAllLettersActive();

    void setCreateMoreLetters(boolean value);

    void setCreateAllLetters(boolean value);

    Single<Boolean> insertLetterIntoField(String letter, int position);

    void removeLetterFromField(int index);

    void changeCategories(Set<String> categories);

    void changeLanguage(String language);

    void changePhase(WordGamePhase phase);

    Single<Boolean> setHint(Boolean active);

    Single<List<Letter>> getRandomLetters(int n);

    Single<List<Letter>> getAllLetters();

    void setMessagesLanguage(String language);


    void resetGame();

    Single<Boolean> isGamePhasePlaying();

    Single<Boolean> isGamePhasePresenting();

    Single<Boolean> isHintOnCorrectOn();

    Single<Boolean> isHintInstantlyOn();

    Single<Boolean> isHintWhenFullOn();

    Single<Boolean> areAllFieldsFull();

    Single<String> getIncorrectMessage();

    Single<String> getCorrectMessage();

    Single<Integer> getIndexOfFirstIncorrectField();

    Single<String> getCorrectLetterForIndex(int index);

    Single<Boolean> isHintActive();

    Single<Integer> getCoins();

    void setCoins(int n);

    Single<Word> nextWord();

    void setGreenOnCorrect(boolean active);

    void setGreenInstantly(boolean active);

    void setGreenWhenFull(boolean active);

    void setLanguage(String language);


}
