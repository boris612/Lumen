package hr.fer.zpr.lumen.dagger;

import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.dagger.application.ApplicationComponent;

public final class ComponentFactory {


    private ComponentFactory() {
    }

    public static ApplicationComponent createApplicationComponent(final LumenApplication lumenApplication) {
        return ApplicationComponent.Initializer.init(lumenApplication);
    }




}