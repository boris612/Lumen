package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import io.reactivex.Completable;

public class RemoveLetterFromFieldUseCase implements CompletableUseCaseWithParams<Integer> {

    private WordGameManager manager;

    public RemoveLetterFromFieldUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Integer index) {
       return Completable.fromAction(()->manager.removeLetterFromField(index));
    }
}
