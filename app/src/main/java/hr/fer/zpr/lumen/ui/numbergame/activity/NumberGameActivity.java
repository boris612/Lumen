package hr.fer.zpr.lumen.ui.numbergame.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.ui.numbergame.presenter.NumberGamePresenter;
import hr.fer.zpr.lumen.ui.numbergame.view.NumberGameView;

public class NumberGameActivity extends DaggerActivity {

    @Inject
    NumberGameView view;
    @Inject
    NumberGamePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        view = new NumberGameView(this.getLumenApplication());
//        presenter.setView(view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
//        presenter.exit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        presenter.startGame();
    }

}
