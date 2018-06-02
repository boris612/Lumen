package hr.fer.zpr.lumen.dagger.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import coingame.CoinGameRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameCoinAmountUseCase;
import hr.fer.zpr.lumen.coingame.interactor.ChangeCoinGameLanguageUseCase;
import hr.fer.zpr.lumen.coingame.interactor.InsertCoinIntoFieldUseCase;
import hr.fer.zpr.lumen.coingame.interactor.RemoveCoinFromFieldUseCase;
import hr.fer.zpr.lumen.coingame.manager.CoinGameManager;
import hr.fer.zpr.lumen.coingame.manager.CoinGameManagerImpl;
import hr.fer.zpr.lumen.coingame.repository.CoinGameRepository;
import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.player.SoundPlayer;
import hr.fer.zpr.lumen.ui.coingame.CoinGamePresenter;
import hr.fer.zpr.lumen.ui.coingame.CoinGamePresenterImpl;
import hr.fer.zpr.lumen.ui.coingame.mapping.CoinGameMapper;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import wordgame.db.database.WordGameDatabase;

@Module
public class CoinGameModule {

    @Provides
    @Singleton
    CoinGameMapper providesMapper(Context context, SharedPreferences preferences) {
        return new CoinGameMapper(context, preferences);
    }

    @Provides
    @Singleton
    CoinGameRepository providesRepository(WordGameDatabase database) {
        return new CoinGameRepositoryImpl(database);
    }

    @Provides
    @Singleton
    CoinGameManager providesManager(CoinGameRepository repository, SharedPreferences preferences,SoundPlayer player) {
        CoinGameManager manager = new CoinGameManagerImpl(repository,player);
        int coins = preferences.getInt(ViewConstants.PREFERENCES_COINS, 0);
        manager.setCoins(coins);
        manager.setLanguage(preferences.getString(ViewConstants.PREFERENCES_GUI_LANGUAGE, ViewConstants.DEFAULT_GUI_LANGUAGE));
        return manager;
    }

    @Provides
    @Singleton
    InsertCoinIntoFieldUseCase providesInsertCoinIntoFieldUseCase(CoinGameManager manager) {
        return new InsertCoinIntoFieldUseCase(manager);
    }

    @Provides
    @Singleton
    RemoveCoinFromFieldUseCase providesRemoveCoinFromFieldUseCase(CoinGameManager manager) {
        return new RemoveCoinFromFieldUseCase(manager);
    }

    @Provides
    @Singleton
    CoinGamePresenter providesPresenter(LumenApplication application) {
        return new CoinGamePresenterImpl(application);
    }

    @Provides
    @Singleton
    ChangeCoinGameLanguageUseCase changeCoinGameLanguageUseCase(CoinGameManager manager) {
        return new ChangeCoinGameLanguageUseCase(manager);
    }

    @Provides
    @Singleton
    ChangeCoinGameCoinAmountUseCase changeCoinGameCoinAmountUseCase(CoinGameManager manager){
        return new ChangeCoinGameCoinAmountUseCase(manager);
    }
}
