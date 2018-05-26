package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;
import io.reactivex.Single;

public class SetCreateMoreLettersUseCase implements CompletableUseCaseWithParams<Boolean>{

    private WordGameManager manager;

    public SetCreateMoreLettersUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Completable execute(Boolean aBoolean) {
        return Completable.fromAction(()->manager.setCreateMoreLetters(aBoolean));
    }
}
