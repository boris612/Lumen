package coingame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import coingame.model.DbCoin;
import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.model.Image;
import hr.fer.zpr.lumen.coingame.repository.CoinGameRepository;
import hr.fer.zpr.lumen.wordgame.model.Language;
import io.reactivex.Single;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.model.DbCorrectMessage;
import wordgame.db.model.DbTryAgainMessage;

public class CoinGameRepositoryImpl implements CoinGameRepository {

    private HashMap<Integer, String> coinImagesPaths = new HashMap<>();

    private WordGameDatabase database;

    public CoinGameRepositoryImpl(WordGameDatabase database) {
        this.database = database;
    }

    @Override
    public Single<List<Coin>> getCoinsForValues(List<Integer> values) {
        List<Coin> result=new ArrayList<>();
        for(Integer i:values){
            DbCoin coin=database.coinDao().getForValue(i);
            result.add(new Coin(coin.value,new Image(coin.imagePath)));
        }
        return Single.just(result);
    }

    @Override
    public Single<String> getCorrectMessage(Language language) {
        List<DbCorrectMessage> messages = database.correctDao().getMessages(database.languageDao().findByValue(language.name().toLowerCase()).id);
        return Single.just(messages.get(new Random().nextInt(messages.size())
        ).path);
    }

    @Override
    public Single<String> tryAgainMessage(Language language) {
        List<DbTryAgainMessage> messages = database.againMessageDao().getMessages(database.languageDao().findByValue(language.name().toLowerCase()).id);
        return Single.just(messages.get(new Random().nextInt(messages.size())).path);
    }

}
