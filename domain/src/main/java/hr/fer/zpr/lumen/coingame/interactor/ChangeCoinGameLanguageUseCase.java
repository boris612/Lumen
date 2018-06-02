package hr.fer.zpr.lumen.coingame.interactor;

import hr.fer.zpr.lumen.coingame.manager.CoinGameManager;
import hr.fer.zpr.lumen.wordgame.interactor.CompletableUseCaseWithParams;
import io.reactivex.Completable;

public class ChangeCoinGameLanguageUseCase implements CompletableUseCaseWithParams<String> {

    private CoinGameManager manager;

    public ChangeCoinGameLanguageUseCase(CoinGameManager manager) {
        this.manager = manager;
    }

    @Override
    public Completable execute(String s) {
        return Completable.fromAction(() -> manager.setLanguage(s));
    }
}
