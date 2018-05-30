package hr.fer.zpr.lumen.wordgame.manager;

import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import io.reactivex.Single;

public interface WordGameManager {

        Single<Word> startGame(List<Word> words);


        Single<Boolean> isAnswerCorrect();

        Single<Boolean> isCreateMoreLettersActive();

        void setCreateMoreLetters(boolean value);

        Single<Boolean> insertLetterintoField(String letter, int position);

        void removeLetterFromField(int index);

        void changeCategories(Set<String> categories);

        void changeLanguage(String language);

        void changePhase(WordGamePhase phase);

        Single<Boolean> setHint(Boolean active);

        Single<List<Letter>> getRandomLetters(int n);



        void resetGame();

        Single<Boolean> isGamePhasePlaying();

        Single<Boolean> isHintOnCorrectOn();

        Single<Boolean> areAllFieldsFull();

        Single<String> getIncorrectMessage();

        Single<String> getCorrectMessage();

        Single<Integer> getIndexOfFirstIncorrectField();

        Single<String> getCorrectLetterForIndex(int index);

        Single<Boolean> isHintActive();


        void setCoins(int n);

        Single<Integer> getCoins();

        Single<Word> nextWord();

        void setGreenOnCorrect(boolean active);

        void setLanguage(String language);



}
