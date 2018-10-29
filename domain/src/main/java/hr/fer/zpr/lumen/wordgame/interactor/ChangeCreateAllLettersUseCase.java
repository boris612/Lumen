package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeCreateAllLettersUseCase implements CompletableUseCaseWithParams<Boolean> {

    private WordGameManager manager;

    public ChangeCreateAllLettersUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Boolean aBoolean) {
        return Completable.fromAction(() -> manager.setCreateAllLetters(aBoolean));
    }
}
