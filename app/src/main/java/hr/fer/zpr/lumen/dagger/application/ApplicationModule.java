package hr.fer.zpr.lumen.dagger.application;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hr.fer.zpr.lumen.database.loader.DatabaseLoader;
import hr.fer.zpr.lumen.database.loader.DatabaseLoaderImpl;
import hr.fer.zpr.lumen.localization.LocalizationProvider;
import hr.fer.zpr.lumen.localization.LocalizationProviderImpl;
import wordgame.db.database.WordGameDatabase;


@Module
public class ApplicationModule {

    public static final String pref_name = "Lumen";
    private final String database_name = "lumen_database";
    private final LumenApplication app;


    public ApplicationModule(final LumenApplication app) {
        this.app = app;
    }

    @Provides
    @Singleton
    LumenApplication providesApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return app;
    }

    @Provides
    @Singleton
    SharedPreferences providesPreferences() {
        return app.getSharedPreferences(pref_name, Context.MODE_PRIVATE);
    }


    @Provides
    @Singleton
    WordGameDatabase providesDatabase(LumenApplication application) {
        return Room.databaseBuilder(application, WordGameDatabase.class, database_name).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    @Provides
    @Singleton
    DatabaseLoader providesLoader(LumenApplication application, WordGameDatabase database) {
        return new DatabaseLoaderImpl(application, database);
    }

    @Provides
    @Singleton
    LocalizationProvider providesLocalizationProvider(Context context) {
        return new LocalizationProviderImpl(context);
    }


}
