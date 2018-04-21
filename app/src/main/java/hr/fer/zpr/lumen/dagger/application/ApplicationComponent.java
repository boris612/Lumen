package hr.fer.zpr.lumen.dagger.application;

import javax.inject.Singleton;

import dagger.Component;
import hr.fer.zpr.lumen.dagger.module.ApplicationModule;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface ApplicationComponent extends ApplicationComponentInjects {

    final class Initializer {
        static public ApplicationComponent init(final LumenApplication lumenApplication) {
            return DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(lumenApplication))
                    .build();
        }
    }
}
