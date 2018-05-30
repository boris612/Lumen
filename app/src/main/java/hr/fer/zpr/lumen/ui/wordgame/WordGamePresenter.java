package hr.fer.zpr.lumen.ui.wordgame;

import android.view.View;

import javax.inject.Inject;

import hr.fer.zpr.lumen.ui.wordgame.models.LetterFieldModel;
import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Word;
import wordgame.db.database.WordGameDatabase;

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
