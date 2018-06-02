package hr.fer.zpr.lumen.ui.coingame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import hr.fer.zpr.lumen.coingame.interactor.InsertCoinIntoFieldUseCase;
import hr.fer.zpr.lumen.coingame.interactor.RemoveCoinFromFieldUseCase;
import hr.fer.zpr.lumen.coingame.manager.CoinGameManager;
import hr.fer.zpr.lumen.coingame.model.Coin;
import hr.fer.zpr.lumen.coingame.model.CoinGamePhase;
import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.player.SoundPlayer;
import hr.fer.zpr.lumen.ui.DebugUtil;
import hr.fer.zpr.lumen.ui.coingame.mapping.CoinGameMapper;
import hr.fer.zpr.lumen.ui.coingame.models.CoinFieldModel;
import hr.fer.zpr.lumen.ui.coingame.models.CoinModel;
import hr.fer.zpr.lumen.ui.coingame.models.NumberLabel;
import hr.fer.zpr.lumen.ui.coingame.util.CoinGameConstants;
import hr.fer.zpr.lumen.ui.wordgame.models.ImageModel;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class CoinGamePresenterImpl implements CoinGamePresenter {

    @Inject
    CoinGameManager manager;

    @Inject
    Context context;


    @Inject
    SoundPlayer player;

    @Inject
    SharedPreferences preferences;

    @Inject
    CoinGameMapper mapper;

    @Inject
    InsertCoinIntoFieldUseCase insertCoinIntoFieldUseCase;

    @Inject
    RemoveCoinFromFieldUseCase removeCoinFromFieldUseCase;


    private CoinFieldModel field;


    private NumberLabel counter;

    private NumberLabel goal;

    private boolean initialized = false;


    private hr.fer.zpr.lumen.ui.viewmodels.CoinModel coin;

    private CoinGameView view;

    private Disposable nextRoundDisposable;

    private ImageModel tick;

    public CoinGamePresenterImpl(LumenApplication app) {
        app.getApplicationComponent().inject(this);
        goal = mapper.goalLabel(0);
        try {

            coin = mapper.createHintCoin();
        } catch (Exception e) {
            DebugUtil.LogDebug(e);
        }


        manager.setLanguage(preferences.getString(ViewConstants.PREFERENCES_LANGUAGE, ViewConstants.DEFAULT_LANGUAGE));
    }

    @Override
    public void setView(CoinGameView view) {
        this.view = view;
    }


    @Override
    public boolean shouldHandleTouch() {
        return manager.isGamePhasePlaying().blockingGet();
    }

    @Override
    public void coinInserted(CoinModel coin) {
        insertCoinIntoFieldUseCase.execute(coin.getValue()).blockingGet();
        field.addCoin(coin);
        counter.setValue(manager.getCurrentSum().blockingGet());
        checkIfAnswerCorrect();
    }

    private void onCorrectAnswer() {
        view.addDrawable(tick);
        manager.changeGamePhase(CoinGamePhase.ENDING);
        coin.setCoins(manager.getCoinsAmount().blockingGet());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ViewConstants.PREFERENCES_COINS, manager.getCoinsAmount().blockingGet());
        editor.commit();

        nextRoundDisposable = Completable.timer(CoinGameConstants.TIME_UNTIL_NEXT_ROUND, TimeUnit.MILLISECONDS).subscribe(() -> nextRound());

    }


    @Override
    public void coinRemoved(CoinModel coin) {
        removeCoinFromFieldUseCase.execute(coin.getValue()).blockingGet();
        field.removeCoin(coin);
        counter.setValue(manager.getCurrentSum().blockingGet());
        checkIfAnswerCorrect();
    }

    private void checkIfAnswerCorrect() {
        boolean correct = manager.isAnswerCorrect().blockingGet();
        boolean optimal = manager.isAnswerOptimal().blockingGet();
        if (correct && optimal) {
            onCorrectAnswer();
        }

    }

    @Override
    public void init() {
        goal = mapper.goalLabel(0);
        manager.setCoins(preferences.getInt(ViewConstants.PREFERENCES_COINS, 0));
        coin.setCoins(manager.getCoinsAmount().blockingGet());
        counter = mapper.currentLabel();
        field = mapper.createField();
        view.setField(field);
        view.addDrawable(goal);
        view.addDrawable(counter);
        view.addDrawable(coin);
        tick = mapper.generateTick();
        initialized = true;
    }

    @Override
    public void nextRound() {
        if (!initialized) init();
        view.clearCoins();
        view.removeDrawable(tick);
        counter.setValue(0);
        List<Coin> coins = manager.newRound().blockingGet();
        manager.changeGamePhase(CoinGamePhase.PLAYING);
        goal.setValue(manager.getCurrentGoal().blockingGet());
        List<CoinModel> models = mapper.getModelsForCoins(coins);
        for (CoinModel model : models) {
            view.addDrawable(model);
        }
        view.setCoins(models);


    }

    @Override
    public void exit() {
        if (nextRoundDisposable != null) nextRoundDisposable.dispose();
        player.stopPlaying();
        view.clearCoins();
        initialized = false;
    }
}
