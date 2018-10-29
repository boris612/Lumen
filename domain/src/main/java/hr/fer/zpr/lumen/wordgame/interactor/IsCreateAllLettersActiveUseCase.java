package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Single;

public class IsCreateAllLettersActiveUseCase implements SingleUseCase<Boolean> {

    private WordGameManager manager;

    public IsCreateAllLettersActiveUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Single<Boolean> execute() {
        return manager.isCreateAllLettersActive();
    }
}
