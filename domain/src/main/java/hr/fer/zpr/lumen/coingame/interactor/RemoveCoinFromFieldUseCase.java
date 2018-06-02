package hr.fer.zpr.lumen.coingame.interactor;

import hr.fer.zpr.lumen.coingame.manager.CoinGameManager;
import hr.fer.zpr.lumen.wordgame.interactor.CompletableUseCaseWithParams;
import io.reactivex.Completable;

public class RemoveCoinFromFieldUseCase implements CompletableUseCaseWithParams<Integer> {

    private CoinGameManager manager;

    public RemoveCoinFromFieldUseCase(CoinGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(Integer integer) {
        return Completable.fromAction(() -> manager.removeCoinFromField(integer));
    }
}
