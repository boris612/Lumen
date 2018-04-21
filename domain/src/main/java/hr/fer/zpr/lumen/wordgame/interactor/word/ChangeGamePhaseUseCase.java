package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;

public class ChangeGamePhaseUseCaseImpl implements ChangeGamePhaseUseCase {

    private WordGameManager manager;

    public ChangeGamePhaseUseCaseImpl(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public void changeGamePhase(WordGamePhase phase) {
        manager.changePhase(phase);
    }
}
