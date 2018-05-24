package hr.fer.zpr.lumen.ui.wordgame;

import android.view.View;

import javax.inject.Inject;

import hr.fer.zpr.lumen.base.BasePresenter;
import hr.fer.zpr.lumen.base.ScopedPresenter;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Word;
import wordgame.db.database.WordGameDatabase;

public interface WordGamePresenter {

    void presentWord(Word word);

    void activateHint();

    void newWord();

    void update();

    void startGame();

    void setView(WordGameView view);




}
