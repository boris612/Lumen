package hr.fer.zpr.lumen.coingame.manager;

import java.util.List;

import hr.fer.zpr.lumen.coingame.model.Coin;
import io.reactivex.Single;

public interface CoinGameManager {

    Single<List<Coin>> newRound();

    void insertCoinIntoField(int value);

    void removeCoinFromField(int value);

    Single<Boolean> isAnswerCorrect();

}
