package hr.fer.zpr.lumen.wordgame.interactor;

import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import io.reactivex.Completable;

public class ChangeWordGameCoinAmountUseCase implements CompletableUseCaseWithParams<Integer> {

    private WordGameManager manager;

    public ChangeWordGameCoinAmountUseCase(WordGameManager manager){
        this.manager=manager;
    }

    @Override
    public Completable execute(Integer integer) {
        return Completable.fromAction(()->manager.setCoins(integer));
    }
}
