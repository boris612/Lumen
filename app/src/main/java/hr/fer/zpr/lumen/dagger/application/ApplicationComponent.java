package hr.fer.zpr.lumen.dagger.application;

import javax.inject.Singleton;

import dagger.Component;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.dagger.modules.CoinGameModule;
import hr.fer.zpr.lumen.dagger.modules.WordGameUseCaseModule;
import hr.fer.zpr.lumen.dagger.modules.WordGameModule;
import hr.fer.zpr.lumen.ui.activities.CategorySelectionActivity;
import hr.fer.zpr.lumen.ui.activities.GameSettingsActivity;
import hr.fer.zpr.lumen.ui.activities.InfoActivity;
import hr.fer.zpr.lumen.ui.activities.WordGameSettingsActivity;
import hr.fer.zpr.lumen.ui.coingame.CoinGamePresenterImpl;
import hr.fer.zpr.lumen.ui.coingame.CoinGameView;
import hr.fer.zpr.lumen.ui.coingame.activity.CoinGameActivity;
import hr.fer.zpr.lumen.ui.wordgame.WordGameActivity;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenterImpl;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;

@Singleton
@Component(modules = {ApplicationModule.class, WordGameUseCaseModule.class, WordGameModule.class, CoinGameModule.class})
public interface ApplicationComponent {


    void inject(LumenApplication application);

    void inject(WordGamePresenterImpl presenter);

    void inject(DaggerActivity activity);

    void inject(WordGameView view);

    void inject(WordGameActivity activity);

    void inject(GameSettingsActivity activity);

    void inject(CoinGameView view);

    void inject(CoinGamePresenterImpl presenter);

    void inject(CategorySelectionActivity activity);

    void inject(InfoActivity activity);

    void inject(CoinGameActivity activity);

    void inject(WordGameSettingsActivity activity);

    final class Initializer {

        static public ApplicationComponent init(final LumenApplication lumenApplication) {
            return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(lumenApplication)).build();

        }
    }

}
