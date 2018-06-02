package hr.fer.zpr.lumen.ui.coingame.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import javax.inject.Inject;

import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.ui.coingame.CoinGamePresenter;
import hr.fer.zpr.lumen.ui.coingame.CoinGameView;

public class CoinGameActivity extends DaggerActivity {

    @Inject
    CoinGamePresenter presenter;
    private CoinGameView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        view = new CoinGameView(this.getLumenApplication());
        presenter.setView(view);
        setContentView(view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.nextRound();


    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.exit();
    }
}
