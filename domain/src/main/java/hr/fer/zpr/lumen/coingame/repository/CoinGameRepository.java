package hr.fer.zpr.lumen.coingame.repository;

import java.util.List;

import hr.fer.zpr.lumen.coingame.model.Coin;
import io.reactivex.Single;

public interface CoinGameRepository {

    Single<List<Coin>> getCoinsForValues(List<Integer> values);
}
