package hr.fer.zpr.lumen.coingame.manager;


import java.util.ArrayList;
import java.util.List;

import hr.fer.zpr.lumen.coingame.ProblemGenerator;
import hr.fer.zpr.lumen.coingame.ProblemGeneratorImpl;
import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.model.CoinField;
import hr.fer.zpr.lumen.coingame.model.CoinGamePhase;
import hr.fer.zpr.lumen.coingame.repository.CoinGameRepository;
import hr.fer.zpr.lumen.coingame.util.CoinUtil;
import hr.fer.zpr.lumen.wordgame.model.Coins;
import hr.fer.zpr.lumen.wordgame.model.Language;
import io.reactivex.Single;

public class CoinGameManagerImpl implements CoinGameManager {

    public static final int MAX_GOAL = 20;
    public static final int MIN_GOAL = 7;
    private CoinGameRepository repository;
    private ProblemGenerator generator;
    private int currentGoal;

    private CoinField field;

    private CoinGamePhase phase;

    private Coins coins;

    private Language currentLanguage;


    private List<Coin> optimalCoins;

    public CoinGameManagerImpl(CoinGameRepository repository) {
        this.repository = repository;
        generator = new ProblemGeneratorImpl();
        field = new CoinField();
        coins = new Coins(0);
    }

    @Override
    public void setLanguage(String language) {
        currentLanguage = Language.valueOf(language.toUpperCase());
    }

    @Override
    public Single<List<Coin>> newRound() {
        currentGoal = generator.generateRandomNumber(MIN_GOAL, MAX_GOAL);
        field.clearField();
        optimalCoins = repository.getCoinsForValues(generator.generateOptimalCoinsFor(currentGoal)).blockingGet();
        List<Coin> allCoins = new ArrayList<>(optimalCoins);
        allCoins.addAll(repository.getCoinsForValues(generator.generateRandomCoins()).blockingGet());
        phase = CoinGamePhase.PLAYING;
        return Single.just(allCoins);
    }

    @Override
    public void insertCoinIntoField(int value) {
        field.insertCoinIntoField(value);
    }

    @Override
    public void removeCoinFromField(int value) {
        field.removeCoinFromField(value);
    }

    @Override
    public Single<Boolean> isAnswerCorrect() {
        if (field.sumOfCoins() != currentGoal) return Single.just(false);
        return Single.just(true);
    }


    @Override
    public Single<Boolean> isAnswerOptimal() {
        boolean optimal=CoinUtil.compareIntegerLists(CoinUtil.coinsToIntList(optimalCoins), field.coinToIntList());
        if(optimal) coins.addCoins(2);
        return Single.just(optimal);
    }

    @Override
    public Single<Boolean> isGamePhasePlaying() {
        return Single.just(phase == CoinGamePhase.PLAYING);
    }

    @Override
    public void changeGamePhase(CoinGamePhase phase) {
        this.phase = phase;
    }

    @Override
    public Single<String> getCorrectMessage() {
        return repository.getCorrectMessage(currentLanguage);
    }

    @Override
    public Single<String> getTryAgainMessage() {
        return repository.tryAgainMessage(currentLanguage);
    }

    @Override
    public Single<Integer> getCurrentGoal() {
        return Single.just(currentGoal);
    }

    @Override
    public Single<Integer> getCurrentSum() {
        return Single.just(field.sumOfCoins());
    }

    @Override
    public void setCoins(int value) {
        coins.setCoins(value);
    }

    public Single<Integer> getCoinsAmount() {
        return Single.just(coins.getCoins());
    }
}
