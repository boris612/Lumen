package hr.fer.zpr.lumen.ui.wordgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;

import javax.inject.Inject;

import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;
import hr.fer.zpr.lumen.wordgame.manager.WordGameManager;

public class WordGameActivity extends DaggerActivity {


    @Inject
    WordGameView view;
    @Inject
    WordGamePresenter presenter;
    @Inject
    WordGameManager manager;

    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_game);
        view = new WordGameView(this.getLumenApplication());
        constraintLayout = findViewById(R.id.gameLayout);
        HorizontalScrollView scrollView = new HorizontalScrollView(this.getLumenApplication());
        view.setScrollView(scrollView);
        constraintLayout.addView(view);
        if(manager.isCreateAllLettersActive().blockingGet()){
            constraintLayout.addView(scrollView);
            scrollView.setVisibility(ViewGroup.VISIBLE);
            scrollView.setBottom(View.SCROLL_INDICATOR_BOTTOM);
        }

        presenter.setView(view);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        presenter.exit();
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startGame();
    }


}
