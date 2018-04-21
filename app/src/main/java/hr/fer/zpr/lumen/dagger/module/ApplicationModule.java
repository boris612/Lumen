package hr.fer.zpr.lumen.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.dagger.application.ForApplication;
import hr.fer.zpr.lumen.dagger.application.LumenApplication;

@Module
public class ApplicationModule {

    private final LumenApplication lumenApplication;

    public ApplicationModule(final LumenApplication lumenApplication) {
        this.lumenApplication = lumenApplication;
    }

    @Provides
    @Singleton
    LumenApplication provideLumenApplication() {
        return lumenApplication;
    }

    @Provides
    @Singleton
    @ForApplication
    Context provideContext() {
        return lumenApplication;
    }
}
