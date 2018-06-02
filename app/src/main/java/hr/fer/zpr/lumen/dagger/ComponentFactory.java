package hr.fer.zpr.lumen.dagger;

import hr.fer.zpr.lumen.dagger.application.ApplicationComponent;
import hr.fer.zpr.lumen.dagger.application.LumenApplication;

public final class ComponentFactory {


    private ComponentFactory() {
    }

    public static ApplicationComponent createApplicationComponent(final LumenApplication lumenApplication) {
        return ApplicationComponent.Initializer.init(lumenApplication);
    }


}