package hr.fer.zpr.lumen.coingame.manager;


import java.util.List;

import hr.fer.zpr.lumen.coingame.ProblemGenerator;
import hr.fer.zpr.lumen.coingame.ProblemGeneratorImpl;
import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.repository.CoinGameRepository;
import io.reactivex.Single;

public class CoinGameManagerImpl implements CoinGameManager {

    private CoinGameRepository repository;

    private ProblemGenerator generator;

    public CoinGameManagerImpl(CoinGameRepository repository){
        this.repository=repository;
        generator=new ProblemGeneratorImpl();
    }

    @Override
    public Single<List<Coin>> newRound() {
        return null;
    }

    @Override
    public void insertCoinIntoField(int value) {

    }

    @Override
    public void removeCoinFromField(int value) {

    }

    @Override
    public Single<Boolean> isAnswerCorrect() {
        return null;
    }
}
