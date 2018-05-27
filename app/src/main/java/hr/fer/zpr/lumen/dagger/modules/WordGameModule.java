package hr.fer.zpr.lumen.dagger.modules;

import android.content.Context;
import android.media.MediaPlayer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.sound.SoundPlayer;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenter;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenterImpl;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;
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
    WordGameManager providesManager(WordGameRepository repository){
        return new WordGameManagerImpl(repository);
    }

    @Provides
    @Singleton
    WordGameRepository providesRepository(WordGameDatabase database){
        return new WordGameRepositoryImpl(database);
    }

    @Provides
    @Singleton
    DataDomainMapper providesMapper(){
        return new DataDomainMapper();
    }

    @Provides
    @Singleton
    WordGamePresenter providesWordGamePresenter(LumenApplication application) {
        return new WordGamePresenterImpl(application);
    }

    @Provides
    @Singleton
    WordGameView providesWordGameView(LumenApplication application){
        return new WordGameView(application);
    }

    @Provides
    @Singleton
    SoundPlayer providesSoundPlayer(){
        return new SoundPlayer();
    }


    @Provides
    @Singleton
    UseHintUseCase providesUseHintUseCase(WordGameManager manager){
        return new UseHintUseCase(manager);
    }
}
