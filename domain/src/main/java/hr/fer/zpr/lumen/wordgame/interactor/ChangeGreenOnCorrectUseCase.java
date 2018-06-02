package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeGreenOnCorrectUseCase implements CompletableUseCaseWithParams<Boolean> {

    private WordGameManager manager;

    public ChangeGreenOnCorrectUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Boolean aBoolean) {
        return Completable.fromAction(() -> manager.setGreenOnCorrect(aBoolean));
    }
}
