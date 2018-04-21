package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGamePhase;
import io.reactivex.Completable;

public class ChangeGamePhaseUseCase implements CompletableUseCaseWithParams<WordGamePhase> {

    private WordGameManager manager;

    public ChangeGamePhaseUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Completable execute(WordGamePhase phase) {
        return Completable.fromAction(()->manager.changePhase(phase));
    }
}
