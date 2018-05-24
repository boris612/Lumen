package hr.fer.zpr.lumen.dagger.application;

import javax.inject.Singleton;

import dagger.Component;
import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.base.BaseActivity;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.dagger.modules.UseCaseModule;
import hr.fer.zpr.lumen.dagger.modules.WordGameModule;
import hr.fer.zpr.lumen.ui.wordgame.WordGameActivity;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenter;
import hr.fer.zpr.lumen.ui.wordgame.WordGamePresenterImpl;
import hr.fer.zpr.lumen.ui.wordgame.WordGameView;
import wordgame.db.database.WordGameDatabase;
@Singleton
@Component(modules={ApplicationModule.class, UseCaseModule.class,WordGameModule.class})
public interface ApplicationComponent {



    void inject(LumenApplication application);

    void inject(WordGamePresenterImpl presenter);

    void inject(DaggerActivity activity);

    void inject(WordGameView view);

    void inject(WordGameActivity activity);



    final class Initializer {

        static public ApplicationComponent init(final LumenApplication lumenApplication) {
            return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(lumenApplication)).build();

        }
    }

}
