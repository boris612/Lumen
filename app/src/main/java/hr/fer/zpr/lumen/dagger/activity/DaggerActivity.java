package hr.fer.zpr.lumen.dagger.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.application.LumenApplication;
import hr.fer.zpr.lumen.dagger.ComponentFactory;
import hr.fer.zpr.lumen.dagger.application.ApplicationComponent;
import hr.fer.zpr.lumen.database.loader.DatabaseLoader;
import wordgame.db.database.WordGameDatabase;

public abstract  class DaggerActivity extends AppCompatActivity {

    protected ApplicationComponent component;

    @Inject
    protected WordGameDatabase database;

    @Inject
    protected DatabaseLoader loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component= ComponentFactory.createApplicationComponent(getLumenApplication());
        getLumenApplication().getApplicationComponent().inject(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public LumenApplication getLumenApplication(){
        return (LumenApplication) getApplication();
    }
}
