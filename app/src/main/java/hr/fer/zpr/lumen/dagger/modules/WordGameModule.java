package hr.fer.zpr.lumen.dagger.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.sound.SoundPlayer;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenter;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenterImpl;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;
import hr.fer.zpr.lumen.ui.wordgame.mapping.WordGameMapper;
import hr.fer.zpr.lumen.ui.wordgame.util.ViewConstants;
import hr.fer.zpr.lumen.wordgame.interactor.ChangeWordGameCoinAmountUseCase;
import hr.fer.zpr.lumen.wordgame.interactor.UseHintUseCase;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManagerImpl;
import hr.fer.zpr.lumen.wordgame.repository.WordGameRepository;
import wordgame.db.database.WordGameDatabase;
import wordgame.db.mapping.DataDomainMapper;
import wordgame.db.repository.WordGameRepositoryImpl;

@Module
public class WordGameModule {

    @Provides
    @Singleton
    WordGameManager providesManager(WordGameRepository repository, SharedPreferences preferences) {
        WordGameManager manager = new WordGameManagerImpl(repository);
        manager.setCoins(preferences.getInt(ViewConstants.PREFERENCES_COINS, 0));
        manager.setCreateMoreLetters(preferences.getBoolean(ViewConstants.PREFERENCES_LETTERS, false));
        manager.setGreenOnCorrect(preferences.getBoolean(ViewConstants.PREFERENCES_GREEN_ON_CORRECT, false));
        manager.changeCategories(preferences.getStringSet(ViewConstants.PREFERENCES_CATEGORIES, null));
        manager.setLanguage(preferences.getString(ViewConstants.PREFERENCES_LANGUAGE, "croatian"));
        return manager;
    }


    @Provides
    @Singleton
    WordGameRepository providesRepository(WordGameDatabase database) {
        return new WordGameRepositoryImpl(database);
    }

    @Provides
    @Singleton
    DataDomainMapper providesMapper() {
        return new DataDomainMapper();
    }

    @Provides
    @Singleton
    WordGamePresenter providesWordGamePresenter(LumenApplication application) {
        return new WordGamePresenterImpl(application);
    }

    @Provides
    @Singleton
    WordGameView providesWordGameView(LumenApplication application) {
        return new WordGameView(application);
    }

    @Provides
    @Singleton
    SoundPlayer providesSoundPlayer() {
        return new SoundPlayer();
    }


    @Provides
    @Singleton
    UseHintUseCase providesUseHintUseCase(WordGameManager manager) {
        return new UseHintUseCase(manager);
    }

    @Provides
    @Singleton
    WordGameMapper providesWordGameMapper(Context context) {
        return new WordGameMapper(context);
    }


    @Provides
    @Singleton
    ChangeWordGameCoinAmountUseCase changeWordGameCoinAmountUseCase(WordGameManager manager){
        return new ChangeWordGameCoinAmountUseCase(manager);
    }
}
