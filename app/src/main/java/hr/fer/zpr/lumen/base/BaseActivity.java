package hr.fer.zpr.lumen.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wordgame.db.database.WordGameDatabase;


public abstract  class BaseActivity extends AppCompatActivity implements ScopedPresenter {


    @Inject
    protected WordGameDatabase database;

    @Inject
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().activate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().destroy();
    }

    protected abstract ScopedPresenter getPresenter();
}
