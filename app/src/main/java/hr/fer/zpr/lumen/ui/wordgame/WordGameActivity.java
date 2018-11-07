package hr.fer.zpr.lumen.ui.wordgame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import hr.fer.zpr.lumen.R;
import hr.fer.zpr.lumen.dagger.activity.DaggerActivity;

import javax.inject.Inject;

public class WordGameActivity extends DaggerActivity {


    @Inject
    WordGameView view;
    @Inject
    WordGamePresenter presenter;

    private ViewGroup constraintLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getLumenApplication().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_game);
        view = new WordGameView(this.getLumenApplication());
        //OVO staviti samo ako je ukljuceno stvaranje svih slova
        constraintLayout = findViewById(R.id.gameLayout);
        TextView textView = new TextView(this.getLumenApplication());

        HorizontalScrollView scrollView = new HorizontalScrollView(this.getLumenApplication());
        view.setScrollView(scrollView);
        scrollView.setVisibility(ViewGroup.VISIBLE);

        constraintLayout.addView(view);
        constraintLayout.addView(scrollView);
        scrollView.setX(0);
        scrollView.setY(630);
        scrollView.setBottom(View.SCROLL_INDICATOR_BOTTOM);
//do tud
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
