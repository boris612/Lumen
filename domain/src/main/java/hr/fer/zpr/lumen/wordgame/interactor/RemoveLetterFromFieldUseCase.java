package hr.fer.zpr.lumen.wordgame.interactor.word;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.model.Letter;
import io.reactivex.Completable;

public class RemoveLetterFromFieldUseCase implements CompletableUseCaseWithParams<Letter> {

    private WordGameManager manager;

    public RemoveLetterFromFieldUseCase(WordGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Letter letter) {
       return Completable.fromAction(()->manager.removeLetterFromField(letter));
    }
}
