package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeGreenInstantlyUseCase implements CompletableUseCaseWithParams<Boolean> {

    private WordGameManager manager;

    public ChangeGreenInstantlyUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Boolean aBoolean) {
        return Completable.fromAction(() -> manager.setGreenInstantly(aBoolean));
    }
}
