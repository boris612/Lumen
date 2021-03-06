package hr.fer.zpr.lumen.coingame.repository;

import java.util.List;

import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.wordgame.model.Language;
import io.reactivex.Single;

public interface CoinGameRepository {

    Single<List<Coin>> getCoinsForValues(List<Integer> values);

    public Single<String> getCorrectMessage(Language language);

    public Single<String> tryAgainMessage(Language language);
}
