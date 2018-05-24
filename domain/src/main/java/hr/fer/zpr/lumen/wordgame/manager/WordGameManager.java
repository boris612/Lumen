package hr.fer.zpr.lumen.wordgame.manager;

import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Language;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import hr.fer.zpr.lumen.wordgame.model.Word;
import io.reactivex.Single;

public interface WordGameManager {

        Word startGame(List<Word> words);

        void nextRound();

        Single<Boolean> isAnswerCorrect();


        void insertLetterintoField(Letter letter, int position);

        void removeLetterFromField(Letter letter);

        void changeCategories(Set<Category> categories);

        void changeLanguage(Language language);

        void changePhase(WordGamePhase phase);

        void setHint(Boolean active);

        void resetGame();

}
