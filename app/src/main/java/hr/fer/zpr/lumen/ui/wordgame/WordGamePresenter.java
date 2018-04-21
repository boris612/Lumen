package hr.fer.zpr.lumen.ui.wordgame;

import android.view.View;

import java.util.List;

import hr.fer.zpr.lumen.ui.wordgame.models.LetterModel;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;

public class WordGamePresenter implements WordGameContract {


    private View view;
    private WordGameManager manager;


    public WordGamePresenter() {

    }

    @Override
    public void setView(View view) {
        this.view=view;
    }

    @Override
    public void presentWord() {

    }

    @Override
    public void changeGamePhase(WordGamePhase phase) {

    }

    public void update() {

    }
}
