package hr.fer.zpr.lumen.ui.coingame;

import hr.fer.zpr.lumen.ui.coingame.models.CoinModel;

public interface CoinGamePresenter {

    boolean shouldHandleTouch();

    void coinInserted(CoinModel coin);

    void coinRemoved(CoinModel coin);

    void nextRound();

    void setView(CoinGameView view);

    void exit();

    void init();
}
