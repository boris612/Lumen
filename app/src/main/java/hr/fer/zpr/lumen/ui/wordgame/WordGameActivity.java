package hr.fer.zpr.lumen.ui.wordgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import hr.fer.zpr.lumen.LumenApplication;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;

public class WordGameActivity extends DaggerActivity {


    @Inject
     WordGameView view;
    @Inject
     WordGamePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        view=new WordGameView(this.getLumenApplication());
        presenter.setView(view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.startGame();
        setContentView(view);


    }

}
