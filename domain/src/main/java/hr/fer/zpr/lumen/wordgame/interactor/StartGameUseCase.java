package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class StartGameUseCase implements CompletableUseCase {

    private WordGameManager manager;

    public StartGameUseCase(WordGameManager manager){
        this.manager=manager;
    }
    @Override
    public Completable execute() {
        return Completable.fromAction(()->manager.startGame());
    }
}
