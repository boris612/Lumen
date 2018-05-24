package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class SetHintActiveUseCase implements CompletableUseCaseWithParams<Boolean>
{

    private WordGameManager manager;

    public SetHintActiveUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Completable execute(Boolean hintActive) {
        return Completable.fromAction(()->manager.setHint(hintActive));
    }
}
