package hr.fer.zpr.lumen.ui.wordgame;

import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.wordgame.model.Word;

public interface WordGamePresenter {

    void presentWord(Word word);

    void startGame();

    void setView(WordGameView view);

    boolean shouldHandleTouch();

    void letterInserted(LetterModel letter, LetterFieldModel field);

    void letterRemoved(LetterFieldModel field);

    void hintPressed();

    void exit();


}
