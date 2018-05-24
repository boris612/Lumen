package hr.fer.zpr.lumen;


import android.app.Application;
import android.os.Debug;

import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.ComponentFactory;
import hr.fer.zpr.lumen.dagger.application.ApplicationComponent;
import hr.fer.zpr.lumen.database.loader.DatabaseLoader;
import wordgame.db.database.WordGameDatabase;

public class LumenApplication extends Application {

    private  ApplicationComponent component;

    @Inject
    DatabaseLoader loader;

    @Override
    public void onCreate() {
        super.onCreate();
        component= ComponentFactory.createApplicationComponent(this);
        component.inject(this);
        loader.load();
        Stetho.initializeWithDefaults(this);

    }

    public  ApplicationComponent getApplicationComponent() {
        return component;
    }
}
