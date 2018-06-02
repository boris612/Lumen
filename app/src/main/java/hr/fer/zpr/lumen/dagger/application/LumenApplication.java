package hr.fer.zpr.lumen.dagger.application;


import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import hr.fer.zpr.lumen.BuildConfig;
import hr.fer.zpr.lumen.dagger.ComponentFactory;
import hr.fer.zpr.lumen.database.loader.DatabaseLoader;

public class LumenApplication extends Application {

    @Inject
    DatabaseLoader loader;
    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = ComponentFactory.createApplicationComponent(this);
        component.inject(this);
        loader.load();
        if(BuildConfig.DEBUG){
        Stetho.initializeWithDefaults(this);
        }

    }

    public ApplicationComponent getApplicationComponent() {
        return component;
    }
}
