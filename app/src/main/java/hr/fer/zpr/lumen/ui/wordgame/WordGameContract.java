package hr.fer.zpr.lumen.ui.wordgame;

import android.view.View;

import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;

public interface WordGameContract {

    void setView(View view);

    void presentWord();

    void changeGamePhase(WordGamePhase phase);


}
