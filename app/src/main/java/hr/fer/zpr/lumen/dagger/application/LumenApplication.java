package hr.fer.zpr.lumen.dagger.application;

import android.app.Application;

public class LumenApplication extends Application {

    private ApplicationComponent applicationComponent;


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
