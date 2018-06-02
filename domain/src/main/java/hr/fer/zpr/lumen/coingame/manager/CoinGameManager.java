package hr.fer.zpr.lumen.coingame.manager;

import java.util.List;

import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.model.CoinGamePhase;
import io.reactivex.Single;

public interface CoinGameManager {

    Single<List<Coin>> newRound();

    void insertCoinIntoField(int value);

    void removeCoinFromField(int value);

    Single<Boolean> isAnswerCorrect();

    Single<Integer> getCurrentSum();

    Single<Boolean> isAnswerOptimal();

    Single<Boolean> isGamePhasePlaying();

    void changeGamePhase(CoinGamePhase phase);

    Single<String> getTryAgainMessage();

    Single<String> getCorrectMessage();

    void setLanguage(String language);

    Single<Integer> getCurrentGoal();

    void setCoins(int value);

    Single<Integer> getCoinsAmount();

}
